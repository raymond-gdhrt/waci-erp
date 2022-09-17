package com.waci.erp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.waci.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ChurchMemberTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChurchMember.class);
        ChurchMember churchMember1 = new ChurchMember();
        churchMember1.setId(1L);
        ChurchMember churchMember2 = new ChurchMember();
        churchMember2.setId(churchMember1.getId());
        assertThat(churchMember1).isEqualTo(churchMember2);
        churchMember2.setId(2L);
        assertThat(churchMember1).isNotEqualTo(churchMember2);
        churchMember1.setId(null);
        assertThat(churchMember1).isNotEqualTo(churchMember2);
    }
}
