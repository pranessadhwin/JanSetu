package com.jansetu4.portal.industry.entity;

import com.jansetu4.portal.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "industries")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Industry extends BaseEntity {

    @Column(nullable = false)
    private String name;

    private String sector;

    @Column(name = "contact_email")
    private String contactEmail;

    @Column(name = "contact_phone")
    private String contactPhone;
}
