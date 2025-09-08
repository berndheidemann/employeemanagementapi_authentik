package de.szut.employee_administration_backend.controller.service.repository;

import de.szut.employee_administration_backend.controller.service.model.QualificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QualificationRepository extends JpaRepository<QualificationEntity, Long> {
    QualificationEntity findBySkill(String skill);

    boolean existsBySkill(String skill);
}
