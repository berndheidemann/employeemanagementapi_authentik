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

public class EmployeeFindAllIT extends AbstractIntegrationTest {

    @Test
    void authorization() throws Exception {
        this.mockMvc.perform(get("/employees/"))
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

        this.employeeRepository.save(e1);

        final var contentAsString = this.mockMvc.perform(get("/employees"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName", is("Max")))
                .andExpect(jsonPath("$[0].lastName", is("Meier")))
                .andExpect(jsonPath("$[0].street", is("Hauptstraße")))
                .andExpect(jsonPath("$[0].postcode", is("12345")))
                .andExpect(jsonPath("$[0].city", is("Bremen")))
                .andExpect(jsonPath("$[0].phone", is("+4912345")));

    }
}
