package com.waci.erp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.waci.erp.IntegrationTest;
import com.waci.erp.domain.ChurchMember;
import com.waci.erp.repository.ChurchMemberRepository;
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
 * Integration tests for the {@link ChurchMemberResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ChurchMemberResourceIT {

    private static final String DEFAULT_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_DATE = "AAAAAAAAAA";
    private static final String UPDATED_DATE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/church-members";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ChurchMemberRepository churchMemberRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restChurchMemberMockMvc;

    private ChurchMember churchMember;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChurchMember createEntity(EntityManager em) {
        ChurchMember churchMember = new ChurchMember()
            .fullName(DEFAULT_FULL_NAME)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .date(DEFAULT_DATE)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE);
        return churchMember;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChurchMember createUpdatedEntity(EntityManager em) {
        ChurchMember churchMember = new ChurchMember()
            .fullName(UPDATED_FULL_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .date(UPDATED_DATE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);
        return churchMember;
    }

    @BeforeEach
    public void initTest() {
        churchMember = createEntity(em);
    }

    @Test
    @Transactional
    void createChurchMember() throws Exception {
        int databaseSizeBeforeCreate = churchMemberRepository.findAll().size();
        // Create the ChurchMember
        restChurchMemberMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(churchMember)))
            .andExpect(status().isCreated());

        // Validate the ChurchMember in the database
        List<ChurchMember> churchMemberList = churchMemberRepository.findAll();
        assertThat(churchMemberList).hasSize(databaseSizeBeforeCreate + 1);
        ChurchMember testChurchMember = churchMemberList.get(churchMemberList.size() - 1);
        assertThat(testChurchMember.getFullName()).isEqualTo(DEFAULT_FULL_NAME);
        assertThat(testChurchMember.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testChurchMember.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testChurchMember.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testChurchMember.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void createChurchMemberWithExistingId() throws Exception {
        // Create the ChurchMember with an existing ID
        churchMember.setId(1L);

        int databaseSizeBeforeCreate = churchMemberRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restChurchMemberMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(churchMember)))
            .andExpect(status().isBadRequest());

        // Validate the ChurchMember in the database
        List<ChurchMember> churchMemberList = churchMemberRepository.findAll();
        assertThat(churchMemberList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFullNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = churchMemberRepository.findAll().size();
        // set the field null
        churchMember.setFullName(null);

        // Create the ChurchMember, which fails.

        restChurchMemberMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(churchMember)))
            .andExpect(status().isBadRequest());

        List<ChurchMember> churchMemberList = churchMemberRepository.findAll();
        assertThat(churchMemberList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPhoneNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = churchMemberRepository.findAll().size();
        // set the field null
        churchMember.setPhoneNumber(null);

        // Create the ChurchMember, which fails.

        restChurchMemberMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(churchMember)))
            .andExpect(status().isBadRequest());

        List<ChurchMember> churchMemberList = churchMemberRepository.findAll();
        assertThat(churchMemberList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = churchMemberRepository.findAll().size();
        // set the field null
        churchMember.setDate(null);

        // Create the ChurchMember, which fails.

        restChurchMemberMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(churchMember)))
            .andExpect(status().isBadRequest());

        List<ChurchMember> churchMemberList = churchMemberRepository.findAll();
        assertThat(churchMemberList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = churchMemberRepository.findAll().size();
        // set the field null
        churchMember.setStartDate(null);

        // Create the ChurchMember, which fails.

        restChurchMemberMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(churchMember)))
            .andExpect(status().isBadRequest());

        List<ChurchMember> churchMemberList = churchMemberRepository.findAll();
        assertThat(churchMemberList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = churchMemberRepository.findAll().size();
        // set the field null
        churchMember.setEndDate(null);

        // Create the ChurchMember, which fails.

        restChurchMemberMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(churchMember)))
            .andExpect(status().isBadRequest());

        List<ChurchMember> churchMemberList = churchMemberRepository.findAll();
        assertThat(churchMemberList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllChurchMembers() throws Exception {
        // Initialize the database
        churchMemberRepository.saveAndFlush(churchMember);

        // Get all the churchMemberList
        restChurchMemberMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(churchMember.getId().intValue())))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));
    }

    @Test
    @Transactional
    void getChurchMember() throws Exception {
        // Initialize the database
        churchMemberRepository.saveAndFlush(churchMember);

        // Get the churchMember
        restChurchMemberMockMvc
            .perform(get(ENTITY_API_URL_ID, churchMember.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(churchMember.getId().intValue()))
            .andExpect(jsonPath("$.fullName").value(DEFAULT_FULL_NAME))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingChurchMember() throws Exception {
        // Get the churchMember
        restChurchMemberMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewChurchMember() throws Exception {
        // Initialize the database
        churchMemberRepository.saveAndFlush(churchMember);

        int databaseSizeBeforeUpdate = churchMemberRepository.findAll().size();

        // Update the churchMember
        ChurchMember updatedChurchMember = churchMemberRepository.findById(churchMember.getId()).get();
        // Disconnect from session so that the updates on updatedChurchMember are not directly saved in db
        em.detach(updatedChurchMember);
        updatedChurchMember
            .fullName(UPDATED_FULL_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .date(UPDATED_DATE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);

        restChurchMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedChurchMember.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedChurchMember))
            )
            .andExpect(status().isOk());

        // Validate the ChurchMember in the database
        List<ChurchMember> churchMemberList = churchMemberRepository.findAll();
        assertThat(churchMemberList).hasSize(databaseSizeBeforeUpdate);
        ChurchMember testChurchMember = churchMemberList.get(churchMemberList.size() - 1);
        assertThat(testChurchMember.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testChurchMember.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testChurchMember.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testChurchMember.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testChurchMember.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void putNonExistingChurchMember() throws Exception {
        int databaseSizeBeforeUpdate = churchMemberRepository.findAll().size();
        churchMember.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChurchMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, churchMember.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(churchMember))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChurchMember in the database
        List<ChurchMember> churchMemberList = churchMemberRepository.findAll();
        assertThat(churchMemberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchChurchMember() throws Exception {
        int databaseSizeBeforeUpdate = churchMemberRepository.findAll().size();
        churchMember.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChurchMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(churchMember))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChurchMember in the database
        List<ChurchMember> churchMemberList = churchMemberRepository.findAll();
        assertThat(churchMemberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamChurchMember() throws Exception {
        int databaseSizeBeforeUpdate = churchMemberRepository.findAll().size();
        churchMember.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChurchMemberMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(churchMember)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ChurchMember in the database
        List<ChurchMember> churchMemberList = churchMemberRepository.findAll();
        assertThat(churchMemberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateChurchMemberWithPatch() throws Exception {
        // Initialize the database
        churchMemberRepository.saveAndFlush(churchMember);

        int databaseSizeBeforeUpdate = churchMemberRepository.findAll().size();

        // Update the churchMember using partial update
        ChurchMember partialUpdatedChurchMember = new ChurchMember();
        partialUpdatedChurchMember.setId(churchMember.getId());

        partialUpdatedChurchMember.fullName(UPDATED_FULL_NAME);

        restChurchMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChurchMember.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedChurchMember))
            )
            .andExpect(status().isOk());

        // Validate the ChurchMember in the database
        List<ChurchMember> churchMemberList = churchMemberRepository.findAll();
        assertThat(churchMemberList).hasSize(databaseSizeBeforeUpdate);
        ChurchMember testChurchMember = churchMemberList.get(churchMemberList.size() - 1);
        assertThat(testChurchMember.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testChurchMember.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testChurchMember.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testChurchMember.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testChurchMember.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void fullUpdateChurchMemberWithPatch() throws Exception {
        // Initialize the database
        churchMemberRepository.saveAndFlush(churchMember);

        int databaseSizeBeforeUpdate = churchMemberRepository.findAll().size();

        // Update the churchMember using partial update
        ChurchMember partialUpdatedChurchMember = new ChurchMember();
        partialUpdatedChurchMember.setId(churchMember.getId());

        partialUpdatedChurchMember
            .fullName(UPDATED_FULL_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .date(UPDATED_DATE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);

        restChurchMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChurchMember.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedChurchMember))
            )
            .andExpect(status().isOk());

        // Validate the ChurchMember in the database
        List<ChurchMember> churchMemberList = churchMemberRepository.findAll();
        assertThat(churchMemberList).hasSize(databaseSizeBeforeUpdate);
        ChurchMember testChurchMember = churchMemberList.get(churchMemberList.size() - 1);
        assertThat(testChurchMember.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testChurchMember.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testChurchMember.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testChurchMember.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testChurchMember.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingChurchMember() throws Exception {
        int databaseSizeBeforeUpdate = churchMemberRepository.findAll().size();
        churchMember.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChurchMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, churchMember.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(churchMember))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChurchMember in the database
        List<ChurchMember> churchMemberList = churchMemberRepository.findAll();
        assertThat(churchMemberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchChurchMember() throws Exception {
        int databaseSizeBeforeUpdate = churchMemberRepository.findAll().size();
        churchMember.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChurchMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(churchMember))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChurchMember in the database
        List<ChurchMember> churchMemberList = churchMemberRepository.findAll();
        assertThat(churchMemberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamChurchMember() throws Exception {
        int databaseSizeBeforeUpdate = churchMemberRepository.findAll().size();
        churchMember.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChurchMemberMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(churchMember))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ChurchMember in the database
        List<ChurchMember> churchMemberList = churchMemberRepository.findAll();
        assertThat(churchMemberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteChurchMember() throws Exception {
        // Initialize the database
        churchMemberRepository.saveAndFlush(churchMember);

        int databaseSizeBeforeDelete = churchMemberRepository.findAll().size();

        // Delete the churchMember
        restChurchMemberMockMvc
            .perform(delete(ENTITY_API_URL_ID, churchMember.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ChurchMember> churchMemberList = churchMemberRepository.findAll();
        assertThat(churchMemberList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
