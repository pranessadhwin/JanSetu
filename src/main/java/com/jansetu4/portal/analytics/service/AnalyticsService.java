package com.jansetu4.portal.analytics.service;

import com.jansetu4.portal.analytics.dto.ChartDataResponse;
import com.jansetu4.portal.analytics.dto.SummaryResponse;
import com.jansetu4.portal.citizen.repository.ChallengeRepository;
import com.jansetu4.portal.classification.repository.ClassificationRepository;
import com.jansetu4.portal.common.exceptions.BadRequestException;
import com.jansetu4.portal.university.repository.UniversityAssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final ChallengeRepository challengeRepository;
    private final ClassificationRepository classificationRepository;
    private final UniversityAssignmentRepository universityAssignmentRepository;

    @Transactional(readOnly = true)
    public SummaryResponse getSummary() {
        List<Object[]> rows = challengeRepository.countByStatus();
        return SummaryResponse.builder()
                .totalChallenges(challengeRepository.count())
                .labels(rows.stream().map(row -> String.valueOf(row[0])).toList())
                .values(rows.stream().map(row -> ((Number) row[1]).longValue()).toList())
                .build();
    }

    @Transactional(readOnly = true)
    public ChartDataResponse getByDomain() {
        return toChartData(classificationRepository.countByDomain());
    }

    @Transactional(readOnly = true)
    public ChartDataResponse getByUniversity() {
        return toChartData(universityAssignmentRepository.countAssignmentsByUniversity());
    }

    @Transactional(readOnly = true)
    public ChartDataResponse getTrend(String period) {
        if (period != null && !"monthly".equalsIgnoreCase(period)) {
            throw new BadRequestException("Only monthly trend analytics are supported");
        }
        return toChartData(challengeRepository.countSubmissionsByMonth());
    }

    private ChartDataResponse toChartData(List<Object[]> rows) {
        return ChartDataResponse.builder()
                .labels(rows.stream().map(row -> String.valueOf(row[0])).toList())
                .values(rows.stream().map(row -> ((Number) row[1]).longValue()).toList())
                .build();
    }
}
