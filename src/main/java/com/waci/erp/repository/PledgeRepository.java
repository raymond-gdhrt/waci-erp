package com.waci.erp.repository;

import com.waci.erp.domain.Pledge;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Pledge entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PledgeRepository extends JpaRepository<Pledge, Long> {}
