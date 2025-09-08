package de.szut.employee_administration_backend.employee;

import de.szut.employee_administration_backend.controller.service.model.EmployeeEntity;
import de.szut.employee_administration_backend.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EmployeeFindByIDIT extends AbstractIntegrationTest {

    @Test
    void authorization() throws Exception {
        this.mockMvc.perform(get("/employees/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "user")
    void findById() throws Exception {

        var e1 = new EmployeeEntity();
        e1.setFirstName("Max");
        e1.setLastName("Meier");
        e1.setStreet("Hauptstraße");
        e1.setPostcode("12345");
        e1.setCity("Bremen");
        e1.setPhone("+4912345");

        e1 = this.employeeRepository.save(e1);

        final var contentAsString = this.mockMvc.perform(get("/employees/" + e1.getId()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("firstName", is("Max")))
                .andExpect(jsonPath("lastName", is("Meier")))
                .andExpect(jsonPath("street", is("Hauptstraße")))
                .andExpect(jsonPath("postcode", is("12345")))
                .andExpect(jsonPath("city", is("Bremen")))
                .andExpect(jsonPath("phone", is("+4912345")));

    }

    @Test
    @WithMockUser(roles = "user")
    void notExists() throws Exception {
        final var contentAsString = this.mockMvc.perform(get("/employees/1"))
                .andExpect(status().isNotFound());
    }
}
