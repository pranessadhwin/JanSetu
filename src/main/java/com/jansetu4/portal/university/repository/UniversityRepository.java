package com.jansetu4.portal.university.repository;

import com.jansetu4.portal.common.enums.Domain;
import com.jansetu4.portal.university.entity.University;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UniversityRepository extends JpaRepository<University, Long> {

    List<University> findAllByOrderByIdAsc();

    @Query("""
            select distinct u from University u
            join u.disciplines d
            where d = :domain
            order by u.id
            """)
    List<University> findByDomain(@Param("domain") Domain domain);
}
