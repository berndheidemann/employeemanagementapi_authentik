package de.szut.employee_administration_backend.employee;

import de.szut.employee_administration_backend.controller.service.model.EmployeeEntity;
import de.szut.employee_administration_backend.controller.service.model.QualificationEntity;
import de.szut.employee_administration_backend.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EmployeeDeleteQualificationsOfEmployeeIT extends AbstractIntegrationTest {

    @Test
    void authorization() throws Exception {
        this.mockMvc.perform(delete("/employees/1/qualifications"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "user")
    void happyPath() throws Exception {

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
        q1 = this.qualificationRepository.save(q1);

        e1.addQualification(q1);
        e1 = this.employeeRepository.save(e1);


        final var contentAsString = this.mockMvc.perform(delete("/employees/" + e1.getId() + "/qualifications/" + q1.getId()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("lastName", is("Meier")))
                .andExpect(jsonPath("firstName", is("Max")))
                .andExpect(jsonPath("$.skillSet", hasSize(0)));

    }

    @Test
    @WithMockUser(roles = "user")
    void employeeDoesntExist() throws Exception {


        QualificationEntity q1 = new QualificationEntity();
        q1.setSkill("Java");
        q1 = this.qualificationRepository.save(q1);


        this.mockMvc.perform(delete("/employees/1/qualifications/" + q1.getId()))
                .andExpect(status().isNotFound());

    }
}
