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
@Entity(name = "Employee")
public class EmployeeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employee_generator")
    @SequenceGenerator(name = "employee_generator", sequenceName = "employee_id_seq", allocationSize = 1)
    private long id;

    @Column(name = "last_name")
    private String lastName;
    @Column(name = "first_name")
    private String firstName;
    private String street;
    @Column(name = "zip")
    private String postcode;
    private String city;
    private String phone;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "employee_qualification", joinColumns = @JoinColumn(name = "e_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "q_id", referencedColumnName = "id"))
    private Set<QualificationEntity> skills = new HashSet<>();

    public void addQualification(QualificationEntity newQualification) {
        this.skills.add(newQualification);
    }

    public void removeQualification(QualificationEntity qualification) {
        this.skills.remove(qualification);
    }

    public boolean hasQualification(QualificationEntity qualification) {
        return this.skills.contains(qualification);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeEntity employee = (EmployeeEntity) o;
        return Objects.equals(this.lastName, employee.lastName)
                && Objects.equals(this.firstName, employee.firstName)
                && Objects.equals(this.street, employee.street)
                && Objects.equals(this.postcode, employee.postcode)
                && Objects.equals(this.city, employee.city)
                && Objects.equals(this.phone, employee.phone);
    }
}

