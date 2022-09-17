package com.waci.erp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.waci.erp.IntegrationTest;
import com.waci.erp.domain.Program;
import com.waci.erp.repository.ProgramRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ProgramResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProgramResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Float DEFAULT_MIN_AMOUNT = 1F;
    private static final Float UPDATED_MIN_AMOUNT = 2F;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/programs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProgramRepository programRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProgramMockMvc;

    private Program program;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Program createEntity(EntityManager em) {
        Program program = new Program()
            .name(DEFAULT_NAME)
            .minAmount(DEFAULT_MIN_AMOUNT)
            .description(DEFAULT_DESCRIPTION)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE);
        return program;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Program createUpdatedEntity(EntityManager em) {
        Program program = new Program()
            .name(UPDATED_NAME)
            .minAmount(UPDATED_MIN_AMOUNT)
            .description(UPDATED_DESCRIPTION)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);
        return program;
    }

    @BeforeEach
    public void initTest() {
        program = createEntity(em);
    }

    @Test
    @Transactional
    void createProgram() throws Exception {
        int databaseSizeBeforeCreate = programRepository.findAll().size();
        // Create the Program
        restProgramMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(program)))
            .andExpect(status().isCreated());

        // Validate the Program in the database
        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeCreate + 1);
        Program testProgram = programList.get(programList.size() - 1);
        assertThat(testProgram.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProgram.getMinAmount()).isEqualTo(DEFAULT_MIN_AMOUNT);
        assertThat(testProgram.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProgram.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testProgram.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void createProgramWithExistingId() throws Exception {
        // Create the Program with an existing ID
        program.setId(1L);

        int databaseSizeBeforeCreate = programRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProgramMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(program)))
            .andExpect(status().isBadRequest());

        // Validate the Program in the database
        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = programRepository.findAll().size();
        // set the field null
        program.setName(null);

        // Create the Program, which fails.

        restProgramMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(program)))
            .andExpect(status().isBadRequest());

        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMinAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = programRepository.findAll().size();
        // set the field null
        program.setMinAmount(null);

        // Create the Program, which fails.

        restProgramMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(program)))
            .andExpect(status().isBadRequest());

        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = programRepository.findAll().size();
        // set the field null
        program.setDescription(null);

        // Create the Program, which fails.

        restProgramMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(program)))
            .andExpect(status().isBadRequest());

        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = programRepository.findAll().size();
        // set the field null
        program.setStartDate(null);

        // Create the Program, which fails.

        restProgramMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(program)))
            .andExpect(status().isBadRequest());

        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = programRepository.findAll().size();
        // set the field null
        program.setEndDate(null);

        // Create the Program, which fails.

        restProgramMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(program)))
            .andExpect(status().isBadRequest());

        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPrograms() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get all the programList
        restProgramMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(program.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].minAmount").value(hasItem(DEFAULT_MIN_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));
    }

    @Test
    @Transactional
    void getProgram() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get the program
        restProgramMockMvc
            .perform(get(ENTITY_API_URL_ID, program.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(program.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.minAmount").value(DEFAULT_MIN_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingProgram() throws Exception {
        // Get the program
        restProgramMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProgram() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        int databaseSizeBeforeUpdate = programRepository.findAll().size();

        // Update the program
        Program updatedProgram = programRepository.findById(program.getId()).get();
        // Disconnect from session so that the updates on updatedProgram are not directly saved in db
        em.detach(updatedProgram);
        updatedProgram
            .name(UPDATED_NAME)
            .minAmount(UPDATED_MIN_AMOUNT)
            .description(UPDATED_DESCRIPTION)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);

        restProgramMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProgram.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProgram))
            )
            .andExpect(status().isOk());

        // Validate the Program in the database
        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeUpdate);
        Program testProgram = programList.get(programList.size() - 1);
        assertThat(testProgram.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProgram.getMinAmount()).isEqualTo(UPDATED_MIN_AMOUNT);
        assertThat(testProgram.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProgram.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testProgram.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void putNonExistingProgram() throws Exception {
        int databaseSizeBeforeUpdate = programRepository.findAll().size();
        program.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProgramMockMvc
            .perform(
                put(ENTITY_API_URL_ID, program.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(program))
            )
            .andExpect(status().isBadRequest());

        // Validate the Program in the database
        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProgram() throws Exception {
        int databaseSizeBeforeUpdate = programRepository.findAll().size();
        program.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProgramMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(program))
            )
            .andExpect(status().isBadRequest());

        // Validate the Program in the database
        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProgram() throws Exception {
        int databaseSizeBeforeUpdate = programRepository.findAll().size();
        program.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProgramMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(program)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Program in the database
        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProgramWithPatch() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        int databaseSizeBeforeUpdate = programRepository.findAll().size();

        // Update the program using partial update
        Program partialUpdatedProgram = new Program();
        partialUpdatedProgram.setId(program.getId());

        partialUpdatedProgram.description(UPDATED_DESCRIPTION);

        restProgramMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProgram.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProgram))
            )
            .andExpect(status().isOk());

        // Validate the Program in the database
        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeUpdate);
        Program testProgram = programList.get(programList.size() - 1);
        assertThat(testProgram.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProgram.getMinAmount()).isEqualTo(DEFAULT_MIN_AMOUNT);
        assertThat(testProgram.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProgram.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testProgram.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void fullUpdateProgramWithPatch() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        int databaseSizeBeforeUpdate = programRepository.findAll().size();

        // Update the program using partial update
        Program partialUpdatedProgram = new Program();
        partialUpdatedProgram.setId(program.getId());

        partialUpdatedProgram
            .name(UPDATED_NAME)
            .minAmount(UPDATED_MIN_AMOUNT)
            .description(UPDATED_DESCRIPTION)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);

        restProgramMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProgram.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProgram))
            )
            .andExpect(status().isOk());

        // Validate the Program in the database
        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeUpdate);
        Program testProgram = programList.get(programList.size() - 1);
        assertThat(testProgram.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProgram.getMinAmount()).isEqualTo(UPDATED_MIN_AMOUNT);
        assertThat(testProgram.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProgram.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testProgram.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingProgram() throws Exception {
        int databaseSizeBeforeUpdate = programRepository.findAll().size();
        program.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProgramMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, program.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(program))
            )
            .andExpect(status().isBadRequest());

        // Validate the Program in the database
        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProgram() throws Exception {
        int databaseSizeBeforeUpdate = programRepository.findAll().size();
        program.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProgramMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(program))
            )
            .andExpect(status().isBadRequest());

        // Validate the Program in the database
        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProgram() throws Exception {
        int databaseSizeBeforeUpdate = programRepository.findAll().size();
        program.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProgramMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(program)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Program in the database
        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProgram() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        int databaseSizeBeforeDelete = programRepository.findAll().size();

        // Delete the program
        restProgramMockMvc
            .perform(delete(ENTITY_API_URL_ID, program.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
