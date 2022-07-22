package com.gtemate.petiteannoncekmer.web.rest;

/**
 * Created by admin on 10/12/2016.
 */

import com.codahale.metrics.annotation.Timed;
import com.gtemate.petiteannoncekmer.domain.Country;
import com.gtemate.petiteannoncekmer.service.CountryService;
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
 * REST controller for managing country.
 */
@SuppressWarnings("CdiInjectionPointsInspection")
@RestController
@RequestMapping("/api")
public class CountryResource {

    private final Logger log = LoggerFactory.getLogger(Country.class);

    @Inject
    private CountryService countryService;

    /**
     * GET  /country : get all the country.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of country in body
     */
    @GetMapping("/country")
    @Timed
    public ResponseEntity<List<Country>> getAllCountry() {
        log.debug("REST request to get all Country");

        return new ResponseEntity<>(
            countryService.findAll(),
            HttpStatus.OK);
    }

    /**
     * GET  /country : get a country.
     *
     * @return the ResponseEntity with status 200 (OK) and the country in body
     */
    @GetMapping("/country/{id}")
    @Timed
    public ResponseEntity<Country> get(@PathVariable Long id) {
        log.debug("REST request to get all Country");
        return new ResponseEntity<>(
            countryService.findOne(id),
            HttpStatus.OK);
    }
}
