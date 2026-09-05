package com.jansetu4.portal.university.entity;

import com.jansetu4.portal.citizen.entity.Challenge;
import com.jansetu4.portal.common.BaseEntity;
import com.jansetu4.portal.common.enums.AssignmentStatus;
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

import java.time.LocalDateTime;

@Entity
@Table(name = "university_assignments")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UniversityAssignment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge challenge;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "university_id", nullable = false)
    private University university;

    @Column(name = "assigned_at", nullable = false)
    private LocalDateTime assignedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssignmentStatus status;

    @Column(name = "faculty_mentor")
    private String facultyMentor;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "solution_title")
    private String solutionTitle;

    @Column(name = "solution_description", columnDefinition = "TEXT")
    private String solutionDescription;

    @Column(name = "team_members", columnDefinition = "TEXT")
    private String teamMembers;

    @Column(name = "solution_proposed_at")
    private LocalDateTime solutionProposedAt;
}
