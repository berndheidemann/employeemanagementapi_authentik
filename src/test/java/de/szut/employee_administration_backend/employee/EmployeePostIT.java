package de.szut.employee_administration_backend.employee;

import de.szut.employee_administration_backend.controller.service.model.QualificationEntity;
import de.szut.employee_administration_backend.testcontainers.AbstractIntegrationTest;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EmployeePostIT extends AbstractIntegrationTest {


    @Test
    void authorization() throws Exception {
        String content = """
                    {
                        "lastName": "Meier",
                        "firstName": "Max",
                        "street": "Hauptstraße",
                        "postcode": "26125",
                        "phone": "+4912345567"
                    }
                """;
        final var contentAsString = this.mockMvc.perform(post("/employees/").content(content).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "user")
    void happyPath() throws Exception {
        String content = """
                    {
                        "lastName": "Meier",
                        "firstName": "Max",
                        "street": "Hauptstraße",
                        "postcode": "26125",
                        "city": "Oldenburg",
                        "phone": "+4912345567"
                    }
                """;

        final var contentAsString = this.mockMvc.perform(post("/employees").content(content).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("lastName", is("Meier")))
                .andExpect(jsonPath("firstName", is("Max")))
                .andExpect(jsonPath("street", is("Hauptstraße")))
                .andExpect(jsonPath("postcode", is("26125")))
                .andExpect(jsonPath("city", is("Oldenburg")))
                .andExpect(jsonPath("phone", is("+4912345567")))
                .andReturn()
                .getResponse()
                .getContentAsString();

        final var id = Long.parseLong(new JSONObject(contentAsString).get("id").toString());

        final var loadedEntity = employeeRepository.findById(id);
        assertThat(loadedEntity.get().getFirstName()).isEqualTo("Max");
        assertThat(loadedEntity.get().getLastName()).isEqualTo("Meier");
        assertThat(loadedEntity.get().getStreet()).isEqualTo("Hauptstraße");
        assertThat(loadedEntity.get().getPostcode()).isEqualTo("26125");
        assertThat(loadedEntity.get().getCity()).isEqualTo("Oldenburg");
        assertThat(loadedEntity.get().getPhone()).isEqualTo("+4912345567");
    }

    @Test
    @WithMockUser(roles = "user")
    void skillDontExist() throws Exception {
        String content = """
                    {
                        "lastName": "Meier",
                        "firstName": "Max",
                        "street": "Hauptstraße",
                        "postcode": "26125",
                        "city": "Oldenburg",
                        "phone": "+4912345567"
                        "skills": [ "Java", "Angular" ]
                
                    }
                """;


        final var contentAsString = this.mockMvc.perform(post("/employees/").content(content).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());

    }

    @Test
    @WithMockUser(roles = "user")
    void happyPathWithSkill() throws Exception {
        QualificationEntity q1 = new QualificationEntity();
        q1.setSkill("Java");
        q1 = qualificationRepository.save(q1);
        QualificationEntity q2 = new QualificationEntity();
        q2.setSkill("Angular");
        q2 = qualificationRepository.save(q2);

        String content = """
                    {
                        "lastName": "Meier",
                        "firstName": "Max",
                        "street": "Hauptstraße",
                        "postcode": "26125",
                        "city": "Oldenburg",
                        "phone": "+4912345567",
                        "skillSet": [ %s, %s ]
                    }
                """.formatted(q1.getId(), q2.getId());

        final var contentAsString = this.mockMvc.perform(post("/employees").content(content).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("lastName", is("Meier")))
                .andExpect(jsonPath("firstName", is("Max")))
                .andExpect(jsonPath("street", is("Hauptstraße")))
                .andExpect(jsonPath("postcode", is("26125")))
                .andExpect(jsonPath("city", is("Oldenburg")))
                .andExpect(jsonPath("phone", is("+4912345567")))
                .andExpect(jsonPath("$.skillSet", hasSize(2)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        final var id = Long.parseLong(new JSONObject(contentAsString).get("id").toString());

        final var loadedEntity = employeeRepository.findById(id);
        assertThat(loadedEntity.get().getFirstName()).isEqualTo("Max");
        assertThat(loadedEntity.get().getLastName()).isEqualTo("Meier");
        assertThat(loadedEntity.get().getStreet()).isEqualTo("Hauptstraße");
        assertThat(loadedEntity.get().getPostcode()).isEqualTo("26125");
        assertThat(loadedEntity.get().getCity()).isEqualTo("Oldenburg");
        assertThat(loadedEntity.get().getPhone()).isEqualTo("+4912345567");
        assertThat(loadedEntity.get().getSkills().size()).isEqualTo(2);
    }
}
