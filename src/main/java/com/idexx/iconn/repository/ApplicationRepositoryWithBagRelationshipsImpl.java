package com.idexx.iconn.repository;

import com.idexx.iconn.domain.Application;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class ApplicationRepositoryWithBagRelationshipsImpl implements ApplicationRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Application> fetchBagRelationships(Optional<Application> application) {
        return application.map(this::fetchTeches);
    }

    @Override
    public Page<Application> fetchBagRelationships(Page<Application> applications) {
        return new PageImpl<>(
            fetchBagRelationships(applications.getContent()),
            applications.getPageable(),
            applications.getTotalElements()
        );
    }

    @Override
    public List<Application> fetchBagRelationships(List<Application> applications) {
        return Optional.of(applications).map(this::fetchTeches).orElse(Collections.emptyList());
    }

    Application fetchTeches(Application result) {
        return entityManager
            .createQuery(
                "select application from Application application left join fetch application.teches where application is :application",
                Application.class
            )
            .setParameter("application", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Application> fetchTeches(List<Application> applications) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, applications.size()).forEach(index -> order.put(applications.get(index).getId(), index));
        List<Application> result = entityManager
            .createQuery(
                "select distinct application from Application application left join fetch application.teches where application in :applications",
                Application.class
            )
            .setParameter("applications", applications)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
