package com.gtemate.petiteannoncekmer.service;

import com.gtemate.petiteannoncekmer.domain.Localisation;
import com.gtemate.petiteannoncekmer.repository.LocalisationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

@Service
@Transactional
public class LocalisationService extends BaseEntityService<Localisation>{

    private final Logger log = LoggerFactory.getLogger(LocalisationService.class);

    @Inject
    LocalisationRepository  localisationRepository;

    @Override
    public LocalisationRepository getRepository() {
        return localisationRepository;
    }


    @Override
    public JpaSpecificationExecutor<Localisation> getPagineableRepository() {
        return  getRepository();
    }

    /**
     *  Get all the Localisation.
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Localisation> findByUserIsCurrentUser() {
        log.debug("Request to get all Localisation");
        List<Localisation>  result = localisationRepository.findByUserIsCurrentUser();
        return result;
    }
}
