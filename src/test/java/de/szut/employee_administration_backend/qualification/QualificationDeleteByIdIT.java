package de.szut.employee_administration_backend.qualification;

import de.szut.employee_administration_backend.controller.service.model.QualificationEntity;
import de.szut.employee_administration_backend.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class QualificationDeleteByIdIT extends AbstractIntegrationTest {


    @Test
    void authorization() throws Exception {

        String content = """
                {
                    "skill": "Foo"
                 }
                """;

        this.mockMvc.perform(delete("/qualifications").content(content).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "user")
    void happyPath() throws Exception {
        QualificationEntity toStore = new QualificationEntity();
        toStore.setSkill("Foo");
        QualificationEntity stored = qualificationRepository.save(toStore);


        this.mockMvc.perform(
                        delete("/qualifications/" + stored.getId()))
                .andExpect(status().isNoContent());
        assertNull(qualificationRepository.findBySkill(stored.getSkill()));
        assertNull(qualificationRepository.findById(stored.getId()).orElse(null));
    }

    @Test
    @WithMockUser(roles = "user")
    void skillDoesntExist() throws Exception {


        this.mockMvc.perform(
                        delete("/qualifications/11"))
                .andExpect(status().isNotFound());
    }


}
