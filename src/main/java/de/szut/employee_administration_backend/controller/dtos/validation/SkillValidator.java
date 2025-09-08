package de.szut.employee_administration_backend.controller.dtos.validation;


import de.szut.employee_administration_backend.controller.service.repository.QualificationRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Configurable;

@Configurable
public class SkillValidator implements
        ConstraintValidator<SkillExistConstraint, long[]> {

    private final QualificationRepository qualificationRepository;

    public SkillValidator(QualificationRepository qualificationRepository) {
        this.qualificationRepository = qualificationRepository;
    }

    @Override
    public void initialize(SkillExistConstraint constraint) {
    }

    @Override
    public boolean isValid(long[] skills, ConstraintValidatorContext constraintValidatorContext) {
        if (skills == null) {
            return true;
        }
        for (long id : skills) {
            if (!this.qualificationRepository.existsById(id)) {
                return false;
            }
        }
        return true;
    }


}
