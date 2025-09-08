package de.szut.employee_administration_backend.controller.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class QualificationPostDTO implements Serializable {
    @NotBlank(message = "skill is mandatory")
    private String skill;


}
