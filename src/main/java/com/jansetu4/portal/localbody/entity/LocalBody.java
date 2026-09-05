package com.jansetu4.portal.localbody.entity;

import com.jansetu4.portal.common.BaseEntity;
import com.jansetu4.portal.common.enums.Domain;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Jharkhand government/municipal department that handles routine
 * civic issues directly, without needing university/industry involvement.
 */
@Entity
@Table(name = "local_bodies")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocalBody extends BaseEntity {

    @Column(nullable = false)
    private String name;

    private String jurisdiction;

    @Builder.Default
    @ElementCollection(fetch = FetchType.EAGER, targetClass = Domain.class)
    @CollectionTable(name = "local_body_disciplines", joinColumns = @JoinColumn(name = "local_body_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "discipline", nullable = false)
    private List<Domain> disciplines = new ArrayList<>();

    @Column(name = "contact_email")
    private String contactEmail;

    @Column(name = "contact_phone")
    private String contactPhone;
}
