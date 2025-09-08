package de.szut.employee_administration_backend.controller.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class EmployeesForAQualificationDTO implements Serializable {

    private QualificationGetDTO qualification;
    private Set<EmployeeNameDataDTO> employees = new HashSet<>();

    public void addEmployee(EmployeeNameDataDTO dto){

        this.employees.add(dto);
    }

}
