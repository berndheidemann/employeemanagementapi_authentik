package de.szut.employee_administration_backend.controller.service;

import de.szut.employee_administration_backend.controller.service.model.EmployeeEntity;
import de.szut.employee_administration_backend.controller.service.model.QualificationEntity;
import de.szut.employee_administration_backend.controller.service.repository.QualificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class QualificationService {
    private final QualificationRepository repository;

    public QualificationService(QualificationRepository repository) {
        this.repository = repository;
    }

    public QualificationEntity create(QualificationEntity qualification) {
        return this.repository.save(qualification);
    }

    public List<QualificationEntity> readAll() {
        return this.repository.findAll();
    }

    public boolean qualificationExistsBySkill(String designation) {

        return this.repository.existsBySkill(designation);
    }

    public QualificationEntity readByDesignation(String designation) {
        return this.repository.findBySkill(designation);
    }

    public void delete(long id) {
        this.repository.deleteById(id);
    }

    public boolean qualificationExistsById(long id) {
        return this.repository.existsById(id);
    }

    public Set<EmployeeEntity> readAllEmployeesByQualification(long id) {
        var opt = this.repository.findById(id);
        if (opt.isPresent()) {
            return opt.get().getEmployees();
        } else {
            throw new IllegalArgumentException("Qualification with id " + id + " does not exist");
        }
    }

    public Optional<QualificationEntity> readById(long id) {
        return this.repository.findById(id);
    }

    public QualificationEntity update(QualificationEntity qualification) {
        var opt = this.repository.findById(qualification.getId());
        if (opt.isPresent()) {
            var oldQualification = opt.get();
            oldQualification.setSkill(qualification.getSkill());
            return this.repository.save(oldQualification);
        } else {
            throw new IllegalArgumentException("Qualification with id " + qualification.getId() + " does not exist");
        }
    }
}
