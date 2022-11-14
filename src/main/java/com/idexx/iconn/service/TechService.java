package com.idexx.iconn.service;

import com.idexx.iconn.domain.Tech;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Tech}.
 */
public interface TechService {
    /**
     * Save a tech.
     *
     * @param tech the entity to save.
     * @return the persisted entity.
     */
    Tech save(Tech tech);

    /**
     * Updates a tech.
     *
     * @param tech the entity to update.
     * @return the persisted entity.
     */
    Tech update(Tech tech);

    /**
     * Partially updates a tech.
     *
     * @param tech the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Tech> partialUpdate(Tech tech);

    /**
     * Get all the teches.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Tech> findAll(Pageable pageable);

    /**
     * Get all the teches with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Tech> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" tech.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Tech> findOne(Long id);

    /**
     * Delete the "id" tech.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
