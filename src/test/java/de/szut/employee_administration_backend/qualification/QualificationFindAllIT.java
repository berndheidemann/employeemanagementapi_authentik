package de.szut.employee_administration_backend.qualification;

import de.szut.employee_administration_backend.controller.service.model.QualificationEntity;
import de.szut.employee_administration_backend.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class QualificationFindAllIT extends AbstractIntegrationTest {

    @Test
    void authorization() throws Exception {
        this.mockMvc.perform(get("/qualifications/"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser()
    void findAll() throws Exception {

        var q1 = new QualificationEntity();
        q1.setSkill("Foo");
        var q2 = new QualificationEntity();
        q2.setSkill("Bar");

        qualificationRepository.save(q1);
        qualificationRepository.save(q2);


        final var contentAsString = this.mockMvc.perform(get("/qualifications"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].skill", is("Foo")))
                .andExpect(jsonPath("$[1].skill", is("Bar")));
    }
}
