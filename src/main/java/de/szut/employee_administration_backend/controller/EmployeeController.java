package de.szut.employee_administration_backend.controller;

import de.szut.employee_administration_backend.controller.dtos.*;
import de.szut.employee_administration_backend.controller.exceptionHandling.ResourceNotFoundException;
import de.szut.employee_administration_backend.controller.service.EmployeeService;
import de.szut.employee_administration_backend.controller.service.MappingService;
import de.szut.employee_administration_backend.controller.service.QualificationService;
import de.szut.employee_administration_backend.controller.service.model.EmployeeEntity;
import de.szut.employee_administration_backend.controller.service.model.QualificationEntity;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("employees")
public class EmployeeController implements IEmployeeController {
    private final EmployeeService service;
    private final QualificationService qualiService;
    private final MappingService mappingService;

    private final String exceptionMessage = "EmployeeEntity not found on id = ";

    public EmployeeController(EmployeeService service, QualificationService qualiService, MappingService mappingService) {
        this.service = service;
        this.qualiService = qualiService;
        this.mappingService = mappingService;
    }

    @Override

    @PostMapping()
    public ResponseEntity<EmployeeResponseDTO> createEmployee(@Valid @RequestBody EmployeeRequestDTO employeeRequestDTO) {
        EmployeeEntity employee = this.mappingService.mapEmployeeRequestDTOToEmployeeEntity(employeeRequestDTO);
        employee = this.service.create(employee);
        EmployeeResponseDTO responseDTO = this.mappingService.mapEmployeeEntityToEmployeeResponseDTO(employee);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @Override
    @GetMapping()
    public ResponseEntity<List<EmployeeResponseDTO>> findAll() {
        List<EmployeeEntity> employeeList = this.service.getAllEmployees();
        List<EmployeeResponseDTO> response = new LinkedList<>();
        for (EmployeeEntity e : employeeList) {
            response.add(this.mappingService.mapEmployeeEntityToEmployeeResponseDTO(e));
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> findById(@PathVariable long id) {
        EmployeeEntity employee = service.getEmployeeById(id);
        if (employee == null) {
            throw new ResourceNotFoundException(exceptionMessage + id);
        }
        EmployeeResponseDTO dto = this.mappingService.mapEmployeeEntityToEmployeeResponseDTO(employee);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> updateEmployee(@PathVariable Long id, @Valid @RequestBody EmployeeRequestPutDTO employeeRequestDTO) {
        if (!this.service.employeeExistsById(id)) {
            throw new ResourceNotFoundException("Employee not found on id = !" + id);
        }
        EmployeeEntity updatedEmployee = this.mappingService.mapEmployeeRequestPutDTOToEmployeeEntity(employeeRequestDTO);
        updatedEmployee.setId(id);


        updatedEmployee = this.service.updateEmployee(updatedEmployee);
        EmployeeResponseDTO dto = this.mappingService.mapEmployeeEntityToEmployeeResponseDTO(updatedEmployee);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Override
    @PatchMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> patchEmployee(@PathVariable Long id, @Valid @RequestBody EmployeeRequestPutDTO employeeRequestDTO) {
        if (!this.service.employeeExistsById(id)) {
            throw new ResourceNotFoundException("Employee not found on id = !" + id);
        }
        EmployeeEntity updatedEmployee = this.mappingService.mapEmployeeRequestPutDTOToEmployeeEntity(employeeRequestDTO);
        updatedEmployee.setId(id);

        updatedEmployee = this.service.patchEmployee(updatedEmployee);
        EmployeeResponseDTO dto = this.mappingService.mapEmployeeEntityToEmployeeResponseDTO(updatedEmployee);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Override
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable Long id) {
        if (!this.service.employeeExistsById(id)) {
            throw new ResourceNotFoundException(this.exceptionMessage + id);
        }
        this.service.delete(id);
    }

    @Override
    @PostMapping("/{id}/qualifications")
    public ResponseEntity<EmployeeNameAndSkillDataDTO> addQualificationToEmployeeById(@PathVariable Long id,
                                                                                      @Valid @RequestBody QualificationPostDTO qualificationPostDTO) {
        if (!this.service.employeeExistsById(id)) {
            throw new ResourceNotFoundException("Employee not found on id = " + id);
        }
        if (!this.qualiService.qualificationExistsBySkill(qualificationPostDTO.getSkill())) {
            throw new ResourceNotFoundException("Qualification not found on designation = " + qualificationPostDTO.getSkill());
        }
        QualificationEntity qualification = this.mappingService.mapQualificationPostDTOToQualification(qualificationPostDTO);
        EmployeeEntity employee = this.service.addQualification(id, qualification);
        EmployeeNameAndSkillDataDTO response = this.mappingService.mapEmployeeEntityToEmployeeNameAndSkillDTO(employee);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @GetMapping("/{id}/qualifications")
    public ResponseEntity<EmployeeNameAndSkillDataDTO> findAllQualificationOfAEmployeeById(@PathVariable Long id) {
        if (!this.service.employeeExistsById(id)) {
            throw new ResourceNotFoundException("EmployeeEntity not found on id = " + id);
        }
        EmployeeEntity employee = this.service.getEmployeeById(id);
        EmployeeNameAndSkillDataDTO response = this.mappingService.mapEmployeeEntityToEmployeeNameAndSkillDTO(employee);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @DeleteMapping("/{eid}/qualifications/{qid}")
    public ResponseEntity<EmployeeNameAndSkillDataDTO> removeQualificationFromEmployee(@PathVariable Long eid, @PathVariable Long qid) {
        if (!this.service.employeeExistsById(eid)) {
            throw new ResourceNotFoundException("EmployeeEntity not found on id = " + eid);
        }
        if (this.qualiService.readById(qid).isEmpty()) {
            throw new ResourceNotFoundException("QualificationEntity not found on id = " + qid);
        }
        QualificationEntity qualification = this.qualiService.readById(qid).get();
        EmployeeEntity employee = this.service.removeQualification(eid, qualification);
        EmployeeNameAndSkillDataDTO response = this.mappingService.mapEmployeeEntityToEmployeeNameAndSkillDTO(employee);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
