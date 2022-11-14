package com.idexx.iconn.web.rest;

import com.idexx.iconn.domain.Tech;
import com.idexx.iconn.repository.TechRepository;
import com.idexx.iconn.service.TechService;
import com.idexx.iconn.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.idexx.iconn.domain.Tech}.
 */
@RestController
@RequestMapping("/api")
public class TechResource {

    private final Logger log = LoggerFactory.getLogger(TechResource.class);

    private static final String ENTITY_NAME = "tech";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TechService techService;

    private final TechRepository techRepository;

    public TechResource(TechService techService, TechRepository techRepository) {
        this.techService = techService;
        this.techRepository = techRepository;
    }

    /**
     * {@code POST  /teches} : Create a new tech.
     *
     * @param tech the tech to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tech, or with status {@code 400 (Bad Request)} if the tech has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/teches")
    public ResponseEntity<Tech> createTech(@Valid @RequestBody Tech tech) throws URISyntaxException {
        log.debug("REST request to save Tech : {}", tech);
        if (tech.getId() != null) {
            throw new BadRequestAlertException("A new tech cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Tech result = techService.save(tech);
        return ResponseEntity
            .created(new URI("/api/teches/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /teches/:id} : Updates an existing tech.
     *
     * @param id the id of the tech to save.
     * @param tech the tech to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tech,
     * or with status {@code 400 (Bad Request)} if the tech is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tech couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/teches/{id}")
    public ResponseEntity<Tech> updateTech(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Tech tech)
        throws URISyntaxException {
        log.debug("REST request to update Tech : {}, {}", id, tech);
        if (tech.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tech.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!techRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Tech result = techService.update(tech);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tech.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /teches/:id} : Partial updates given fields of an existing tech, field will ignore if it is null
     *
     * @param id the id of the tech to save.
     * @param tech the tech to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tech,
     * or with status {@code 400 (Bad Request)} if the tech is not valid,
     * or with status {@code 404 (Not Found)} if the tech is not found,
     * or with status {@code 500 (Internal Server Error)} if the tech couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/teches/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Tech> partialUpdateTech(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Tech tech
    ) throws URISyntaxException {
        log.debug("REST request to partial update Tech partially : {}, {}", id, tech);
        if (tech.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tech.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!techRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Tech> result = techService.partialUpdate(tech);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tech.getId().toString())
        );
    }

    /**
     * {@code GET  /teches} : get all the teches.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of teches in body.
     */
    @GetMapping("/teches")
    public ResponseEntity<List<Tech>> getAllTeches(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of Teches");
        Page<Tech> page;
        if (eagerload) {
            page = techService.findAllWithEagerRelationships(pageable);
        } else {
            page = techService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /teches/:id} : get the "id" tech.
     *
     * @param id the id of the tech to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tech, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/teches/{id}")
    public ResponseEntity<Tech> getTech(@PathVariable Long id) {
        log.debug("REST request to get Tech : {}", id);
        Optional<Tech> tech = techService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tech);
    }

    /**
     * {@code DELETE  /teches/:id} : delete the "id" tech.
     *
     * @param id the id of the tech to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/teches/{id}")
    public ResponseEntity<Void> deleteTech(@PathVariable Long id) {
        log.debug("REST request to delete Tech : {}", id);
        techService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
