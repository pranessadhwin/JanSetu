package com.jansetu4.portal.university.repository;

import com.jansetu4.portal.common.enums.Domain;
import com.jansetu4.portal.university.entity.UniversityAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UniversityAssignmentRepository extends JpaRepository<UniversityAssignment, Long> {

    @Query("""
            select ua from UniversityAssignment ua
            join fetch ua.challenge c
            join fetch ua.university u
            left join fetch c.classification
            where u.id = :universityId
            order by ua.assignedAt desc
            """)
    List<UniversityAssignment> findDashboardAssignments(@Param("universityId") Long universityId);

    @Query("""
            select count(ua) from UniversityAssignment ua
            join ua.challenge c
            join c.classification cl
            where cl.domain = :domain
            """)
    long countByDomain(@Param("domain") Domain domain);

    @Query("""
            select u.name, count(ua)
            from UniversityAssignment ua
            join ua.university u
            group by u.name
            order by u.name
            """)
    List<Object[]> countAssignmentsByUniversity();

    @Query("""
            select ua from UniversityAssignment ua
            join fetch ua.challenge c
            join fetch ua.university u
            join fetch c.classification cl
            where ua.status <> com.jansetu4.portal.common.enums.AssignmentStatus.ACCEPTED
            and cl.domain in :domains
            order by ua.assignedAt desc
            """)
    List<UniversityAssignment> findClaimableForDomains(@Param("domains") List<Domain> domains);

    @Query("""
            select ua from UniversityAssignment ua
            join fetch ua.challenge c
            join fetch ua.university u
            left join fetch c.classification
            where ua.solutionProposedAt is not null
            order by ua.solutionProposedAt desc
            """)
    List<UniversityAssignment> findWithProposedSolutions();

    @Modifying
    void deleteByChallengeId(Long challengeId);
}
