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
import com.jansetu4.portal.common.enums.ResolutionTrack;
import com.jansetu4.portal.common.exceptions.ResourceNotFoundException;
import com.jansetu4.portal.localbody.service.LocalBodyService;
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
    private final LocalBodyService localBodyService;

    @Transactional
    public Classification classify(Challenge challenge) {
        ClassificationResult result = classificationEngine.classify(challenge);
        Classification classification = saveClassification(challenge, result.getDomain(),
                result.getMethod(), result.getResolutionTrack());
        routeChallenge(challenge, result.getDomain(), result.getResolutionTrack());
        return classification;
    }

    @Transactional
    public ClassificationResponse overrideClassification(Long challengeId, Domain domain, ResolutionTrack resolutionTrack) {
        Challenge challenge = challengeRepository.findDetailedById(challengeId)
                .orElseThrow(() -> new ResourceNotFoundException("Challenge not found"));
        Classification classification = saveClassification(challenge, domain, ClassificationMethod.MANUAL_OVERRIDE, resolutionTrack);
        routeChallenge(challenge, domain, resolutionTrack);
        return toResponse(classification);
    }

    /**
     * Routes a classified challenge to a local body (routine track) or a
     * university (innovation track). If a routine issue has no matching local
     * body, it falls back to the innovation/university track instead. Issues
     * that couldn't be classified (e.g. the AI classifier is unavailable) are
     * left unrouted until they are manually classified.
     */
    private void routeChallenge(Challenge challenge, Domain domain, ResolutionTrack resolutionTrack) {
        if (resolutionTrack == ResolutionTrack.MUNICIPAL_ROUTINE) {
            universityService.clearAssignment(challenge.getId());
            boolean routed = localBodyService.autoRouteChallenge(challenge, domain);
            if (!routed) {
                universityService.autoRouteChallenge(challenge, domain);
            }
        } else if (resolutionTrack == ResolutionTrack.INNOVATION_REQUIRED) {
            localBodyService.clearAssignment(challenge.getId());
            universityService.autoRouteChallenge(challenge, domain);
        } else {
            localBodyService.clearAssignment(challenge.getId());
            universityService.clearAssignment(challenge.getId());
        }
    }

    private Classification saveClassification(Challenge challenge,
                                              Domain domain,
                                              ClassificationMethod method,
                                              ResolutionTrack resolutionTrack) {
        Classification classification = classificationRepository.findByChallengeId(challenge.getId())
                .orElse(Classification.builder().challenge(challenge).build());
        classification.setDomain(domain);
        classification.setClassifiedAt(LocalDateTime.now());
        classification.setMethod(method);
        classification.setResolutionTrack(resolutionTrack);

        Classification savedClassification = classificationRepository.save(classification);
        challenge.setClassification(savedClassification);
        challenge.setStatus(domain == Domain.UNCLASSIFIED ? ChallengeStatus.SUBMITTED : ChallengeStatus.CLASSIFIED);
        challengeRepository.save(challenge);
        return savedClassification;
    }

    private ClassificationResponse toResponse(Classification classification) {
        return ClassificationResponse.builder()
                .id(classification.getId())
                .challengeId(classification.getChallenge().getId())
                .domain(classification.getDomain())
                .classifiedAt(classification.getClassifiedAt())
                .method(classification.getMethod())
                .resolutionTrack(classification.getResolutionTrack())
                .build();
    }
}
