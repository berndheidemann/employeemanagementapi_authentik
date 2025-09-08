package de.szut.employee_administration_backend.employee;

import de.szut.employee_administration_backend.controller.service.model.EmployeeEntity;
import de.szut.employee_administration_backend.controller.service.model.QualificationEntity;
import de.szut.employee_administration_backend.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EmployeeFindAllQualificationsOfEmployeeIT extends AbstractIntegrationTest {

    @Test
    void authorization() throws Exception {
        this.mockMvc.perform(get("/employees/1/qualifications"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "user")
    void findAll() throws Exception {

        var e1 = new EmployeeEntity();
        e1.setFirstName("Max");
        e1.setLastName("Meier");
        e1.setStreet("Hauptstraße");
        e1.setPostcode("12345");
        e1.setCity("Bremen");
        e1.setPhone("+4912345");

        e1 = this.employeeRepository.save(e1);

        QualificationEntity q1 = new QualificationEntity();
        q1.setSkill("Java");
        this.qualificationRepository.save(q1);

        e1.addQualification(q1);
        e1 = this.employeeRepository.save(e1);


        final var contentAsString = this.mockMvc.perform(get("/employees/" + e1.getId() + "/qualifications"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("lastName", is("Meier")))
                .andExpect(jsonPath("firstName", is("Max")))
                .andExpect(jsonPath("$.skillSet", hasSize(1)))
                .andExpect(jsonPath("$.skillSet[0].skill", is("Java")));

    }

    @Test
    @WithMockUser(roles = "user")
    void employeeDoesntExist() throws Exception {

        String content = """
                    {
                        "skill": "Java"
                    }
                """;

        QualificationEntity q1 = new QualificationEntity();
        q1.setSkill("Java");
        this.qualificationRepository.save(q1);

        final var contentAsString = this.mockMvc.perform(post("/employees/1/qualifications").content(content).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }
}
