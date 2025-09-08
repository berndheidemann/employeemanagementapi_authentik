package de.szut.employee_administration_backend.qualification;

import de.szut.employee_administration_backend.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class QualificationPostIT extends AbstractIntegrationTest {


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
        String content = """
                    {
                        "skill": "Java"
                    }
                """;

        this.mockMvc.perform(post("/qualifications").content(content).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("skill", is("Java")));


        final var loadedEntity = qualificationRepository.findBySkill("Java");
        assertThat(loadedEntity.getSkill()).isEqualTo("Java");

    }

    @Test
    @WithMockUser(roles = "user")
    void emptySkill() throws Exception {
        String content = """
                    {
                        "skill": ""
                    }
                """;
        this.mockMvc.perform(post("/qualifications").content(content).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());


    }

    @Test
    @WithMockUser(roles = "user")
    void noSkill() throws Exception {
        String content = """
                    {
                
                    }
                """;
        this.mockMvc.perform(post("/qualifications").content(content).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());


    }


}
