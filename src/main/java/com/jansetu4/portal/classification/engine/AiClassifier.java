package com.jansetu4.portal.classification.engine;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jansetu4.portal.citizen.entity.Challenge;
import com.jansetu4.portal.common.enums.ClassificationMethod;
import com.jansetu4.portal.common.enums.Domain;
import com.jansetu4.portal.config.AiConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Classifies challenges by calling an OpenAI-compatible Chat Completions API.
 * Falls back to the rule-based classifier if no API key is configured or the call fails.
 */
@Slf4j
@Primary
@Component
public class AiClassifier implements ClassificationEngine {

    private final AiConfig aiConfig;
    private final RuleBasedClassifier fallbackClassifier;
    private final ObjectMapper objectMapper;
    private final RestClient restClient = RestClient.create();

    public AiClassifier(AiConfig aiConfig, RuleBasedClassifier fallbackClassifier, ObjectMapper objectMapper) {
        this.aiConfig = aiConfig;
        this.fallbackClassifier = fallbackClassifier;
        this.objectMapper = objectMapper;
    }

    @Override
    public ClassificationResult classify(Challenge challenge) {
        if (!StringUtils.hasText(aiConfig.getApiKey())) {
            log.warn("No AI API key configured; falling back to rule-based classifier");
            return fallbackClassifier.classify(challenge);
        }

        try {
            String domainList = String.join(", ", Arrays.stream(Domain.values()).map(Enum::name).toList());
            String systemPrompt = "You are a civic-issue triage assistant. Classify the citizen complaint into exactly one "
                    + "of these domains: " + domainList + ". Respond ONLY with a JSON object in the form "
                    + "{\"domain\": \"<ONE_OF_THE_DOMAINS>\", \"confidence\": <number between 0.0 and 1.0>}.";
            String userPrompt = "Title: " + challenge.getTitle()
                    + "\nDescription: " + challenge.getDescription()
                    + "\nAddress: " + (challenge.getAddress() != null ? challenge.getAddress() : "N/A");

            Map<String, Object> requestBody = Map.of(
                    "model", aiConfig.getModel(),
                    "temperature", 0,
                    "response_format", Map.of("type", "json_object"),
                    "messages", List.of(
                            Map.of("role", "system", "content", systemPrompt),
                            Map.of("role", "user", "content", userPrompt)
                    )
            );

            JsonNode response = restClient.post()
                    .uri(aiConfig.getBaseUrl() + "/chat/completions")
                    .header("Authorization", "Bearer " + aiConfig.getApiKey())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(requestBody)
                    .retrieve()
                    .body(JsonNode.class);

            String content = response.path("choices").path(0).path("message").path("content").asText();
            JsonNode parsed = objectMapper.readTree(content);

            Domain domain = Domain.valueOf(parsed.path("domain").asText().trim().toUpperCase(java.util.Locale.ROOT));
            double confidence = Math.max(0.0, Math.min(1.0, parsed.path("confidence").asDouble(0.5)));

            return ClassificationResult.builder()
                    .domain(domain)
                    .confidenceScore(confidence)
                    .method(ClassificationMethod.AI_BASED)
                    .build();
        } catch (Exception ex) {
            log.error("AI classification failed ({}); falling back to rule-based classifier", ex.getMessage());
            return fallbackClassifier.classify(challenge);
        }
    }
}
