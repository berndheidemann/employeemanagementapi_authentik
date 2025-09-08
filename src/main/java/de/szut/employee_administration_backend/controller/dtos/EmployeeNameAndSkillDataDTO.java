package de.szut.employee_administration_backend.controller.dtos;

import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
public class EmployeeNameAndSkillDataDTO implements Serializable {
    private Long id;
    private String lastName;
    private String firstName;
    private Set<QualificationGetDTO> skillSet = new HashSet<>();

}
