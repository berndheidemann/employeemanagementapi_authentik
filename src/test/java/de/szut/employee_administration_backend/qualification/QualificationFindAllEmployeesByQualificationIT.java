package de.szut.employee_administration_backend.qualification;

import de.szut.employee_administration_backend.controller.service.model.EmployeeEntity;
import de.szut.employee_administration_backend.controller.service.model.QualificationEntity;
import de.szut.employee_administration_backend.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class QualificationFindAllEmployeesByQualificationIT extends AbstractIntegrationTest {

    @Test
    void authorization() throws Exception {
        this.mockMvc.perform(get("/qualifications/foo/employees"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "user")
    void happyPath() throws Exception {
        QualificationEntity q = new QualificationEntity();
        q.setSkill("Foo");
        q = qualificationRepository.save(q);
        EmployeeEntity e = new EmployeeEntity();
        e.setLastName("Meier");
        e.setFirstName("Helmut");
        e = employeeRepository.save(e);
        e.addQualification(q);
        e = employeeRepository.save(e);

        var id = e.getId();

        this.mockMvc.perform(get("/qualifications/"+q.getId()+"/employees"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("qualification.skill", is("Foo")))
                .andExpect(jsonPath("employees", hasSize(1)))
                .andExpect(jsonPath("employees[0].firstName", is("Helmut")))
                .andExpect(jsonPath("employees[0].lastName", is("Meier")));
    }

    @Test
    @WithMockUser(roles = "user")
    void skillDoesntExist() throws Exception {

        this.mockMvc.perform(get("/qualifications/12/employees"))
                .andExpect(status().isNotFound());

    }
}
