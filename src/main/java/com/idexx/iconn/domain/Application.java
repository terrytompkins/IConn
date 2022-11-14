package com.idexx.iconn.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Application.
 */
@Entity
@Table(name = "application")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Application implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Size(max = 4000)
    @Column(name = "description", length = 4000)
    private String description;

    @Column(name = "wiki_url")
    private String wikiUrl;

    @Lob
    @Column(name = "logo_image")
    private byte[] logoImage;

    @Column(name = "logo_image_content_type")
    private String logoImageContentType;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @NotNull
    @Column(name = "modified_at", nullable = false)
    private Instant modifiedAt;

    @ManyToOne(optional = false)
    @NotNull
    private User user;

    @ManyToMany
    @JoinTable(
        name = "rel_application__tech",
        joinColumns = @JoinColumn(name = "application_id"),
        inverseJoinColumns = @JoinColumn(name = "tech_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "applications" }, allowSetters = true)
    private Set<Tech> teches = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Application id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Application name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Application description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWikiUrl() {
        return this.wikiUrl;
    }

    public Application wikiUrl(String wikiUrl) {
        this.setWikiUrl(wikiUrl);
        return this;
    }

    public void setWikiUrl(String wikiUrl) {
        this.wikiUrl = wikiUrl;
    }

    public byte[] getLogoImage() {
        return this.logoImage;
    }

    public Application logoImage(byte[] logoImage) {
        this.setLogoImage(logoImage);
        return this;
    }

    public void setLogoImage(byte[] logoImage) {
        this.logoImage = logoImage;
    }

    public String getLogoImageContentType() {
        return this.logoImageContentType;
    }

    public Application logoImageContentType(String logoImageContentType) {
        this.logoImageContentType = logoImageContentType;
        return this;
    }

    public void setLogoImageContentType(String logoImageContentType) {
        this.logoImageContentType = logoImageContentType;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Application createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getModifiedAt() {
        return this.modifiedAt;
    }

    public Application modifiedAt(Instant modifiedAt) {
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

    public Application user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<Tech> getTeches() {
        return this.teches;
    }

    public void setTeches(Set<Tech> teches) {
        this.teches = teches;
    }

    public Application teches(Set<Tech> teches) {
        this.setTeches(teches);
        return this;
    }

    public Application addTech(Tech tech) {
        this.teches.add(tech);
        tech.getApplications().add(this);
        return this;
    }

    public Application removeTech(Tech tech) {
        this.teches.remove(tech);
        tech.getApplications().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Application)) {
            return false;
        }
        return id != null && id.equals(((Application) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Application{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", wikiUrl='" + getWikiUrl() + "'" +
            ", logoImage='" + getLogoImage() + "'" +
            ", logoImageContentType='" + getLogoImageContentType() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", modifiedAt='" + getModifiedAt() + "'" +
            "}";
    }
}
