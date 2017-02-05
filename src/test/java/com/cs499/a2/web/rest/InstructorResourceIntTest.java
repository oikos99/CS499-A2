package com.cs499.a2.web.rest;

import com.cs499.a2.A2App;

import com.cs499.a2.domain.Instructor;
import com.cs499.a2.repository.InstructorRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the InstructorResource REST controller.
 *
 * @see InstructorResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = A2App.class)
public class InstructorResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_AGE = 1;
    private static final Integer UPDATED_AGE = 2;

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    @Inject
    private InstructorRepository instructorRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restInstructorMockMvc;

    private Instructor instructor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        InstructorResource instructorResource = new InstructorResource();
        ReflectionTestUtils.setField(instructorResource, "instructorRepository", instructorRepository);
        this.restInstructorMockMvc = MockMvcBuilders.standaloneSetup(instructorResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Instructor createEntity(EntityManager em) {
        Instructor instructor = new Instructor()
                .name(DEFAULT_NAME)
                .age(DEFAULT_AGE)
                .email(DEFAULT_EMAIL);
        return instructor;
    }

    @Before
    public void initTest() {
        instructor = createEntity(em);
    }

    @Test
    @Transactional
    public void createInstructor() throws Exception {
        int databaseSizeBeforeCreate = instructorRepository.findAll().size();

        // Create the Instructor

        restInstructorMockMvc.perform(post("/api/instructors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(instructor)))
            .andExpect(status().isCreated());

        // Validate the Instructor in the database
        List<Instructor> instructorList = instructorRepository.findAll();
        assertThat(instructorList).hasSize(databaseSizeBeforeCreate + 1);
        Instructor testInstructor = instructorList.get(instructorList.size() - 1);
        assertThat(testInstructor.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testInstructor.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testInstructor.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    public void createInstructorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = instructorRepository.findAll().size();

        // Create the Instructor with an existing ID
        Instructor existingInstructor = new Instructor();
        existingInstructor.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInstructorMockMvc.perform(post("/api/instructors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingInstructor)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Instructor> instructorList = instructorRepository.findAll();
        assertThat(instructorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllInstructors() throws Exception {
        // Initialize the database
        instructorRepository.saveAndFlush(instructor);

        // Get all the instructorList
        restInstructorMockMvc.perform(get("/api/instructors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(instructor.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())));
    }

    @Test
    @Transactional
    public void getInstructor() throws Exception {
        // Initialize the database
        instructorRepository.saveAndFlush(instructor);

        // Get the instructor
        restInstructorMockMvc.perform(get("/api/instructors/{id}", instructor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(instructor.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.age").value(DEFAULT_AGE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingInstructor() throws Exception {
        // Get the instructor
        restInstructorMockMvc.perform(get("/api/instructors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInstructor() throws Exception {
        // Initialize the database
        instructorRepository.saveAndFlush(instructor);
        int databaseSizeBeforeUpdate = instructorRepository.findAll().size();

        // Update the instructor
        Instructor updatedInstructor = instructorRepository.findOne(instructor.getId());
        updatedInstructor
                .name(UPDATED_NAME)
                .age(UPDATED_AGE)
                .email(UPDATED_EMAIL);

        restInstructorMockMvc.perform(put("/api/instructors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedInstructor)))
            .andExpect(status().isOk());

        // Validate the Instructor in the database
        List<Instructor> instructorList = instructorRepository.findAll();
        assertThat(instructorList).hasSize(databaseSizeBeforeUpdate);
        Instructor testInstructor = instructorList.get(instructorList.size() - 1);
        assertThat(testInstructor.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testInstructor.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testInstructor.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void updateNonExistingInstructor() throws Exception {
        int databaseSizeBeforeUpdate = instructorRepository.findAll().size();

        // Create the Instructor

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restInstructorMockMvc.perform(put("/api/instructors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(instructor)))
            .andExpect(status().isCreated());

        // Validate the Instructor in the database
        List<Instructor> instructorList = instructorRepository.findAll();
        assertThat(instructorList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteInstructor() throws Exception {
        // Initialize the database
        instructorRepository.saveAndFlush(instructor);
        int databaseSizeBeforeDelete = instructorRepository.findAll().size();

        // Get the instructor
        restInstructorMockMvc.perform(delete("/api/instructors/{id}", instructor.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Instructor> instructorList = instructorRepository.findAll();
        assertThat(instructorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
