package com.idexx.iconn.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.idexx.iconn.domain.enumeration.TechType;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Tech.
 */
@Entity
@Table(name = "tech")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Tech implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "tech_type")
    private TechType techType;

    @Size(max = 2000)
    @Column(name = "description", length = 2000)
    private String description;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @NotNull
    @Column(name = "modified_at", nullable = false)
    private Instant modifiedAt;

    @ManyToOne(optional = false)
    @NotNull
    private User user;

    @ManyToMany(mappedBy = "teches")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "teches" }, allowSetters = true)
    private Set<Application> applications = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Tech id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Tech name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TechType getTechType() {
        return this.techType;
    }

    public Tech techType(TechType techType) {
        this.setTechType(techType);
        return this;
    }

    public void setTechType(TechType techType) {
        this.techType = techType;
    }

    public String getDescription() {
        return this.description;
    }

    public Tech description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Tech createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getModifiedAt() {
        return this.modifiedAt;
    }

    public Tech modifiedAt(Instant modifiedAt) {
        this.setModifiedAt(modifiedAt);
        return this;
    }

    public void setModifiedAt(Instant modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Tech user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<Application> getApplications() {
        return this.applications;
    }

    public void setApplications(Set<Application> applications) {
        if (this.applications != null) {
            this.applications.forEach(i -> i.removeTech(this));
        }
        if (applications != null) {
            applications.forEach(i -> i.addTech(this));
        }
        this.applications = applications;
    }

    public Tech applications(Set<Application> applications) {
        this.setApplications(applications);
        return this;
    }

    public Tech addApplication(Application application) {
        this.applications.add(application);
        application.getTeches().add(this);
        return this;
    }

    public Tech removeApplication(Application application) {
        this.applications.remove(application);
        application.getTeches().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tech)) {
            return false;
        }
        return id != null && id.equals(((Tech) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Tech{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", techType='" + getTechType() + "'" +
            ", description='" + getDescription() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", modifiedAt='" + getModifiedAt() + "'" +
            "}";
    }
}
