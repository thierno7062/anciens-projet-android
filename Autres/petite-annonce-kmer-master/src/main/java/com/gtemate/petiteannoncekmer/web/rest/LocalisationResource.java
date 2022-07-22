package com.gtemate.petiteannoncekmer.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gtemate.petiteannoncekmer.domain.Localisation;
import com.gtemate.petiteannoncekmer.service.LocalisationService;
import com.gtemate.petiteannoncekmer.service.UserService;
import com.gtemate.petiteannoncekmer.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Localisation.
 */
@RestController
@RequestMapping("/api")
public class LocalisationResource {

    private final Logger log = LoggerFactory.getLogger(LocalisationResource.class);

    @Inject
    private LocalisationService localisationService;

    @Inject
    private UserService userService;

    /**
     * POST  /localisations : Create a new localisation.
     *
     * @param localisation the localisation to create
     * @return the ResponseEntity with status 201 (Created) and with body the new localisation, or with status 400 (Bad Request) if the localisation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/localisations")
    @Timed
    public ResponseEntity<Localisation> createLocalisation(@Valid @RequestBody Localisation localisation) throws URISyntaxException {
        log.debug("REST request to save Localisation : {}", localisation);
        if (localisation.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("localisation", "idexists", "A new localisation cannot already have an ID")).body(null);
        }
        Localisation result = localisationService.save(localisation);
        return ResponseEntity.created(new URI("/api/localisations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("localisation", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /localisations : Updates an existing localisation.
     *
     * @param localisation the localisation to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated localisation,
     * or with status 400 (Bad Request) if the localisation is not valid,
     * or with status 500 (Internal Server Error) if the localisation couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/localisations")
    @Timed
    public ResponseEntity<Localisation> updateLocalisation(@Valid @RequestBody Localisation localisation) throws URISyntaxException {
        log.debug("REST request to update Localisation : {}", localisation);
        if (localisation.getId() == null) {
            return createLocalisation(localisation);
        }
        Localisation result = localisationService.save(localisation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("localisation", localisation.getId().toString()))
            .body(result);
    }

    /**
     * GET  /localisations : get all the localisations.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of localisations in body
     */
    @GetMapping("/localisations")
    @Timed
    public List<Localisation> getAllLocalisations() {
        log.debug("REST request to get all Localisations");
        List<Localisation> localisations = null;
        if(userService.isCurrentUserAdmin()){
            localisations = localisationService.findAll();
        }else{
            localisations = localisationService.findByUserIsCurrentUser();
        }
        return localisations;
    }

    /**
     * GET  /localisations/:id : get the "id" localisation.
     *
     * @param id the id of the localisation to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the localisation, or with status 404 (Not Found)
     */
    @GetMapping("/localisations/{id}")
    @Timed
    public ResponseEntity<Localisation> getLocalisation(@PathVariable Long id) {
        log.debug("REST request to get Localisation : {}", id);
        Localisation localisation = localisationService.findOne(id);
        return Optional.ofNullable(localisation)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /localisations/:id : delete the "id" localisation.
     *
     * @param id the id of the localisation to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/localisations/{id}")
    @Timed
    public ResponseEntity<Void> deleteLocalisation(@PathVariable Long id) {
        log.debug("REST request to delete Localisation : {}", id);
        localisationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("localisation", id.toString())).build();
    }

}
