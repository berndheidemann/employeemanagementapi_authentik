package de.szut.employee_administration_backend.testcontainers;


import de.szut.employee_administration_backend.controller.service.repository.EmployeeRepository;
import de.szut.employee_administration_backend.controller.service.repository.QualificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

/**
 * A fast slice test will only start jpa context.
 * <p>
 * To use other context beans use org.springframework.context.annotation.@Import annotation.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("it")
@ContextConfiguration(initializers = PostgresContextInitializer.class)
public class AbstractIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected EmployeeRepository employeeRepository;

    @Autowired
    protected QualificationRepository qualificationRepository;

    @BeforeEach
    void setUp() {
        employeeRepository.deleteAll();
        qualificationRepository.deleteAll();
    }
}
