package de.szut.employee_administration_backend.employee;

import de.szut.employee_administration_backend.controller.service.model.EmployeeEntity;
import de.szut.employee_administration_backend.controller.service.model.QualificationEntity;
import de.szut.employee_administration_backend.testcontainers.AbstractIntegrationTest;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests for PUT /employees/{id}
 */
public class EmployeePatchIT extends AbstractIntegrationTest {

    @Test
    public void authorization() throws Exception {
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
    public void happyPathWithoutSkillset() throws Exception {
        EmployeeEntity e1 = new EmployeeEntity();
        e1.setLastName("Meier");
        e1.setFirstName("Max");
        e1.setStreet("Hauptstraße");
        e1.setPostcode("26125");
        e1.setCity("Oldenburg");
        e1.setPhone("+4912345567");
        e1 = employeeRepository.save(e1);
        QualificationEntity q1 = new QualificationEntity();
        q1.setSkill("Java");
        q1 = qualificationRepository.save(q1);

        e1.addQualification(q1);
        e1 = employeeRepository.save(e1);

        String content = """
                {
                    "lastName": "MeierPUT",
                    "firstName": "MaxPUT",
                    "postcode": "26120",
                    "phone": "+4912345567000"
                }
                """;

        final var contentAsString = this.mockMvc.perform(patch("/employees/" + e1.getId()).content(content).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("lastName", is("MeierPUT")))
                .andExpect(jsonPath("firstName", is("MaxPUT")))
                .andExpect(jsonPath("street", is("Hauptstraße")))
                .andExpect(jsonPath("postcode", is("26120")))
                .andExpect(jsonPath("city", is("Oldenburg")))
                .andExpect(jsonPath("phone", is("+4912345567000")))
                .andReturn().getResponse().getContentAsString();

        final var jsonObject = new JSONObject(contentAsString);
        final var id = jsonObject.getLong("id");

        final var employeeEntity = this.employeeRepository.findById(id).orElseThrow();
        assertThat(employeeEntity.getLastName()).isEqualTo("MeierPUT");
        assertThat(employeeEntity.getFirstName()).isEqualTo("MaxPUT");
        assertThat(employeeEntity.getStreet()).isEqualTo("Hauptstraße");
        assertThat(employeeEntity.getPostcode()).isEqualTo("26120");
        assertThat(employeeEntity.getCity()).isEqualTo("Oldenburg");
        assertThat(employeeEntity.getPhone()).isEqualTo("+4912345567000");
        assertThat(employeeEntity.getSkills()).hasSize(1);

    }

    @Test
    @WithMockUser(roles = "user")
    public void happyPathWithSkillset() throws Exception {
        EmployeeEntity e1 = new EmployeeEntity();
        e1.setLastName("Meier");
        e1.setFirstName("Max");
        e1.setStreet("Hauptstraße");
        e1.setPostcode("26125");
        e1.setCity("Oldenburg");
        e1.setPhone("+4912345567");
        QualificationEntity q1 = new QualificationEntity();
        q1.setSkill("Java");
        q1 = qualificationRepository.save(q1);
        e1.addQualification(q1);

        QualificationEntity q2 = new QualificationEntity();
        q2.setSkill("C++");
        q2 = qualificationRepository.save(q2);
        e1.addQualification(q2);
        e1 = employeeRepository.save(e1);

        QualificationEntity q3 = new QualificationEntity();
        q3.setSkill("RUST");
        q3 = qualificationRepository.save(q3);

        QualificationEntity q4 = new QualificationEntity();
        q4.setSkill("Python");
        q4 = qualificationRepository.save(q4);

        String content = """
                {
                    "lastName": "MeierPUT",
                    "skillSet": [
                        %s,%s,%s
                    ]
                }
                """.formatted(q1.getId(), q3.getId(), q4.getId());

        final var contentAsString = this.mockMvc.perform(patch("/employees/" + e1.getId()).content(content).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("lastName", is("MeierPUT")))
                .andExpect(jsonPath("firstName", is("Max")))
                .andExpect(jsonPath("street", is("Hauptstraße")))
                .andExpect(jsonPath("postcode", is("26125")))
                .andExpect(jsonPath("city", is("Oldenburg")))
                .andExpect(jsonPath("phone", is("+4912345567")))
                .andExpect(jsonPath("skillSet", hasSize(3)))
                .andReturn().getResponse().getContentAsString();


        final var jsonObject = new JSONObject(contentAsString);
        final var id = jsonObject.getLong("id");

        final EmployeeEntity employeeEntity = this.employeeRepository.findById(id).orElseThrow();
        assertThat(employeeEntity.getLastName()).isEqualTo("MeierPUT");
        assertThat(employeeEntity.getFirstName()).isEqualTo("Max");
        assertThat(employeeEntity.getStreet()).isEqualTo("Hauptstraße");
        assertThat(employeeEntity.getPostcode()).isEqualTo("26125");
        assertThat(employeeEntity.getCity()).isEqualTo("Oldenburg");
        assertThat(employeeEntity.getPhone()).isEqualTo("+4912345567");
        assertThat(employeeEntity.getSkills()).hasSize(3);
        assertThat(employeeEntity.getSkills()).contains(q1);
        assertThat(employeeEntity.getSkills()).contains(q3);
        assertThat(employeeEntity.getSkills()).contains(q4);
    }
}

