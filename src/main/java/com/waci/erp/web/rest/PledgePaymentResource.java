package com.waci.erp.web.rest;

import com.waci.erp.domain.PledgePayment;
import com.waci.erp.repository.PledgePaymentRepository;
import com.waci.erp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.waci.erp.domain.PledgePayment}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PledgePaymentResource {

    private final Logger log = LoggerFactory.getLogger(PledgePaymentResource.class);

    private static final String ENTITY_NAME = "pledgePayment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PledgePaymentRepository pledgePaymentRepository;

    public PledgePaymentResource(PledgePaymentRepository pledgePaymentRepository) {
        this.pledgePaymentRepository = pledgePaymentRepository;
    }

    /**
     * {@code POST  /pledge-payments} : Create a new pledgePayment.
     *
     * @param pledgePayment the pledgePayment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pledgePayment, or with status {@code 400 (Bad Request)} if the pledgePayment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pledge-payments")
    public ResponseEntity<PledgePayment> createPledgePayment(@Valid @RequestBody PledgePayment pledgePayment) throws URISyntaxException {
        log.debug("REST request to save PledgePayment : {}", pledgePayment);
        if (pledgePayment.getId() != null) {
            throw new BadRequestAlertException("A new pledgePayment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PledgePayment result = pledgePaymentRepository.save(pledgePayment);
        return ResponseEntity
            .created(new URI("/api/pledge-payments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pledge-payments/:id} : Updates an existing pledgePayment.
     *
     * @param id the id of the pledgePayment to save.
     * @param pledgePayment the pledgePayment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pledgePayment,
     * or with status {@code 400 (Bad Request)} if the pledgePayment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pledgePayment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pledge-payments/{id}")
    public ResponseEntity<PledgePayment> updatePledgePayment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PledgePayment pledgePayment
    ) throws URISyntaxException {
        log.debug("REST request to update PledgePayment : {}, {}", id, pledgePayment);
        if (pledgePayment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pledgePayment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pledgePaymentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PledgePayment result = pledgePaymentRepository.save(pledgePayment);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pledgePayment.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /pledge-payments/:id} : Partial updates given fields of an existing pledgePayment, field will ignore if it is null
     *
     * @param id the id of the pledgePayment to save.
     * @param pledgePayment the pledgePayment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pledgePayment,
     * or with status {@code 400 (Bad Request)} if the pledgePayment is not valid,
     * or with status {@code 404 (Not Found)} if the pledgePayment is not found,
     * or with status {@code 500 (Internal Server Error)} if the pledgePayment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/pledge-payments/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PledgePayment> partialUpdatePledgePayment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PledgePayment pledgePayment
    ) throws URISyntaxException {
        log.debug("REST request to partial update PledgePayment partially : {}, {}", id, pledgePayment);
        if (pledgePayment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pledgePayment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pledgePaymentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PledgePayment> result = pledgePaymentRepository
            .findById(pledgePayment.getId())
            .map(existingPledgePayment -> {
                if (pledgePayment.getAmount() != null) {
                    existingPledgePayment.setAmount(pledgePayment.getAmount());
                }
                if (pledgePayment.getDate() != null) {
                    existingPledgePayment.setDate(pledgePayment.getDate());
                }
                if (pledgePayment.getMemberName() != null) {
                    existingPledgePayment.setMemberName(pledgePayment.getMemberName());
                }

                return existingPledgePayment;
            })
            .map(pledgePaymentRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pledgePayment.getId().toString())
        );
    }

    /**
     * {@code GET  /pledge-payments} : get all the pledgePayments.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pledgePayments in body.
     */
    @GetMapping("/pledge-payments")
    public List<PledgePayment> getAllPledgePayments() {
        log.debug("REST request to get all PledgePayments");
        return pledgePaymentRepository.findAll();
    }

    /**
     * {@code GET  /pledge-payments/:id} : get the "id" pledgePayment.
     *
     * @param id the id of the pledgePayment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pledgePayment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pledge-payments/{id}")
    public ResponseEntity<PledgePayment> getPledgePayment(@PathVariable Long id) {
        log.debug("REST request to get PledgePayment : {}", id);
        Optional<PledgePayment> pledgePayment = pledgePaymentRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(pledgePayment);
    }

    /**
     * {@code DELETE  /pledge-payments/:id} : delete the "id" pledgePayment.
     *
     * @param id the id of the pledgePayment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pledge-payments/{id}")
    public ResponseEntity<Void> deletePledgePayment(@PathVariable Long id) {
        log.debug("REST request to delete PledgePayment : {}", id);
        pledgePaymentRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
