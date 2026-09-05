package com.jansetu4.portal.classification.service;

import com.jansetu4.portal.citizen.entity.Challenge;
import com.jansetu4.portal.citizen.repository.ChallengeRepository;
import com.jansetu4.portal.classification.dto.ClassificationResponse;
import com.jansetu4.portal.classification.engine.ClassificationEngine;
import com.jansetu4.portal.classification.engine.ClassificationResult;
import com.jansetu4.portal.classification.entity.Classification;
import com.jansetu4.portal.classification.repository.ClassificationRepository;
import com.jansetu4.portal.common.enums.ChallengeStatus;
import com.jansetu4.portal.common.enums.ClassificationMethod;
import com.jansetu4.portal.common.enums.Domain;
import com.jansetu4.portal.common.exceptions.ResourceNotFoundException;
import com.jansetu4.portal.university.service.UniversityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ClassificationService {

    private final ClassificationRepository classificationRepository;
    private final ChallengeRepository challengeRepository;
    private final ClassificationEngine classificationEngine;
    private final UniversityService universityService;

    @Transactional
    public Classification classify(Challenge challenge) {
        ClassificationResult result = classificationEngine.classify(challenge);
        Classification classification = saveClassification(challenge, result.getDomain(), result.getConfidenceScore(), result.getMethod());
        universityService.autoRouteChallenge(challenge, result.getDomain());
        return classification;
    }

    @Transactional
    public ClassificationResponse overrideClassification(Long challengeId, Domain domain) {
        Challenge challenge = challengeRepository.findDetailedById(challengeId)
                .orElseThrow(() -> new ResourceNotFoundException("Challenge not found"));
        Classification classification = saveClassification(challenge, domain, 1.0d, ClassificationMethod.MANUAL_OVERRIDE);
        universityService.autoRouteChallenge(challenge, domain);
        return toResponse(classification);
    }

    private Classification saveClassification(Challenge challenge,
                                              Domain domain,
                                              Double confidenceScore,
                                              ClassificationMethod method) {
        Classification classification = classificationRepository.findByChallengeId(challenge.getId())
                .orElse(Classification.builder().challenge(challenge).build());
        classification.setDomain(domain);
        classification.setConfidenceScore(confidenceScore);
        classification.setClassifiedAt(LocalDateTime.now());
        classification.setMethod(method);

        Classification savedClassification = classificationRepository.save(classification);
        challenge.setClassification(savedClassification);
        challenge.setStatus(ChallengeStatus.CLASSIFIED);
        challengeRepository.save(challenge);
        return savedClassification;
    }

    private ClassificationResponse toResponse(Classification classification) {
        return ClassificationResponse.builder()
                .id(classification.getId())
                .challengeId(classification.getChallenge().getId())
                .domain(classification.getDomain())
                .confidenceScore(classification.getConfidenceScore())
                .classifiedAt(classification.getClassifiedAt())
                .method(classification.getMethod())
                .build();
    }
}
