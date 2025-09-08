package de.szut.employee_administration_backend.controller.service.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class QualificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "qualification_generator")
    @SequenceGenerator(name = "qualification_generator", sequenceName = "qualification_id_seq", allocationSize = 1)
    private long id;

    @Column(unique = true)
    private String skill;

    @ManyToMany(mappedBy = "skills", cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private Set<EmployeeEntity> employees = new HashSet<>();

    public void addEmployee(EmployeeEntity employeeEntity) {
        this.employees.add(employeeEntity);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QualificationEntity qualification = (QualificationEntity) o;
        return Objects.equals(this.skill, qualification.skill);
    }

}
