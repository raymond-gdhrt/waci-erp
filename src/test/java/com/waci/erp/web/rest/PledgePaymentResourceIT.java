package com.waci.erp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.waci.erp.IntegrationTest;
import com.waci.erp.domain.PledgePayment;
import com.waci.erp.repository.PledgePaymentRepository;
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
 * Integration tests for the {@link PledgePaymentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PledgePaymentResourceIT {

    private static final Float DEFAULT_AMOUNT = 1F;
    private static final Float UPDATED_AMOUNT = 2F;

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_MEMBER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MEMBER_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/pledge-payments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PledgePaymentRepository pledgePaymentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPledgePaymentMockMvc;

    private PledgePayment pledgePayment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PledgePayment createEntity(EntityManager em) {
        PledgePayment pledgePayment = new PledgePayment().amount(DEFAULT_AMOUNT).date(DEFAULT_DATE).memberName(DEFAULT_MEMBER_NAME);
        return pledgePayment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PledgePayment createUpdatedEntity(EntityManager em) {
        PledgePayment pledgePayment = new PledgePayment().amount(UPDATED_AMOUNT).date(UPDATED_DATE).memberName(UPDATED_MEMBER_NAME);
        return pledgePayment;
    }

    @BeforeEach
    public void initTest() {
        pledgePayment = createEntity(em);
    }

    @Test
    @Transactional
    void createPledgePayment() throws Exception {
        int databaseSizeBeforeCreate = pledgePaymentRepository.findAll().size();
        // Create the PledgePayment
        restPledgePaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pledgePayment)))
            .andExpect(status().isCreated());

        // Validate the PledgePayment in the database
        List<PledgePayment> pledgePaymentList = pledgePaymentRepository.findAll();
        assertThat(pledgePaymentList).hasSize(databaseSizeBeforeCreate + 1);
        PledgePayment testPledgePayment = pledgePaymentList.get(pledgePaymentList.size() - 1);
        assertThat(testPledgePayment.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testPledgePayment.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testPledgePayment.getMemberName()).isEqualTo(DEFAULT_MEMBER_NAME);
    }

    @Test
    @Transactional
    void createPledgePaymentWithExistingId() throws Exception {
        // Create the PledgePayment with an existing ID
        pledgePayment.setId(1L);

        int databaseSizeBeforeCreate = pledgePaymentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPledgePaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pledgePayment)))
            .andExpect(status().isBadRequest());

        // Validate the PledgePayment in the database
        List<PledgePayment> pledgePaymentList = pledgePaymentRepository.findAll();
        assertThat(pledgePaymentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = pledgePaymentRepository.findAll().size();
        // set the field null
        pledgePayment.setAmount(null);

        // Create the PledgePayment, which fails.

        restPledgePaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pledgePayment)))
            .andExpect(status().isBadRequest());

        List<PledgePayment> pledgePaymentList = pledgePaymentRepository.findAll();
        assertThat(pledgePaymentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = pledgePaymentRepository.findAll().size();
        // set the field null
        pledgePayment.setDate(null);

        // Create the PledgePayment, which fails.

        restPledgePaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pledgePayment)))
            .andExpect(status().isBadRequest());

        List<PledgePayment> pledgePaymentList = pledgePaymentRepository.findAll();
        assertThat(pledgePaymentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMemberNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = pledgePaymentRepository.findAll().size();
        // set the field null
        pledgePayment.setMemberName(null);

        // Create the PledgePayment, which fails.

        restPledgePaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pledgePayment)))
            .andExpect(status().isBadRequest());

        List<PledgePayment> pledgePaymentList = pledgePaymentRepository.findAll();
        assertThat(pledgePaymentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPledgePayments() throws Exception {
        // Initialize the database
        pledgePaymentRepository.saveAndFlush(pledgePayment);

        // Get all the pledgePaymentList
        restPledgePaymentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pledgePayment.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].memberName").value(hasItem(DEFAULT_MEMBER_NAME)));
    }

    @Test
    @Transactional
    void getPledgePayment() throws Exception {
        // Initialize the database
        pledgePaymentRepository.saveAndFlush(pledgePayment);

        // Get the pledgePayment
        restPledgePaymentMockMvc
            .perform(get(ENTITY_API_URL_ID, pledgePayment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pledgePayment.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.memberName").value(DEFAULT_MEMBER_NAME));
    }

    @Test
    @Transactional
    void getNonExistingPledgePayment() throws Exception {
        // Get the pledgePayment
        restPledgePaymentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPledgePayment() throws Exception {
        // Initialize the database
        pledgePaymentRepository.saveAndFlush(pledgePayment);

        int databaseSizeBeforeUpdate = pledgePaymentRepository.findAll().size();

        // Update the pledgePayment
        PledgePayment updatedPledgePayment = pledgePaymentRepository.findById(pledgePayment.getId()).get();
        // Disconnect from session so that the updates on updatedPledgePayment are not directly saved in db
        em.detach(updatedPledgePayment);
        updatedPledgePayment.amount(UPDATED_AMOUNT).date(UPDATED_DATE).memberName(UPDATED_MEMBER_NAME);

        restPledgePaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPledgePayment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPledgePayment))
            )
            .andExpect(status().isOk());

        // Validate the PledgePayment in the database
        List<PledgePayment> pledgePaymentList = pledgePaymentRepository.findAll();
        assertThat(pledgePaymentList).hasSize(databaseSizeBeforeUpdate);
        PledgePayment testPledgePayment = pledgePaymentList.get(pledgePaymentList.size() - 1);
        assertThat(testPledgePayment.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testPledgePayment.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testPledgePayment.getMemberName()).isEqualTo(UPDATED_MEMBER_NAME);
    }

    @Test
    @Transactional
    void putNonExistingPledgePayment() throws Exception {
        int databaseSizeBeforeUpdate = pledgePaymentRepository.findAll().size();
        pledgePayment.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPledgePaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pledgePayment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pledgePayment))
            )
            .andExpect(status().isBadRequest());

        // Validate the PledgePayment in the database
        List<PledgePayment> pledgePaymentList = pledgePaymentRepository.findAll();
        assertThat(pledgePaymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPledgePayment() throws Exception {
        int databaseSizeBeforeUpdate = pledgePaymentRepository.findAll().size();
        pledgePayment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPledgePaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pledgePayment))
            )
            .andExpect(status().isBadRequest());

        // Validate the PledgePayment in the database
        List<PledgePayment> pledgePaymentList = pledgePaymentRepository.findAll();
        assertThat(pledgePaymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPledgePayment() throws Exception {
        int databaseSizeBeforeUpdate = pledgePaymentRepository.findAll().size();
        pledgePayment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPledgePaymentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pledgePayment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PledgePayment in the database
        List<PledgePayment> pledgePaymentList = pledgePaymentRepository.findAll();
        assertThat(pledgePaymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePledgePaymentWithPatch() throws Exception {
        // Initialize the database
        pledgePaymentRepository.saveAndFlush(pledgePayment);

        int databaseSizeBeforeUpdate = pledgePaymentRepository.findAll().size();

        // Update the pledgePayment using partial update
        PledgePayment partialUpdatedPledgePayment = new PledgePayment();
        partialUpdatedPledgePayment.setId(pledgePayment.getId());

        partialUpdatedPledgePayment.memberName(UPDATED_MEMBER_NAME);

        restPledgePaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPledgePayment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPledgePayment))
            )
            .andExpect(status().isOk());

        // Validate the PledgePayment in the database
        List<PledgePayment> pledgePaymentList = pledgePaymentRepository.findAll();
        assertThat(pledgePaymentList).hasSize(databaseSizeBeforeUpdate);
        PledgePayment testPledgePayment = pledgePaymentList.get(pledgePaymentList.size() - 1);
        assertThat(testPledgePayment.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testPledgePayment.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testPledgePayment.getMemberName()).isEqualTo(UPDATED_MEMBER_NAME);
    }

    @Test
    @Transactional
    void fullUpdatePledgePaymentWithPatch() throws Exception {
        // Initialize the database
        pledgePaymentRepository.saveAndFlush(pledgePayment);

        int databaseSizeBeforeUpdate = pledgePaymentRepository.findAll().size();

        // Update the pledgePayment using partial update
        PledgePayment partialUpdatedPledgePayment = new PledgePayment();
        partialUpdatedPledgePayment.setId(pledgePayment.getId());

        partialUpdatedPledgePayment.amount(UPDATED_AMOUNT).date(UPDATED_DATE).memberName(UPDATED_MEMBER_NAME);

        restPledgePaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPledgePayment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPledgePayment))
            )
            .andExpect(status().isOk());

        // Validate the PledgePayment in the database
        List<PledgePayment> pledgePaymentList = pledgePaymentRepository.findAll();
        assertThat(pledgePaymentList).hasSize(databaseSizeBeforeUpdate);
        PledgePayment testPledgePayment = pledgePaymentList.get(pledgePaymentList.size() - 1);
        assertThat(testPledgePayment.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testPledgePayment.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testPledgePayment.getMemberName()).isEqualTo(UPDATED_MEMBER_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingPledgePayment() throws Exception {
        int databaseSizeBeforeUpdate = pledgePaymentRepository.findAll().size();
        pledgePayment.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPledgePaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pledgePayment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pledgePayment))
            )
            .andExpect(status().isBadRequest());

        // Validate the PledgePayment in the database
        List<PledgePayment> pledgePaymentList = pledgePaymentRepository.findAll();
        assertThat(pledgePaymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPledgePayment() throws Exception {
        int databaseSizeBeforeUpdate = pledgePaymentRepository.findAll().size();
        pledgePayment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPledgePaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pledgePayment))
            )
            .andExpect(status().isBadRequest());

        // Validate the PledgePayment in the database
        List<PledgePayment> pledgePaymentList = pledgePaymentRepository.findAll();
        assertThat(pledgePaymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPledgePayment() throws Exception {
        int databaseSizeBeforeUpdate = pledgePaymentRepository.findAll().size();
        pledgePayment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPledgePaymentMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(pledgePayment))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PledgePayment in the database
        List<PledgePayment> pledgePaymentList = pledgePaymentRepository.findAll();
        assertThat(pledgePaymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePledgePayment() throws Exception {
        // Initialize the database
        pledgePaymentRepository.saveAndFlush(pledgePayment);

        int databaseSizeBeforeDelete = pledgePaymentRepository.findAll().size();

        // Delete the pledgePayment
        restPledgePaymentMockMvc
            .perform(delete(ENTITY_API_URL_ID, pledgePayment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PledgePayment> pledgePaymentList = pledgePaymentRepository.findAll();
        assertThat(pledgePaymentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
