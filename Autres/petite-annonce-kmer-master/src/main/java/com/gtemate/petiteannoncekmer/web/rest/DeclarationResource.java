package com.gtemate.petiteannoncekmer.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gtemate.petiteannoncekmer.domain.Declaration;
import com.gtemate.petiteannoncekmer.domain.Localisation;
import com.gtemate.petiteannoncekmer.service.DeclarationService;
import com.gtemate.petiteannoncekmer.service.MailService;
import com.gtemate.petiteannoncekmer.service.UserService;
import com.gtemate.petiteannoncekmer.web.rest.util.HeaderUtil;
import com.gtemate.petiteannoncekmer.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

/**
 * REST controller for managing Declaration.
 */
@RestController
@RequestMapping("/api")
public class DeclarationResource {

    private final Logger log = LoggerFactory.getLogger(DeclarationResource.class);

    @Inject
    private DeclarationService declarationService;

    @Inject
    private UserService userService;

    @Inject
    private MailService mailService;

    /**
     * POST  /declarations : Create a new declaration.
     *
     * @param declaration the declaration to create
     * @return the ResponseEntity with status 201 (Created) and with body the new declaration, or with status 400 (Bad Request) if the declaration has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/declarations")
    @Timed
    public ResponseEntity<Declaration> createDeclaration(@Valid @RequestBody Declaration declaration) throws URISyntaxException {
        log.debug("REST request to save Declaration : {}", declaration);
        if (declaration.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("declaration", "idexists", "A new declaration cannot already have an ID")).body(null);
        }
        Declaration result = declarationService.save(declaration);
        return ResponseEntity.created(new URI("/api/declarations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("declaration", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /declarations : Updates an existing declaration.
     *
     * @param declaration the declaration to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated declaration,
     * or with status 400 (Bad Request) if the declaration is not valid,
     * or with status 500 (Internal Server Error) if the declaration couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/declarations")
    @Timed
    public ResponseEntity<Declaration> updateDeclaration(@Valid @RequestBody Declaration declaration) throws URISyntaxException {
        log.debug("REST request to update Declaration : {}", declaration);
        if (declaration.getId() == null) {
            return createDeclaration(declaration);
        }
        Declaration result = declarationService.save(declaration);
        if (declaration.getPublished()) {
            mailService.sendDeclarationIsPublishedMail(declaration);
        }
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("declaration", declaration.getId().toString()))
            .body(result);
    }

    /**
     * GET  /declarations : get all the declarations.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of declarations in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/declarations")
    @Timed
    public ResponseEntity<List<Declaration>> getAllDeclarations(Pageable pageable)
        throws URISyntaxException {
        Page<Declaration> page = null;
        if (userService.isCurrentUserAdmin()) {
            page = declarationService.findAll(pageable);
        } else {
            page = declarationService.findByUserIsCurrentUser(pageable);
        }


        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/declarations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


    /**
     * GET  /declarations : get all the declarations by region.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of declarations in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/declarations-byregion")
    @Timed
    public ResponseEntity<List<Declaration>> getAllDeclarationsByRegion(Pageable pageable, @RequestParam("IdRegion") String IdRegion, @RequestParam("search") String search)
        throws URISyntaxException {
        log.debug("REST request to get a page of Declarations {} ", IdRegion);
        Page<Declaration> page = declarationService.getAllDeclarationsByRegion(pageable, IdRegion, search);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/declarations-byregion");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /declarations/:IdRegion : get number of declarations per region "IdRegion" region.
     *
     * @param IdRegion the code of the region
     * @return the ResponseEntity with status 200 (OK) and with body number, or with status 404 (Not Found)
     */
    @GetMapping("/declarations-byregion-count/{IdRegion}")
    @Timed
    public ResponseEntity<?> countAllPerRegion(@PathVariable String IdRegion, HttpServletResponse response) {
        log.debug("REST request to get Declarations count by region : {}", IdRegion);
        Long countDeclarations = declarationService.countAllPerRegion(IdRegion);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", "" + countDeclarations);
        headers.add("X-Region-Code", "" + IdRegion);
        return new ResponseEntity<>(
            "",
            headers,
            HttpStatus.OK);
    }


    /**
     * GET  /declarations/:id : get the "id" declaration.
     *
     * @param id the id of the declaration to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the declaration, or with status 404 (Not Found)
     */
    @GetMapping("/declarations/{id}")
    @Timed
    public ResponseEntity<Declaration> getDeclaration(@PathVariable Long id) {
        log.debug("REST request to get Declaration : {}", id);
        Declaration declaration = declarationService.findOne(id);
        if (declaration != null) {
            ResponseEntity<Declaration> responseEntity = new ResponseEntity<>(declaration, HttpStatus.OK);
            if (declaration.getPublished()) {
                return responseEntity;
            } else {
                if (userService.isCurrentUserAdmin()) {
                    return responseEntity;
                }
            }
        }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
        /**
         * DELETE  /declarations/:id : delete the "id" declaration.
         *
         * @param id the id of the declaration to delete
         * @return the ResponseEntity with status 200 (OK)
         */
        @DeleteMapping("/declarations/{id}")
        @Timed
        public ResponseEntity<Void> deleteDeclaration (@PathVariable Long id){
            log.debug("REST request to delete Declaration : {}", id);
            declarationService.delete(id);
            return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("declaration", id.toString())).build();
        }


        /**
         * REST methods to save declarations-user with uploaded files.
         * @param declaration
         * @param localisation
         * @param images
         * @return
         */
        @PostMapping(value = "/save-declarations-user", consumes = "multipart/form-data", produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<Declaration> saveDeclarationUser (@RequestPart("declaration") Declaration declaration,
            @RequestPart(name = "localisation", required = false) Localisation localisation,
            @RequestPart(name = "login", required = false) String login,
            @RequestPart(name = "images", required = false) MultipartFile[]images
    ) throws URISyntaxException {

            // call service to save Admission Request
            Declaration result = declarationService.saveDeclarationUser(declaration, localisation, login, images);

            // return JSON response
            return ResponseEntity.created(new URI("/api/entities/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("entity", result.getId().toString())).body(result);
        }


    }
