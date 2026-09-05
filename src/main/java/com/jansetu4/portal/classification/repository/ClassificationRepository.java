package com.jansetu4.portal.classification.repository;

import com.jansetu4.portal.classification.entity.Classification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ClassificationRepository extends JpaRepository<Classification, Long> {

    Optional<Classification> findByChallengeId(Long challengeId);

    @Query("select c.domain, count(c) from Classification c group by c.domain order by c.domain")
    List<Object[]> countByDomain();
}
