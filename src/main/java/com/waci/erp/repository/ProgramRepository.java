package com.waci.erp.repository;

import com.waci.erp.domain.Program;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Program entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProgramRepository extends JpaRepository<Program, Long> {}
