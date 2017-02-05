package com.cs499.a2.web.rest;

import com.cs499.a2.A2App;

import com.cs499.a2.domain.OfficeHour;
import com.cs499.a2.repository.OfficeHourRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.cs499.a2.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the OfficeHourResource REST controller.
 *
 * @see OfficeHourResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = A2App.class)
public class OfficeHourResourceIntTest {

    private static final ZonedDateTime DEFAULT_START_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_END_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_END_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Inject
    private OfficeHourRepository officeHourRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restOfficeHourMockMvc;

    private OfficeHour officeHour;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OfficeHourResource officeHourResource = new OfficeHourResource();
        ReflectionTestUtils.setField(officeHourResource, "officeHourRepository", officeHourRepository);
        this.restOfficeHourMockMvc = MockMvcBuilders.standaloneSetup(officeHourResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OfficeHour createEntity(EntityManager em) {
        OfficeHour officeHour = new OfficeHour()
                .startTime(DEFAULT_START_TIME)
                .endTime(DEFAULT_END_TIME);
        return officeHour;
    }

    @Before
    public void initTest() {
        officeHour = createEntity(em);
    }

    @Test
    @Transactional
    public void createOfficeHour() throws Exception {
        int databaseSizeBeforeCreate = officeHourRepository.findAll().size();

        // Create the OfficeHour

        restOfficeHourMockMvc.perform(post("/api/office-hours")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(officeHour)))
            .andExpect(status().isCreated());

        // Validate the OfficeHour in the database
        List<OfficeHour> officeHourList = officeHourRepository.findAll();
        assertThat(officeHourList).hasSize(databaseSizeBeforeCreate + 1);
        OfficeHour testOfficeHour = officeHourList.get(officeHourList.size() - 1);
        assertThat(testOfficeHour.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testOfficeHour.getEndTime()).isEqualTo(DEFAULT_END_TIME);
    }

    @Test
    @Transactional
    public void createOfficeHourWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = officeHourRepository.findAll().size();

        // Create the OfficeHour with an existing ID
        OfficeHour existingOfficeHour = new OfficeHour();
        existingOfficeHour.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOfficeHourMockMvc.perform(post("/api/office-hours")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingOfficeHour)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<OfficeHour> officeHourList = officeHourRepository.findAll();
        assertThat(officeHourList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllOfficeHours() throws Exception {
        // Initialize the database
        officeHourRepository.saveAndFlush(officeHour);

        // Get all the officeHourList
        restOfficeHourMockMvc.perform(get("/api/office-hours?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(officeHour.getId().intValue())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(sameInstant(DEFAULT_START_TIME))))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(sameInstant(DEFAULT_END_TIME))));
    }

    @Test
    @Transactional
    public void getOfficeHour() throws Exception {
        // Initialize the database
        officeHourRepository.saveAndFlush(officeHour);

        // Get the officeHour
        restOfficeHourMockMvc.perform(get("/api/office-hours/{id}", officeHour.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(officeHour.getId().intValue()))
            .andExpect(jsonPath("$.startTime").value(sameInstant(DEFAULT_START_TIME)))
            .andExpect(jsonPath("$.endTime").value(sameInstant(DEFAULT_END_TIME)));
    }

    @Test
    @Transactional
    public void getNonExistingOfficeHour() throws Exception {
        // Get the officeHour
        restOfficeHourMockMvc.perform(get("/api/office-hours/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOfficeHour() throws Exception {
        // Initialize the database
        officeHourRepository.saveAndFlush(officeHour);
        int databaseSizeBeforeUpdate = officeHourRepository.findAll().size();

        // Update the officeHour
        OfficeHour updatedOfficeHour = officeHourRepository.findOne(officeHour.getId());
        updatedOfficeHour
                .startTime(UPDATED_START_TIME)
                .endTime(UPDATED_END_TIME);

        restOfficeHourMockMvc.perform(put("/api/office-hours")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedOfficeHour)))
            .andExpect(status().isOk());

        // Validate the OfficeHour in the database
        List<OfficeHour> officeHourList = officeHourRepository.findAll();
        assertThat(officeHourList).hasSize(databaseSizeBeforeUpdate);
        OfficeHour testOfficeHour = officeHourList.get(officeHourList.size() - 1);
        assertThat(testOfficeHour.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testOfficeHour.getEndTime()).isEqualTo(UPDATED_END_TIME);
    }

    @Test
    @Transactional
    public void updateNonExistingOfficeHour() throws Exception {
        int databaseSizeBeforeUpdate = officeHourRepository.findAll().size();

        // Create the OfficeHour

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restOfficeHourMockMvc.perform(put("/api/office-hours")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(officeHour)))
            .andExpect(status().isCreated());

        // Validate the OfficeHour in the database
        List<OfficeHour> officeHourList = officeHourRepository.findAll();
        assertThat(officeHourList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteOfficeHour() throws Exception {
        // Initialize the database
        officeHourRepository.saveAndFlush(officeHour);
        int databaseSizeBeforeDelete = officeHourRepository.findAll().size();

        // Get the officeHour
        restOfficeHourMockMvc.perform(delete("/api/office-hours/{id}", officeHour.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<OfficeHour> officeHourList = officeHourRepository.findAll();
        assertThat(officeHourList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
