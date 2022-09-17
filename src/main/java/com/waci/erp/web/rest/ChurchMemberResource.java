package com.waci.erp.web.rest;

import com.waci.erp.domain.ChurchMember;
import com.waci.erp.repository.ChurchMemberRepository;
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
 * REST controller for managing {@link com.waci.erp.domain.ChurchMember}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ChurchMemberResource {

    private final Logger log = LoggerFactory.getLogger(ChurchMemberResource.class);

    private static final String ENTITY_NAME = "churchMember";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ChurchMemberRepository churchMemberRepository;

    public ChurchMemberResource(ChurchMemberRepository churchMemberRepository) {
        this.churchMemberRepository = churchMemberRepository;
    }

    /**
     * {@code POST  /church-members} : Create a new churchMember.
     *
     * @param churchMember the churchMember to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new churchMember, or with status {@code 400 (Bad Request)} if the churchMember has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/church-members")
    public ResponseEntity<ChurchMember> createChurchMember(@Valid @RequestBody ChurchMember churchMember) throws URISyntaxException {
        log.debug("REST request to save ChurchMember : {}", churchMember);
        if (churchMember.getId() != null) {
            throw new BadRequestAlertException("A new churchMember cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ChurchMember result = churchMemberRepository.save(churchMember);
        return ResponseEntity
            .created(new URI("/api/church-members/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /church-members/:id} : Updates an existing churchMember.
     *
     * @param id the id of the churchMember to save.
     * @param churchMember the churchMember to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated churchMember,
     * or with status {@code 400 (Bad Request)} if the churchMember is not valid,
     * or with status {@code 500 (Internal Server Error)} if the churchMember couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/church-members/{id}")
    public ResponseEntity<ChurchMember> updateChurchMember(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ChurchMember churchMember
    ) throws URISyntaxException {
        log.debug("REST request to update ChurchMember : {}, {}", id, churchMember);
        if (churchMember.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, churchMember.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!churchMemberRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ChurchMember result = churchMemberRepository.save(churchMember);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, churchMember.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /church-members/:id} : Partial updates given fields of an existing churchMember, field will ignore if it is null
     *
     * @param id the id of the churchMember to save.
     * @param churchMember the churchMember to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated churchMember,
     * or with status {@code 400 (Bad Request)} if the churchMember is not valid,
     * or with status {@code 404 (Not Found)} if the churchMember is not found,
     * or with status {@code 500 (Internal Server Error)} if the churchMember couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/church-members/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ChurchMember> partialUpdateChurchMember(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ChurchMember churchMember
    ) throws URISyntaxException {
        log.debug("REST request to partial update ChurchMember partially : {}, {}", id, churchMember);
        if (churchMember.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, churchMember.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!churchMemberRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ChurchMember> result = churchMemberRepository
            .findById(churchMember.getId())
            .map(existingChurchMember -> {
                if (churchMember.getFullName() != null) {
                    existingChurchMember.setFullName(churchMember.getFullName());
                }
                if (churchMember.getPhoneNumber() != null) {
                    existingChurchMember.setPhoneNumber(churchMember.getPhoneNumber());
                }
                if (churchMember.getDate() != null) {
                    existingChurchMember.setDate(churchMember.getDate());
                }
                if (churchMember.getStartDate() != null) {
                    existingChurchMember.setStartDate(churchMember.getStartDate());
                }
                if (churchMember.getEndDate() != null) {
                    existingChurchMember.setEndDate(churchMember.getEndDate());
                }

                return existingChurchMember;
            })
            .map(churchMemberRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, churchMember.getId().toString())
        );
    }

    /**
     * {@code GET  /church-members} : get all the churchMembers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of churchMembers in body.
     */
    @GetMapping("/church-members")
    public List<ChurchMember> getAllChurchMembers() {
        log.debug("REST request to get all ChurchMembers");
        return churchMemberRepository.findAll();
    }

    /**
     * {@code GET  /church-members/:id} : get the "id" churchMember.
     *
     * @param id the id of the churchMember to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the churchMember, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/church-members/{id}")
    public ResponseEntity<ChurchMember> getChurchMember(@PathVariable Long id) {
        log.debug("REST request to get ChurchMember : {}", id);
        Optional<ChurchMember> churchMember = churchMemberRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(churchMember);
    }

    /**
     * {@code DELETE  /church-members/:id} : delete the "id" churchMember.
     *
     * @param id the id of the churchMember to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/church-members/{id}")
    public ResponseEntity<Void> deleteChurchMember(@PathVariable Long id) {
        log.debug("REST request to delete ChurchMember : {}", id);
        churchMemberRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
