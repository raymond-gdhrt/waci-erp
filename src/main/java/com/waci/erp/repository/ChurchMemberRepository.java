package com.waci.erp.repository;

import com.waci.erp.domain.ChurchMember;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ChurchMember entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChurchMemberRepository extends JpaRepository<ChurchMember, Long> {}
