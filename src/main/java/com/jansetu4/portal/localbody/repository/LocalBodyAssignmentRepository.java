package com.jansetu4.portal.localbody.repository;

import com.jansetu4.portal.common.enums.Domain;
import com.jansetu4.portal.localbody.entity.LocalBodyAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LocalBodyAssignmentRepository extends JpaRepository<LocalBodyAssignment, Long> {

    @Query("""
            select lba from LocalBodyAssignment lba
            join fetch lba.challenge c
            join fetch lba.localBody lb
            left join fetch c.classification
            where lb.id = :localBodyId
            order by lba.assignedAt desc
            """)
    List<LocalBodyAssignment> findDashboardAssignments(@Param("localBodyId") Long localBodyId);

    @Query("""
            select count(lba) from LocalBodyAssignment lba
            join lba.challenge c
            join c.classification cl
            where cl.domain = :domain
            """)
    long countByDomain(@Param("domain") Domain domain);

    Optional<LocalBodyAssignment> findByChallengeId(Long challengeId);

    @Modifying
    void deleteByChallengeId(Long challengeId);
}
