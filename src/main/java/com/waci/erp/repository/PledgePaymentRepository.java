package com.waci.erp.repository;

import com.waci.erp.domain.PledgePayment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PledgePayment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PledgePaymentRepository extends JpaRepository<PledgePayment, Long> {}
