package com.waci.erp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.waci.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PledgeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pledge.class);
        Pledge pledge1 = new Pledge();
        pledge1.setId(1L);
        Pledge pledge2 = new Pledge();
        pledge2.setId(pledge1.getId());
        assertThat(pledge1).isEqualTo(pledge2);
        pledge2.setId(2L);
        assertThat(pledge1).isNotEqualTo(pledge2);
        pledge1.setId(null);
        assertThat(pledge1).isNotEqualTo(pledge2);
    }
}
