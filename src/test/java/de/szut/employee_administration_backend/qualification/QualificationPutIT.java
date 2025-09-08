package de.szut.employee_administration_backend.qualification;

import de.szut.employee_administration_backend.controller.service.model.QualificationEntity;
import de.szut.employee_administration_backend.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
    Test Endpoint for PUT /qualifications/{id}
 */
public class QualificationPutIT extends AbstractIntegrationTest {

    @Test
    void authorization() throws Exception {
        String content = """
                    {
                        "skill": "Java"
                    }
                """;
        this.mockMvc.perform(post("/qualifications/").content(content).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "user")
    void happyPath() throws Exception {

        QualificationEntity qualificationEntity = new QualificationEntity();
        qualificationEntity.setSkill("Java");
        qualificationEntity = qualificationRepository.save(qualificationEntity);

        String content = """
                    {
                        "skill": "C++"
                    }
                """;

        this.mockMvc.perform(put("/qualifications/" + qualificationEntity.getId()).content(content).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("skill", is("C++")));
        var opt = qualificationRepository.findById(qualificationEntity.getId());
        if (opt.isPresent()) {
            assertThat(opt.get().getSkill()).isEqualTo("C++");
        } else {
            throw new RuntimeException("Qualification not found");
        }
    }
}
