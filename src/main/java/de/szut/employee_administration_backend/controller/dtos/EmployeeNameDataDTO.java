package de.szut.employee_administration_backend.controller.dtos;

import lombok.Data;

@Data
public class EmployeeNameDataDTO {
    private Long id;
    private String lastName;
    private String firstName;
}
