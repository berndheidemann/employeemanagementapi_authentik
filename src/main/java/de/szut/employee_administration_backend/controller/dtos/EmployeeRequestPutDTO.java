package de.szut.employee_administration_backend.controller.dtos;

import de.szut.employee_administration_backend.controller.dtos.validation.SkillExistConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmployeeRequestPutDTO {

        private String lastName;
        private String firstName;
        private String street;
        private String postcode;
        private String city;
        private String phone;

        @SkillExistConstraint
        private long[] skillSet;
}
