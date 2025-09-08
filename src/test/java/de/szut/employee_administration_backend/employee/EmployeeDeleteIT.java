package de.szut.employee_administration_backend.employee;

import de.szut.employee_administration_backend.controller.service.model.EmployeeEntity;
import de.szut.employee_administration_backend.controller.service.model.QualificationEntity;
import de.szut.employee_administration_backend.testcontainers.AbstractIntegrationTest;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EmployeeDeleteIT extends AbstractIntegrationTest {


    @Test
    void authorization() throws Exception {

        final var contentAsString = this.mockMvc.perform(delete("/employees/1"))
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

        final var contentAsString = this.mockMvc.perform(delete("/employees/" + e1.getId()))
                .andExpect(status().is2xxSuccessful());

        final var loadedEntity = employeeRepository.findById(e1.getId());
        assertThat(loadedEntity.isPresent()).isEqualTo(false);
    }

    @Test
    @WithMockUser(roles = "user")
    void doesNotExist() throws Exception {


        final var contentAsString = this.mockMvc.perform(delete("/employees/1"))
                .andExpect(status().isNotFound());


    }


}
