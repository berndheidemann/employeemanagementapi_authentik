package de.szut.employee_administration_backend.controller.service;

import de.szut.employee_administration_backend.controller.dtos.*;
import de.szut.employee_administration_backend.controller.service.model.EmployeeEntity;
import de.szut.employee_administration_backend.controller.service.model.QualificationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class MappingService {

    @Autowired
    private QualificationService qualificationService;

    public QualificationPostDTO mapQualificationToQualificationPostDTO(QualificationEntity qualification) {
        QualificationPostDTO dto = new QualificationPostDTO();
        dto.setSkill(qualification.getSkill());
        return dto;
    }

    public QualificationGetDTO mapQualificationEntityToQualificationGetDTO(QualificationEntity qualification) {
        var dto = new QualificationGetDTO();
        dto.setSkill(qualification.getSkill());
        dto.setId(qualification.getId());
        return dto;
    }

    public QualificationEntity mapQualificationPostDTOToQualification(QualificationPostDTO dto) {
        QualificationEntity qualification = new QualificationEntity();
        qualification.setSkill(dto.getSkill());
        return qualification;
    }

    public EmployeeNameAndSkillDataDTO mapEmployeeEntityToEmployeeNameAndSkillDTO(EmployeeEntity entity) {
        EmployeeNameAndSkillDataDTO dto = new EmployeeNameAndSkillDataDTO();
        dto.setId(entity.getId());
        dto.setLastName(entity.getLastName());
        dto.setFirstName(entity.getFirstName());
        dto.setSkillSet(mapEntitySetToDTOSet(entity.getSkills()));
        return dto;
    }

    public EmployeeEntity mapEmployeeRequestDTOToEmployeeEntity(EmployeeRequestDTO dto) {
        EmployeeEntity entity = new EmployeeEntity();
        entity.setLastName(dto.getLastName());
        entity.setFirstName(dto.getFirstName());
        entity.setStreet(dto.getStreet());
        entity.setPostcode(dto.getPostcode());
        entity.setCity(dto.getCity());
        entity.setPhone(dto.getPhone());

        if (dto.getSkillSet() != null) {
            for (long id : dto.getSkillSet()) {
                entity.addQualification(this.qualificationService.readById(id).get());
            }
        }

        return entity;
    }

    private Set<QualificationGetDTO> mapEntitySetToDTOSet(Set<QualificationEntity> entitySet) {
        Set<QualificationGetDTO> result = new HashSet<>();
        for (QualificationEntity q : entitySet) {
            result.add(this.mapQualificationEntityToQualificationGetDTO(q));
        }
        return result;
    }

    public EmployeeResponseDTO mapEmployeeEntityToEmployeeResponseDTO(EmployeeEntity entity) {
        EmployeeResponseDTO dto = new EmployeeResponseDTO();
        dto.setId(entity.getId());
        dto.setLastName(entity.getLastName());
        dto.setFirstName(entity.getFirstName());
        dto.setStreet(entity.getStreet());
        dto.setPostcode(entity.getPostcode());
        dto.setCity(entity.getCity());
        dto.setPhone(entity.getPhone());
        dto.setSkillSet(entity.getSkills().stream().map(this::mapQualificationEntityToQualificationGetDTO).toArray(QualificationGetDTO[]::new));
        return dto;
    }

    public EmployeesForAQualificationDTO mapToEmployeesForAQualificationDTO(QualificationEntity qualification, Set<EmployeeEntity> employees) {
        EmployeesForAQualificationDTO dto = new EmployeesForAQualificationDTO();
        dto.setQualification(this.mapQualificationEntityToQualificationGetDTO(qualification));
        for (EmployeeEntity e : employees) {
            EmployeeNameDataDTO employeeNameDataDTO = new EmployeeNameDataDTO();
            employeeNameDataDTO.setId(e.getId());
            employeeNameDataDTO.setLastName(e.getLastName());
            employeeNameDataDTO.setFirstName(e.getFirstName());
            dto.addEmployee(employeeNameDataDTO);
        }
        return dto;
    }


    public EmployeeEntity mapEmployeeRequestPutDTOToEmployeeEntity(EmployeeRequestPutDTO employeeRequestDTO) {
        EmployeeEntity entity = new EmployeeEntity();
        entity.setLastName(employeeRequestDTO.getLastName());
        entity.setFirstName(employeeRequestDTO.getFirstName());
        entity.setStreet(employeeRequestDTO.getStreet());
        entity.setPostcode(employeeRequestDTO.getPostcode());
        entity.setCity(employeeRequestDTO.getCity());
        entity.setPhone(employeeRequestDTO.getPhone());

        if (employeeRequestDTO.getSkillSet() != null) {
            for (long id : employeeRequestDTO.getSkillSet()) {
                entity.addQualification(this.qualificationService.readById(id).get());
            }
        }

        return entity;
    }
}
