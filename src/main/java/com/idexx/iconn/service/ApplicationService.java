package com.idexx.iconn.service;

import com.idexx.iconn.domain.Application;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Application}.
 */
public interface ApplicationService {
    /**
     * Save a application.
     *
     * @param application the entity to save.
     * @return the persisted entity.
     */
    Application save(Application application);

    /**
     * Updates a application.
     *
     * @param application the entity to update.
     * @return the persisted entity.
     */
    Application update(Application application);

    /**
     * Partially updates a application.
     *
     * @param application the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Application> partialUpdate(Application application);

    /**
     * Get all the applications.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Application> findAll(Pageable pageable);

    /**
     * Get all the applications with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Application> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" application.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Application> findOne(Long id);

    /**
     * Delete the "id" application.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
