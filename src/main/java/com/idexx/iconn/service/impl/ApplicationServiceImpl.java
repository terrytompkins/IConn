package com.idexx.iconn.service.impl;

import com.idexx.iconn.domain.Application;
import com.idexx.iconn.repository.ApplicationRepository;
import com.idexx.iconn.service.ApplicationService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Application}.
 */
@Service
@Transactional
public class ApplicationServiceImpl implements ApplicationService {

    private final Logger log = LoggerFactory.getLogger(ApplicationServiceImpl.class);

    private final ApplicationRepository applicationRepository;

    public ApplicationServiceImpl(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    @Override
    public Application save(Application application) {
        log.debug("Request to save Application : {}", application);
        return applicationRepository.save(application);
    }

    @Override
    public Application update(Application application) {
        log.debug("Request to update Application : {}", application);
        return applicationRepository.save(application);
    }

    @Override
    public Optional<Application> partialUpdate(Application application) {
        log.debug("Request to partially update Application : {}", application);

        return applicationRepository
            .findById(application.getId())
            .map(existingApplication -> {
                if (application.getName() != null) {
                    existingApplication.setName(application.getName());
                }
                if (application.getDescription() != null) {
                    existingApplication.setDescription(application.getDescription());
                }
                if (application.getWikiUrl() != null) {
                    existingApplication.setWikiUrl(application.getWikiUrl());
                }
                if (application.getLogoImage() != null) {
                    existingApplication.setLogoImage(application.getLogoImage());
                }
                if (application.getLogoImageContentType() != null) {
                    existingApplication.setLogoImageContentType(application.getLogoImageContentType());
                }
                if (application.getCreatedAt() != null) {
                    existingApplication.setCreatedAt(application.getCreatedAt());
                }
                if (application.getModifiedAt() != null) {
                    existingApplication.setModifiedAt(application.getModifiedAt());
                }

                return existingApplication;
            })
            .map(applicationRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Application> findAll(Pageable pageable) {
        log.debug("Request to get all Applications");
        return applicationRepository.findAll(pageable);
    }

    public Page<Application> findAllWithEagerRelationships(Pageable pageable) {
        return applicationRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Application> findOne(Long id) {
        log.debug("Request to get Application : {}", id);
        return applicationRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Application : {}", id);
        applicationRepository.deleteById(id);
    }
}
