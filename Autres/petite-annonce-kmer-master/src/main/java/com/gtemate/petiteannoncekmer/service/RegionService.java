package com.gtemate.petiteannoncekmer.service;

import com.gtemate.petiteannoncekmer.domain.Region;
import com.gtemate.petiteannoncekmer.repository.RegionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by admin on 10/12/2016.
 */
@Service
@Transactional
public class RegionService extends BaseEntityService<Region> {

    private final Logger log = LoggerFactory.getLogger(RegionService.class);

    @Inject
    private RegionRepository regionRepository;

    @Override
    public RegionRepository getRepository() {
        return regionRepository;
    }

    @Override
    public JpaSpecificationExecutor<Region> getPagineableRepository() {
        return  getRepository();
    }

    public List<Region> getByCountry(Long countryId){
       return regionRepository.findByCountry(countryId);
    }
}
