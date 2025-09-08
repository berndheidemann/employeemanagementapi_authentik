package de.szut.employee_administration_backend.config;

import de.szut.employee_administration_backend.controller.service.model.EmployeeEntity;
import de.szut.employee_administration_backend.controller.service.model.QualificationEntity;
import de.szut.employee_administration_backend.controller.service.repository.EmployeeRepository;
import de.szut.employee_administration_backend.controller.service.repository.QualificationRepository;
import org.springframework.stereotype.Component;

@Component
public class InitialDataCreation {

    private EmployeeRepository employeeRepository;
    private QualificationRepository qualificationRepository;


    public InitialDataCreation(EmployeeRepository repository, QualificationRepository qualificationRepository) {
        this.employeeRepository = repository;
        this.qualificationRepository = qualificationRepository;


        if (qualificationRepository.count() < 2) {
            QualificationEntity q = new QualificationEntity();
            q.setSkill("Java");
            qualificationRepository.save(q);
            QualificationEntity q2 = new QualificationEntity();
            q2.setSkill("Angular");
            qualificationRepository.save(q2);
        }

        if (repository.count() < 2) {
            EmployeeEntity e = new EmployeeEntity();
            e.setCity("Bremen");
            e.setFirstName("Max");
            e.setLastName("Mustermann");
            e.setPhone("+16209218323");
            e.setPostcode("27823");
            e.setStreet("Meta-Sattler-Straße 33");
            e.addQualification(this.qualificationRepository.findBySkill("Java"));
            EmployeeEntity e2 = new EmployeeEntity();
            e2.setCity("Bremen");
            e2.setFirstName("Susanne");
            e2.setLastName("Musterfrau");
            e2.setPhone("+17292187323");
            e2.setPostcode("27823");
            e2.setStreet("Bahnhofstraße 33");
            e2.addQualification(this.qualificationRepository.findBySkill("Angular"));
            e2.addQualification(this.qualificationRepository.findBySkill("Java"));
            repository.save(e);
            repository.save(e2);
        }
    }
}
