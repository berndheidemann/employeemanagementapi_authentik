package de.szut.employee_administration_backend.controller.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
public class EmployeeResponseDTO implements Serializable {

    private long id;

    @NotNull(message = "Lastname is mandatory")
    private String lastName;
    @NotNull(message = "Firstname is mandatory")
    private String firstName;
    @NotNull(message = "Street is mandatory")
    private String street;
    @NotBlank(message = "Postcode is mandatory")
    @Size(min = 5, max = 5, message = "Postcode must have 5 characters")
    private String postcode;
    @NotNull(message = "city is mandatory")
    private String city;
    @NotNull(message = "Phonenumber is mandatory")
    private String phone;

    private QualificationGetDTO[] skillSet;
}
