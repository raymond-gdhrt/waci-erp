package com.waci.erp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.waci.erp.IntegrationTest;
import com.waci.erp.domain.Pledge;
import com.waci.erp.repository.PledgeRepository;
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
 * Integration tests for the {@link PledgeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PledgeResourceIT {

    private static final Float DEFAULT_AMOUNT = 1F;
    private static final Float UPDATED_AMOUNT = 2F;

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_MEMBER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MEMBER_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/pledges";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PledgeRepository pledgeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPledgeMockMvc;

    private Pledge pledge;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pledge createEntity(EntityManager em) {
        Pledge pledge = new Pledge().amount(DEFAULT_AMOUNT).date(DEFAULT_DATE).memberName(DEFAULT_MEMBER_NAME);
        return pledge;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pledge createUpdatedEntity(EntityManager em) {
        Pledge pledge = new Pledge().amount(UPDATED_AMOUNT).date(UPDATED_DATE).memberName(UPDATED_MEMBER_NAME);
        return pledge;
    }

    @BeforeEach
    public void initTest() {
        pledge = createEntity(em);
    }

    @Test
    @Transactional
    void createPledge() throws Exception {
        int databaseSizeBeforeCreate = pledgeRepository.findAll().size();
        // Create the Pledge
        restPledgeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pledge)))
            .andExpect(status().isCreated());

        // Validate the Pledge in the database
        List<Pledge> pledgeList = pledgeRepository.findAll();
        assertThat(pledgeList).hasSize(databaseSizeBeforeCreate + 1);
        Pledge testPledge = pledgeList.get(pledgeList.size() - 1);
        assertThat(testPledge.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testPledge.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testPledge.getMemberName()).isEqualTo(DEFAULT_MEMBER_NAME);
    }

    @Test
    @Transactional
    void createPledgeWithExistingId() throws Exception {
        // Create the Pledge with an existing ID
        pledge.setId(1L);

        int databaseSizeBeforeCreate = pledgeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPledgeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pledge)))
            .andExpect(status().isBadRequest());

        // Validate the Pledge in the database
        List<Pledge> pledgeList = pledgeRepository.findAll();
        assertThat(pledgeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = pledgeRepository.findAll().size();
        // set the field null
        pledge.setAmount(null);

        // Create the Pledge, which fails.

        restPledgeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pledge)))
            .andExpect(status().isBadRequest());

        List<Pledge> pledgeList = pledgeRepository.findAll();
        assertThat(pledgeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = pledgeRepository.findAll().size();
        // set the field null
        pledge.setDate(null);

        // Create the Pledge, which fails.

        restPledgeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pledge)))
            .andExpect(status().isBadRequest());

        List<Pledge> pledgeList = pledgeRepository.findAll();
        assertThat(pledgeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMemberNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = pledgeRepository.findAll().size();
        // set the field null
        pledge.setMemberName(null);

        // Create the Pledge, which fails.

        restPledgeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pledge)))
            .andExpect(status().isBadRequest());

        List<Pledge> pledgeList = pledgeRepository.findAll();
        assertThat(pledgeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPledges() throws Exception {
        // Initialize the database
        pledgeRepository.saveAndFlush(pledge);

        // Get all the pledgeList
        restPledgeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pledge.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].memberName").value(hasItem(DEFAULT_MEMBER_NAME)));
    }

    @Test
    @Transactional
    void getPledge() throws Exception {
        // Initialize the database
        pledgeRepository.saveAndFlush(pledge);

        // Get the pledge
        restPledgeMockMvc
            .perform(get(ENTITY_API_URL_ID, pledge.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pledge.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.memberName").value(DEFAULT_MEMBER_NAME));
    }

    @Test
    @Transactional
    void getNonExistingPledge() throws Exception {
        // Get the pledge
        restPledgeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPledge() throws Exception {
        // Initialize the database
        pledgeRepository.saveAndFlush(pledge);

        int databaseSizeBeforeUpdate = pledgeRepository.findAll().size();

        // Update the pledge
        Pledge updatedPledge = pledgeRepository.findById(pledge.getId()).get();
        // Disconnect from session so that the updates on updatedPledge are not directly saved in db
        em.detach(updatedPledge);
        updatedPledge.amount(UPDATED_AMOUNT).date(UPDATED_DATE).memberName(UPDATED_MEMBER_NAME);

        restPledgeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPledge.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPledge))
            )
            .andExpect(status().isOk());

        // Validate the Pledge in the database
        List<Pledge> pledgeList = pledgeRepository.findAll();
        assertThat(pledgeList).hasSize(databaseSizeBeforeUpdate);
        Pledge testPledge = pledgeList.get(pledgeList.size() - 1);
        assertThat(testPledge.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testPledge.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testPledge.getMemberName()).isEqualTo(UPDATED_MEMBER_NAME);
    }

    @Test
    @Transactional
    void putNonExistingPledge() throws Exception {
        int databaseSizeBeforeUpdate = pledgeRepository.findAll().size();
        pledge.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPledgeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pledge.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pledge))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pledge in the database
        List<Pledge> pledgeList = pledgeRepository.findAll();
        assertThat(pledgeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPledge() throws Exception {
        int databaseSizeBeforeUpdate = pledgeRepository.findAll().size();
        pledge.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPledgeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pledge))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pledge in the database
        List<Pledge> pledgeList = pledgeRepository.findAll();
        assertThat(pledgeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPledge() throws Exception {
        int databaseSizeBeforeUpdate = pledgeRepository.findAll().size();
        pledge.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPledgeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pledge)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pledge in the database
        List<Pledge> pledgeList = pledgeRepository.findAll();
        assertThat(pledgeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePledgeWithPatch() throws Exception {
        // Initialize the database
        pledgeRepository.saveAndFlush(pledge);

        int databaseSizeBeforeUpdate = pledgeRepository.findAll().size();

        // Update the pledge using partial update
        Pledge partialUpdatedPledge = new Pledge();
        partialUpdatedPledge.setId(pledge.getId());

        partialUpdatedPledge.memberName(UPDATED_MEMBER_NAME);

        restPledgeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPledge.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPledge))
            )
            .andExpect(status().isOk());

        // Validate the Pledge in the database
        List<Pledge> pledgeList = pledgeRepository.findAll();
        assertThat(pledgeList).hasSize(databaseSizeBeforeUpdate);
        Pledge testPledge = pledgeList.get(pledgeList.size() - 1);
        assertThat(testPledge.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testPledge.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testPledge.getMemberName()).isEqualTo(UPDATED_MEMBER_NAME);
    }

    @Test
    @Transactional
    void fullUpdatePledgeWithPatch() throws Exception {
        // Initialize the database
        pledgeRepository.saveAndFlush(pledge);

        int databaseSizeBeforeUpdate = pledgeRepository.findAll().size();

        // Update the pledge using partial update
        Pledge partialUpdatedPledge = new Pledge();
        partialUpdatedPledge.setId(pledge.getId());

        partialUpdatedPledge.amount(UPDATED_AMOUNT).date(UPDATED_DATE).memberName(UPDATED_MEMBER_NAME);

        restPledgeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPledge.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPledge))
            )
            .andExpect(status().isOk());

        // Validate the Pledge in the database
        List<Pledge> pledgeList = pledgeRepository.findAll();
        assertThat(pledgeList).hasSize(databaseSizeBeforeUpdate);
        Pledge testPledge = pledgeList.get(pledgeList.size() - 1);
        assertThat(testPledge.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testPledge.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testPledge.getMemberName()).isEqualTo(UPDATED_MEMBER_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingPledge() throws Exception {
        int databaseSizeBeforeUpdate = pledgeRepository.findAll().size();
        pledge.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPledgeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pledge.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pledge))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pledge in the database
        List<Pledge> pledgeList = pledgeRepository.findAll();
        assertThat(pledgeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPledge() throws Exception {
        int databaseSizeBeforeUpdate = pledgeRepository.findAll().size();
        pledge.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPledgeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pledge))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pledge in the database
        List<Pledge> pledgeList = pledgeRepository.findAll();
        assertThat(pledgeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPledge() throws Exception {
        int databaseSizeBeforeUpdate = pledgeRepository.findAll().size();
        pledge.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPledgeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(pledge)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pledge in the database
        List<Pledge> pledgeList = pledgeRepository.findAll();
        assertThat(pledgeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePledge() throws Exception {
        // Initialize the database
        pledgeRepository.saveAndFlush(pledge);

        int databaseSizeBeforeDelete = pledgeRepository.findAll().size();

        // Delete the pledge
        restPledgeMockMvc
            .perform(delete(ENTITY_API_URL_ID, pledge.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Pledge> pledgeList = pledgeRepository.findAll();
        assertThat(pledgeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
