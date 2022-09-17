package com.waci.erp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.waci.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PledgePaymentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PledgePayment.class);
        PledgePayment pledgePayment1 = new PledgePayment();
        pledgePayment1.setId(1L);
        PledgePayment pledgePayment2 = new PledgePayment();
        pledgePayment2.setId(pledgePayment1.getId());
        assertThat(pledgePayment1).isEqualTo(pledgePayment2);
        pledgePayment2.setId(2L);
        assertThat(pledgePayment1).isNotEqualTo(pledgePayment2);
        pledgePayment1.setId(null);
        assertThat(pledgePayment1).isNotEqualTo(pledgePayment2);
    }
}
