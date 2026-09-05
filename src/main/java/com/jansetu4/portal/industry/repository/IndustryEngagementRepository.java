package com.jansetu4.portal.industry.repository;

import com.jansetu4.portal.industry.entity.IndustryEngagement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IndustryEngagementRepository extends JpaRepository<IndustryEngagement, Long> {

    @Query("""
            select ie from IndustryEngagement ie
            join fetch ie.assignment a
            join fetch a.challenge c
            join fetch a.university u
            join fetch ie.industry i
            where i.id = :industryId
            order by ie.createdAt desc
            """)
    List<IndustryEngagement> findDashboardEngagements(@Param("industryId") Long industryId);
}
