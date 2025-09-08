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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EmployeeUpdateIT extends AbstractIntegrationTest {


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
        final var contentAsString = this.mockMvc.perform(put("/employees/1").content(content).contentType(MediaType.APPLICATION_JSON))
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
        e1.setPhone("+491234567");
        e1 = this.employeeRepository.save(e1);

        String content = """
                    {
                        "lastName": "Meierr",
                        "firstName": "Maxx",
                        "street": "Hauptstraßee",
                        "postcode": "11111",
                        "city": "Oldenburgg",
                        "phone": "+49123455677"
                    }
                """;


        final var contentAsString = this.mockMvc.perform(put("/employees/" + e1.getId()).content(content).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("lastName", is("Meierr")))
                .andExpect(jsonPath("firstName", is("Maxx")))
                .andExpect(jsonPath("street", is("Hauptstraßee")))
                .andExpect(jsonPath("postcode", is("11111")))
                .andExpect(jsonPath("city", is("Oldenburgg")))
                .andExpect(jsonPath("phone", is("+49123455677")))
                .andReturn()
                .getResponse()
                .getContentAsString();


        final var loadedEntity = employeeRepository.findById(e1.getId());
        assertThat(loadedEntity.get().getFirstName()).isEqualTo("Maxx");
        assertThat(loadedEntity.get().getLastName()).isEqualTo("Meierr");
        assertThat(loadedEntity.get().getStreet()).isEqualTo("Hauptstraßee");
        assertThat(loadedEntity.get().getPostcode()).isEqualTo("11111");
        assertThat(loadedEntity.get().getCity()).isEqualTo("Oldenburgg");
        assertThat(loadedEntity.get().getPhone()).isEqualTo("+49123455677");
    }

    @Test
    @WithMockUser(roles = "user")
    void notExists() throws Exception {
        String content = """
                    {
                        "lastName": "Meierr",
                        "firstName": "Maxx",
                        "street": "Hauptstraßee",
                        "postcode": "11111",
                        "city": "Oldenburgg",
                        "phone": "+49123455677"
                    }
                """;

        final var contentAsString = this.mockMvc.perform(

                        put("/employees/1")
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
