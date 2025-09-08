package de.szut.employee_administration_backend.controller.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class QualificationGetDTO implements Serializable {
    private String skill;
    private long id;
}
