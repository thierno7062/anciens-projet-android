package com.gtemate.petiteannoncekmer.repository;

import com.gtemate.petiteannoncekmer.domain.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by admin on 10/12/2016.
 */
public interface RegionRepository extends JpaRepository<Region,Long>,JpaSpecificationExecutor<Region> {

    @Query("from Region as reg "
        + "where reg.country.id = :countryId "
    )
    List<Region> findByCountry(@Param("countryId") Long countryId);
}
