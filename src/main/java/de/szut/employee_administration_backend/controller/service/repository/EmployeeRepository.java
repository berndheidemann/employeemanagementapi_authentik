package de.szut.employee_administration_backend.controller.service.repository;

import de.szut.employee_administration_backend.controller.service.model.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {
    boolean existsByLastNameAndFirstNameAndStreet(String lastname, String firstname, String street);

}
