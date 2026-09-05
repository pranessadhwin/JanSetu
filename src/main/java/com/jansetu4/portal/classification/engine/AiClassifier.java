package com.jansetu4.portal.classification.engine;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jansetu4.portal.citizen.entity.Challenge;
import com.jansetu4.portal.common.enums.ClassificationMethod;
import com.jansetu4.portal.common.enums.Domain;
import com.jansetu4.portal.common.enums.ResolutionTrack;
import com.jansetu4.portal.config.AiConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Classifies challenges by calling an OpenAI-compatible Chat Completions API.
 * There is no local/rule-based fallback: if the API key is missing or the
 * call fails, the challenge is marked UNCLASSIFIED so it can be reviewed and
 * classified manually instead of being routed on a guess.
 */
@Slf4j
@Component
public class AiClassifier implements ClassificationEngine {

    private final AiConfig aiConfig;
    private final ObjectMapper objectMapper;
    private final RestClient restClient = RestClient.create();

    public AiClassifier(AiConfig aiConfig, ObjectMapper objectMapper) {
        this.aiConfig = aiConfig;
        this.objectMapper = objectMapper;
    }

    @Override
    public ClassificationResult classify(Challenge challenge) {
        if (!StringUtils.hasText(aiConfig.getApiKey())) {
            log.warn("No AI API key configured; marking challenge {} as unclassified", challenge.getId());
            return unclassifiedResult();
        }

        try {
            String domainList = String.join(", ", Arrays.stream(Domain.values())
                    .filter(domain -> domain != Domain.UNCLASSIFIED)
                    .map(Enum::name).toList());
            String systemPrompt = "You are a civic-issue triage assistant. Classify the citizen complaint into exactly one "
                    + "of these domains: " + domainList + ". Also decide the resolution track: "
                    + "MUNICIPAL_ROUTINE if this is a routine civic maintenance issue that an existing local "
                    + "government/municipal department handles as daily business (e.g. pothole repair, streetlight "
                    + "replacement, garbage collection, a burst pipe), or INNOVATION_REQUIRED if it is a complex, "
                    + "recurring, or systemic problem that needs a researched or newly-designed solution from a "
                    + "university or industry partner. Respond ONLY with a JSON object in the form "
                    + "{\"domain\": \"<ONE_OF_THE_DOMAINS>\", "
                    + "\"resolutionTrack\": \"MUNICIPAL_ROUTINE|INNOVATION_REQUIRED\"}.";
            String userPrompt = "Title: " + challenge.getTitle()
                    + "\nDescription: " + challenge.getDescription()
                    + "\nAddress: " + (challenge.getAddress() != null ? challenge.getAddress() : "N/A");

            Map<String, Object> requestBody = Map.of(
                    "model", aiConfig.getModel(),
                    "temperature", 0,
                    "max_tokens", 100,
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
            ResolutionTrack resolutionTrack = parseResolutionTrack(parsed.path("resolutionTrack").asText(""));

            return ClassificationResult.builder()
                    .domain(domain)
                    .method(ClassificationMethod.AI_BASED)
                    .resolutionTrack(resolutionTrack)
                    .build();
        } catch (Exception ex) {
            log.error("AI classification failed for challenge {} ({}); marking as unclassified",
                    challenge.getId(), ex.getMessage());
            return unclassifiedResult();
        }
    }

    private ClassificationResult unclassifiedResult() {
        return ClassificationResult.builder()
                .domain(Domain.UNCLASSIFIED)
                .method(ClassificationMethod.UNAVAILABLE)
                .resolutionTrack(ResolutionTrack.UNCLASSIFIED)
                .build();
    }

    private ResolutionTrack parseResolutionTrack(String value) {
        try {
            return ResolutionTrack.valueOf(value.trim().toUpperCase(java.util.Locale.ROOT));
        } catch (Exception ex) {
            // Default to the safer innovation track if the model returns something unexpected.
            return ResolutionTrack.INNOVATION_REQUIRED;
        }
    }
}
