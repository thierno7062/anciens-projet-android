package com.gtemate.petiteannoncekmer.repository;

import com.gtemate.petiteannoncekmer.domain.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by admin on 10/12/2016.
 */
public interface CountryRepository extends JpaRepository<Country,Long>,JpaSpecificationExecutor<Country> {

}
