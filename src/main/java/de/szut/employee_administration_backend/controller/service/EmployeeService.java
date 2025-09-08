package de.szut.employee_administration_backend.controller.service;

import de.szut.employee_administration_backend.controller.exceptionHandling.EmployeeAlreadyExistsException;
import de.szut.employee_administration_backend.controller.exceptionHandling.EmployeeAlreadyHasThisQualificationException;
import de.szut.employee_administration_backend.controller.exceptionHandling.ResourceNotFoundException;
import de.szut.employee_administration_backend.controller.service.model.EmployeeEntity;
import de.szut.employee_administration_backend.controller.service.model.QualificationEntity;
import de.szut.employee_administration_backend.controller.service.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository repository;

    @Autowired
    private QualificationService qualificationService;

    public EmployeeEntity create(EmployeeEntity employee) {
        if (!this.repository.existsByLastNameAndFirstNameAndStreet(employee.getLastName(), employee.getFirstName(), employee.getStreet())) {
            Set<QualificationEntity> skills = employee.getSkills();
            employee.setSkills(null);
            EmployeeEntity e = repository.save(employee);
            e.setSkills(skills);
            return repository.save(e);
        } else {
            throw new EmployeeAlreadyExistsException("EmployeeEntity already exists");
        }
    }

    public EmployeeEntity addQualification(long e_id, QualificationEntity newQualification) {
        EmployeeEntity employee = this.getEmployeeById(e_id);
        newQualification = this.qualificationService.readByDesignation(newQualification.getSkill());
        if (employee.hasQualification(newQualification)) {
            throw new EmployeeAlreadyHasThisQualificationException("EmployeeEntity already has got this qualification!");
        }
        employee.addQualification(newQualification);
        newQualification.addEmployee(employee);
        return this.repository.save(employee);
    }

    public EmployeeEntity removeQualification(Long id, QualificationEntity qualification) {
        EmployeeEntity employee = this.getEmployeeById(id);
        qualification = this.qualificationService.readByDesignation(qualification.getSkill());
        if (!employee.hasQualification(qualification)) {
            throw new ResourceNotFoundException("Employee doesn't have this Qualification!");
        }
        employee.removeQualification(qualification);
        return this.repository.save(employee);
    }

    public List<EmployeeEntity> getAllEmployees() {
        return this.repository.findAll();
    }

    public EmployeeEntity getEmployeeById(long id) {
        Optional<EmployeeEntity> oemployee = this.repository.findById(id);
        if (oemployee.isEmpty()) {
            return null;
        }
        return oemployee.get();
    }

    public boolean employeeExistsById(long id) {
        return repository.existsById(id);
    }

    public EmployeeEntity updateEmployee(EmployeeEntity changes) {
        EmployeeEntity updatedEmployee = getEmployeeById(changes.getId());
        updatedEmployee.setLastName(changes.getLastName());
        updatedEmployee.setFirstName(changes.getFirstName());
        updatedEmployee.setStreet(changes.getStreet());
        updatedEmployee.setCity(changes.getCity());
        updatedEmployee.setPostcode(changes.getPostcode());
        updatedEmployee.setPhone(changes.getPhone());
        updatedEmployee.setSkills(changes.getSkills());
        return repository.save(updatedEmployee);
    }

    public EmployeeEntity patchEmployee(EmployeeEntity changes) {
        EmployeeEntity updatedEmployee = getEmployeeById(changes.getId());
        if (changes.getLastName() != null) {
            updatedEmployee.setLastName(changes.getLastName());
        }
        if (changes.getFirstName() != null) {
            updatedEmployee.setFirstName(changes.getFirstName());
        }
        if (changes.getStreet() != null) {
            updatedEmployee.setStreet(changes.getStreet());
        }
        if (changes.getCity() != null) {
            updatedEmployee.setCity(changes.getCity());
        }
        if (changes.getPostcode() != null) {
            updatedEmployee.setPostcode(changes.getPostcode());
        }
        if (changes.getPhone() != null) {
            updatedEmployee.setPhone(changes.getPhone());
        }
        if (changes.getSkills() != null && !changes.getSkills().isEmpty()) {
            updatedEmployee.setSkills(changes.getSkills());
        }
        
        return repository.save(updatedEmployee);
    }

    public void delete(long id) {
        EmployeeEntity toDelete = getEmployeeById(id);
        repository.deleteById(id);
    }
}
