package com.gtemate.petiteannoncekmer.service;

import com.gtemate.petiteannoncekmer.domain.Country;
import com.gtemate.petiteannoncekmer.repository.CountryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * Created by admin on 10/12/2016.
 */
@SuppressWarnings("CdiInjectionPointsInspection")
@Service
@Transactional
public class CountryService extends BaseEntityService<Country> {

    private final Logger log = LoggerFactory.getLogger(CountryService.class);

    @Inject
    private CountryRepository countryRepository;

    @Override
    public CountryRepository getRepository() {
        return countryRepository;
    }

    @Override
    public JpaSpecificationExecutor<Country> getPagineableRepository() {
        return  getRepository();
    }
}
