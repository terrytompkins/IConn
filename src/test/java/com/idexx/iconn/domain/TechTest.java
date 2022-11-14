package com.idexx.iconn.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.idexx.iconn.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TechTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tech.class);
        Tech tech1 = new Tech();
        tech1.setId(1L);
        Tech tech2 = new Tech();
        tech2.setId(tech1.getId());
        assertThat(tech1).isEqualTo(tech2);
        tech2.setId(2L);
        assertThat(tech1).isNotEqualTo(tech2);
        tech1.setId(null);
        assertThat(tech1).isNotEqualTo(tech2);
    }
}
