package com.gtemate.petiteannoncekmer.web.rest;

/**
 * Created by admin on 10/12/2016.
 */

import com.codahale.metrics.annotation.Timed;
import com.gtemate.petiteannoncekmer.domain.Country;
import com.gtemate.petiteannoncekmer.domain.Region;
import com.gtemate.petiteannoncekmer.service.RegionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;

/**
 * REST controller for managing Region.
 */
@RestController
@RequestMapping("/api")
public class RegionResource {

    private final Logger log = LoggerFactory.getLogger(Region.class);

    @Inject
    private RegionService regionService;

    /**
     * GET  /Region : get all the Region.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of Region in body
     */
    @GetMapping("/region")
    @Timed
    public ResponseEntity<List<Region>> getAllRegion() {
        log.debug("REST request to get all Region");

        return new ResponseEntity<>(
            regionService.findAll(),
            HttpStatus.OK);
    }



    /**
     * GET  /Region : get all the Region for a country.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of Region in body
     */
    @GetMapping("/region/getByCountry/{countryId}")
    @Timed
    public ResponseEntity<List<Region>> getByCountry(@PathVariable Long countryId) {
        log.debug("REST request to get all Region for a countryId");
        return new ResponseEntity<>(
            regionService.getByCountry(countryId),
            HttpStatus.OK);
    }

    /**
     * GET  /country : get a Region.
     *
     * @return the ResponseEntity with status 200 (OK) and the Region in body
     */
    @GetMapping("/region/{id}")
    @Timed
    public ResponseEntity<Region> get(@PathVariable Long id) {
        log.debug("REST request to get all Region");
        return new ResponseEntity<>(
            regionService.findOne(id),
            HttpStatus.OK);
    }

}
