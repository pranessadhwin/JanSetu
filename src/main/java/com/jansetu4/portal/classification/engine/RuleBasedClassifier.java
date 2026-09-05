package com.jansetu4.portal.classification.engine;

import com.jansetu4.portal.citizen.entity.Challenge;
import com.jansetu4.portal.common.enums.ClassificationMethod;
import com.jansetu4.portal.common.enums.Domain;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Component
public class RuleBasedClassifier implements ClassificationEngine {

    private final Map<Domain, List<String>> keywords = new EnumMap<>(Domain.class);

    public RuleBasedClassifier() {
        keywords.put(Domain.EDUCATION, List.of("school", "teacher", "classroom", "student", "learning"));
        keywords.put(Domain.HEALTHCARE, List.of("hospital", "clinic", "doctor", "medicine", "health"));
        keywords.put(Domain.AGRICULTURE, List.of("farmer", "crop", "irrigation", "soil", "seed"));
        keywords.put(Domain.WATER, List.of("water", "drinking", "pipeline", "borewell", "tank"));
        keywords.put(Domain.SANITATION, List.of("toilet", "drain", "sanitation", "waste", "sewage"));
        keywords.put(Domain.ENVIRONMENT, List.of("pollution", "tree", "forest", "climate", "waste management"));
        keywords.put(Domain.RURAL_LIVELIHOOD, List.of("employment", "livelihood", "self help", "artisan", "income"));
        keywords.put(Domain.ACCESSIBILITY, List.of("disabled", "accessible", "ramp", "wheelchair", "barrier"));
        keywords.put(Domain.URBAN_INFRASTRUCTURE, List.of("road", "streetlight", "bridge", "traffic", "drainage"));
        keywords.put(Domain.PUBLIC_SERVICE, List.of("certificate", "service", "office", "document", "application"));
    }

    @Override
    public ClassificationResult classify(Challenge challenge) {
        String text = (challenge.getTitle() + " " + challenge.getDescription()).toLowerCase(Locale.ROOT);
        Domain bestDomain = Domain.PUBLIC_SERVICE;
        int bestHits = 0;
        double bestConfidence = 0.1d;

        for (Map.Entry<Domain, List<String>> entry : keywords.entrySet()) {
            int hits = (int) entry.getValue().stream()
                    .filter(text::contains)
                    .count();
            if (hits > bestHits) {
                bestHits = hits;
                bestDomain = entry.getKey();
                bestConfidence = Math.min(1.0d, (double) hits / entry.getValue().size());
            }
        }

        return ClassificationResult.builder()
                .domain(bestDomain)
                .confidenceScore(bestHits == 0 ? 0.1d : bestConfidence)
                .method(ClassificationMethod.RULE_BASED)
                .build();
    }
}
