package com.idexx.iconn.repository;

import com.idexx.iconn.domain.Tech;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Tech entity.
 */
@Repository
public interface TechRepository extends JpaRepository<Tech, Long> {
    @Query("select tech from Tech tech where tech.user.login = ?#{principal.username}")
    List<Tech> findByUserIsCurrentUser();

    default Optional<Tech> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Tech> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Tech> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct tech from Tech tech left join fetch tech.user",
        countQuery = "select count(distinct tech) from Tech tech"
    )
    Page<Tech> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct tech from Tech tech left join fetch tech.user")
    List<Tech> findAllWithToOneRelationships();

    @Query("select tech from Tech tech left join fetch tech.user where tech.id =:id")
    Optional<Tech> findOneWithToOneRelationships(@Param("id") Long id);
}
