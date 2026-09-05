package com.jansetu4.portal.industry.entity;

import com.jansetu4.portal.common.BaseEntity;
import com.jansetu4.portal.common.enums.EngagementType;
import com.jansetu4.portal.university.entity.UniversityAssignment;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Records an industry/startup/CSR organization's interest in supporting a
 * university-proposed solution for a citizen-reported challenge.
 */
@Entity
@Table(name = "industry_engagements")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IndustryEngagement extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "assignment_id", nullable = false)
    private UniversityAssignment assignment;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "industry_id", nullable = false)
    private Industry industry;

    @Enumerated(EnumType.STRING)
    @Column(name = "engagement_type", nullable = false)
    private EngagementType engagementType;

    @Column(columnDefinition = "TEXT")
    private String notes;
}
