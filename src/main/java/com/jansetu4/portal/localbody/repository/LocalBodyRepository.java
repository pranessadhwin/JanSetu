package com.jansetu4.portal.localbody.repository;

import com.jansetu4.portal.common.enums.Domain;
import com.jansetu4.portal.localbody.entity.LocalBody;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LocalBodyRepository extends JpaRepository<LocalBody, Long> {

    List<LocalBody> findAllByOrderByIdAsc();

    @Query("""
            select distinct lb from LocalBody lb
            join lb.disciplines d
            where d = :domain
            order by lb.id
            """)
    List<LocalBody> findByDomain(@Param("domain") Domain domain);
}
