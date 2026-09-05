package com.jansetu4.portal.classification.entity;

import com.jansetu4.portal.citizen.entity.Challenge;
import com.jansetu4.portal.common.BaseEntity;
import com.jansetu4.portal.common.enums.ClassificationMethod;
import com.jansetu4.portal.common.enums.Domain;
import com.jansetu4.portal.common.enums.ResolutionTrack;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "classifications")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Classification extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "challenge_id", nullable = false, unique = true)
    private Challenge challenge;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Domain domain;

    @Column(name = "classified_at", nullable = false)
    private LocalDateTime classifiedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClassificationMethod method;

    /**
     * Whether this challenge is a routine issue for a local government body
     * or requires an innovative solution from a university partner. Nullable
     * to remain backward compatible with classifications created before this
     * field existed.
     */
    @Enumerated(EnumType.STRING)
    private ResolutionTrack resolutionTrack;
}
