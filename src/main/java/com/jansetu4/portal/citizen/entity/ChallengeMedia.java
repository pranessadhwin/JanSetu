package com.jansetu4.portal.citizen.entity;

import com.jansetu4.portal.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "challenge_media")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeMedia extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge challenge;

    @Column(name = "file_url", nullable = false)
    private String fileUrl;

    @Column(name = "file_type")
    private String fileType;
}
