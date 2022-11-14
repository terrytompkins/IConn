package com.idexx.iconn.service.impl;

import com.idexx.iconn.domain.Tech;
import com.idexx.iconn.repository.TechRepository;
import com.idexx.iconn.service.TechService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Tech}.
 */
@Service
@Transactional
public class TechServiceImpl implements TechService {

    private final Logger log = LoggerFactory.getLogger(TechServiceImpl.class);

    private final TechRepository techRepository;

    public TechServiceImpl(TechRepository techRepository) {
        this.techRepository = techRepository;
    }

    @Override
    public Tech save(Tech tech) {
        log.debug("Request to save Tech : {}", tech);
        return techRepository.save(tech);
    }

    @Override
    public Tech update(Tech tech) {
        log.debug("Request to update Tech : {}", tech);
        return techRepository.save(tech);
    }

    @Override
    public Optional<Tech> partialUpdate(Tech tech) {
        log.debug("Request to partially update Tech : {}", tech);

        return techRepository
            .findById(tech.getId())
            .map(existingTech -> {
                if (tech.getName() != null) {
                    existingTech.setName(tech.getName());
                }
                if (tech.getTechType() != null) {
                    existingTech.setTechType(tech.getTechType());
                }
                if (tech.getDescription() != null) {
                    existingTech.setDescription(tech.getDescription());
                }
                if (tech.getCreatedAt() != null) {
                    existingTech.setCreatedAt(tech.getCreatedAt());
                }
                if (tech.getModifiedAt() != null) {
                    existingTech.setModifiedAt(tech.getModifiedAt());
                }

                return existingTech;
            })
            .map(techRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Tech> findAll(Pageable pageable) {
        log.debug("Request to get all Teches");
        return techRepository.findAll(pageable);
    }

    public Page<Tech> findAllWithEagerRelationships(Pageable pageable) {
        return techRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Tech> findOne(Long id) {
        log.debug("Request to get Tech : {}", id);
        return techRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Tech : {}", id);
        techRepository.deleteById(id);
    }
}
