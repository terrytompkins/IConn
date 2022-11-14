package com.idexx.iconn.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.idexx.iconn.IntegrationTest;
import com.idexx.iconn.domain.Tech;
import com.idexx.iconn.domain.User;
import com.idexx.iconn.domain.enumeration.TechType;
import com.idexx.iconn.repository.TechRepository;
import com.idexx.iconn.service.TechService;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TechResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TechResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final TechType DEFAULT_TECH_TYPE = TechType.PROGRAMMING_LANGUAGE;
    private static final TechType UPDATED_TECH_TYPE = TechType.SERVER;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_MODIFIED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/teches";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TechRepository techRepository;

    @Mock
    private TechRepository techRepositoryMock;

    @Mock
    private TechService techServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTechMockMvc;

    private Tech tech;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tech createEntity(EntityManager em) {
        Tech tech = new Tech()
            .name(DEFAULT_NAME)
            .techType(DEFAULT_TECH_TYPE)
            .description(DEFAULT_DESCRIPTION)
            .createdAt(DEFAULT_CREATED_AT)
            .modifiedAt(DEFAULT_MODIFIED_AT);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        tech.setUser(user);
        return tech;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tech createUpdatedEntity(EntityManager em) {
        Tech tech = new Tech()
            .name(UPDATED_NAME)
            .techType(UPDATED_TECH_TYPE)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .modifiedAt(UPDATED_MODIFIED_AT);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        tech.setUser(user);
        return tech;
    }

    @BeforeEach
    public void initTest() {
        tech = createEntity(em);
    }

    @Test
    @Transactional
    void createTech() throws Exception {
        int databaseSizeBeforeCreate = techRepository.findAll().size();
        // Create the Tech
        restTechMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tech)))
            .andExpect(status().isCreated());

        // Validate the Tech in the database
        List<Tech> techList = techRepository.findAll();
        assertThat(techList).hasSize(databaseSizeBeforeCreate + 1);
        Tech testTech = techList.get(techList.size() - 1);
        assertThat(testTech.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTech.getTechType()).isEqualTo(DEFAULT_TECH_TYPE);
        assertThat(testTech.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTech.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testTech.getModifiedAt()).isEqualTo(DEFAULT_MODIFIED_AT);
    }

    @Test
    @Transactional
    void createTechWithExistingId() throws Exception {
        // Create the Tech with an existing ID
        tech.setId(1L);

        int databaseSizeBeforeCreate = techRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTechMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tech)))
            .andExpect(status().isBadRequest());

        // Validate the Tech in the database
        List<Tech> techList = techRepository.findAll();
        assertThat(techList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = techRepository.findAll().size();
        // set the field null
        tech.setCreatedAt(null);

        // Create the Tech, which fails.

        restTechMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tech)))
            .andExpect(status().isBadRequest());

        List<Tech> techList = techRepository.findAll();
        assertThat(techList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkModifiedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = techRepository.findAll().size();
        // set the field null
        tech.setModifiedAt(null);

        // Create the Tech, which fails.

        restTechMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tech)))
            .andExpect(status().isBadRequest());

        List<Tech> techList = techRepository.findAll();
        assertThat(techList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTeches() throws Exception {
        // Initialize the database
        techRepository.saveAndFlush(tech);

        // Get all the techList
        restTechMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tech.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].techType").value(hasItem(DEFAULT_TECH_TYPE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].modifiedAt").value(hasItem(DEFAULT_MODIFIED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTechesWithEagerRelationshipsIsEnabled() throws Exception {
        when(techServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTechMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(techServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTechesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(techServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTechMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(techRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getTech() throws Exception {
        // Initialize the database
        techRepository.saveAndFlush(tech);

        // Get the tech
        restTechMockMvc
            .perform(get(ENTITY_API_URL_ID, tech.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tech.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.techType").value(DEFAULT_TECH_TYPE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.modifiedAt").value(DEFAULT_MODIFIED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTech() throws Exception {
        // Get the tech
        restTechMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTech() throws Exception {
        // Initialize the database
        techRepository.saveAndFlush(tech);

        int databaseSizeBeforeUpdate = techRepository.findAll().size();

        // Update the tech
        Tech updatedTech = techRepository.findById(tech.getId()).get();
        // Disconnect from session so that the updates on updatedTech are not directly saved in db
        em.detach(updatedTech);
        updatedTech
            .name(UPDATED_NAME)
            .techType(UPDATED_TECH_TYPE)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .modifiedAt(UPDATED_MODIFIED_AT);

        restTechMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTech.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTech))
            )
            .andExpect(status().isOk());

        // Validate the Tech in the database
        List<Tech> techList = techRepository.findAll();
        assertThat(techList).hasSize(databaseSizeBeforeUpdate);
        Tech testTech = techList.get(techList.size() - 1);
        assertThat(testTech.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTech.getTechType()).isEqualTo(UPDATED_TECH_TYPE);
        assertThat(testTech.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTech.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTech.getModifiedAt()).isEqualTo(UPDATED_MODIFIED_AT);
    }

    @Test
    @Transactional
    void putNonExistingTech() throws Exception {
        int databaseSizeBeforeUpdate = techRepository.findAll().size();
        tech.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTechMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tech.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tech))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tech in the database
        List<Tech> techList = techRepository.findAll();
        assertThat(techList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTech() throws Exception {
        int databaseSizeBeforeUpdate = techRepository.findAll().size();
        tech.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTechMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tech))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tech in the database
        List<Tech> techList = techRepository.findAll();
        assertThat(techList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTech() throws Exception {
        int databaseSizeBeforeUpdate = techRepository.findAll().size();
        tech.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTechMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tech)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tech in the database
        List<Tech> techList = techRepository.findAll();
        assertThat(techList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTechWithPatch() throws Exception {
        // Initialize the database
        techRepository.saveAndFlush(tech);

        int databaseSizeBeforeUpdate = techRepository.findAll().size();

        // Update the tech using partial update
        Tech partialUpdatedTech = new Tech();
        partialUpdatedTech.setId(tech.getId());

        partialUpdatedTech.description(UPDATED_DESCRIPTION).createdAt(UPDATED_CREATED_AT).modifiedAt(UPDATED_MODIFIED_AT);

        restTechMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTech.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTech))
            )
            .andExpect(status().isOk());

        // Validate the Tech in the database
        List<Tech> techList = techRepository.findAll();
        assertThat(techList).hasSize(databaseSizeBeforeUpdate);
        Tech testTech = techList.get(techList.size() - 1);
        assertThat(testTech.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTech.getTechType()).isEqualTo(DEFAULT_TECH_TYPE);
        assertThat(testTech.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTech.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTech.getModifiedAt()).isEqualTo(UPDATED_MODIFIED_AT);
    }

    @Test
    @Transactional
    void fullUpdateTechWithPatch() throws Exception {
        // Initialize the database
        techRepository.saveAndFlush(tech);

        int databaseSizeBeforeUpdate = techRepository.findAll().size();

        // Update the tech using partial update
        Tech partialUpdatedTech = new Tech();
        partialUpdatedTech.setId(tech.getId());

        partialUpdatedTech
            .name(UPDATED_NAME)
            .techType(UPDATED_TECH_TYPE)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .modifiedAt(UPDATED_MODIFIED_AT);

        restTechMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTech.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTech))
            )
            .andExpect(status().isOk());

        // Validate the Tech in the database
        List<Tech> techList = techRepository.findAll();
        assertThat(techList).hasSize(databaseSizeBeforeUpdate);
        Tech testTech = techList.get(techList.size() - 1);
        assertThat(testTech.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTech.getTechType()).isEqualTo(UPDATED_TECH_TYPE);
        assertThat(testTech.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTech.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTech.getModifiedAt()).isEqualTo(UPDATED_MODIFIED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingTech() throws Exception {
        int databaseSizeBeforeUpdate = techRepository.findAll().size();
        tech.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTechMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tech.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tech))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tech in the database
        List<Tech> techList = techRepository.findAll();
        assertThat(techList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTech() throws Exception {
        int databaseSizeBeforeUpdate = techRepository.findAll().size();
        tech.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTechMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tech))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tech in the database
        List<Tech> techList = techRepository.findAll();
        assertThat(techList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTech() throws Exception {
        int databaseSizeBeforeUpdate = techRepository.findAll().size();
        tech.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTechMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tech)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tech in the database
        List<Tech> techList = techRepository.findAll();
        assertThat(techList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTech() throws Exception {
        // Initialize the database
        techRepository.saveAndFlush(tech);

        int databaseSizeBeforeDelete = techRepository.findAll().size();

        // Delete the tech
        restTechMockMvc
            .perform(delete(ENTITY_API_URL_ID, tech.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Tech> techList = techRepository.findAll();
        assertThat(techList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
