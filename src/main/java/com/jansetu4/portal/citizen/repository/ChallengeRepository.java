package com.jansetu4.portal.citizen.repository;

import com.jansetu4.portal.citizen.entity.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {

    @Query("""
            select distinct c from Challenge c
            left join fetch c.media
            left join fetch c.classification
            join fetch c.submittedBy
            where c.submittedBy.id = :submittedById
            order by c.createdAt desc
            """)
    List<Challenge> findAllDetailedBySubmittedById(@Param("submittedById") Long submittedById);

    @Query("""
            select distinct c from Challenge c
            left join fetch c.media
            left join fetch c.classification
            join fetch c.submittedBy
            where c.id = :id
            """)
    Optional<Challenge> findDetailedById(@Param("id") Long id);

    @Query("select c.status, count(c) from Challenge c group by c.status order by c.status")
    List<Object[]> countByStatus();

    @Query("""
            select function('date_format', c.createdAt, '%Y-%m'), count(c)
            from Challenge c
            group by function('date_format', c.createdAt, '%Y-%m')
            order by function('date_format', c.createdAt, '%Y-%m')
            """)
    List<Object[]> countSubmissionsByMonth();
}
