package de.szut.employee_administration_backend.controller;

import de.szut.employee_administration_backend.controller.dtos.EmployeesForAQualificationDTO;
import de.szut.employee_administration_backend.controller.dtos.QualificationGetDTO;
import de.szut.employee_administration_backend.controller.dtos.QualificationPostDTO;
import de.szut.employee_administration_backend.controller.exceptionHandling.ResourceNotFoundException;
import de.szut.employee_administration_backend.controller.service.MappingService;
import de.szut.employee_administration_backend.controller.service.QualificationService;
import de.szut.employee_administration_backend.controller.service.model.EmployeeEntity;
import de.szut.employee_administration_backend.controller.service.model.QualificationEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@CrossOrigin
@RestController
@RequestMapping(value = "qualifications")
public class QualificationController {
    private final QualificationService service;
    private final MappingService mappingService;

    public QualificationController(QualificationService service, MappingService mappingService) {
        this.service = service;
        this.mappingService = mappingService;
    }

    @Operation(summary = "creates a new qualification with its id and designation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "created qualification",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = QualificationPostDTO.class))}),
            @ApiResponse(responseCode = "400", description = "invalid JSON posted",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "not authorized",
                    content = @Content)})
    @PostMapping
    public ResponseEntity<QualificationGetDTO> createQualification(@RequestBody @Valid QualificationPostDTO qualificationPostDTO) {
        QualificationEntity qualification = this.mappingService.mapQualificationPostDTOToQualification(qualificationPostDTO);
        qualification = this.service.create(qualification);
        QualificationGetDTO qualificationGetDTO = this.mappingService.mapQualificationEntityToQualificationGetDTO(qualification);
        return new ResponseEntity<>(qualificationGetDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "delivers a list of all available qualifications")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "list of qualifications",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = QualificationGetDTO.class))}),
            @ApiResponse(responseCode = "401", description = "not authorized",
                    content = @Content)})
    @GetMapping
    public ResponseEntity<List<QualificationGetDTO>> findAll() {
        List<QualificationEntity> qualificationList = this.service.readAll();
        List<QualificationGetDTO> response = new LinkedList<>();
        for (QualificationEntity q : qualificationList) {
            QualificationGetDTO dto = this.mappingService.mapQualificationEntityToQualificationGetDTO(q);
            response.add(dto);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "deletes a qualification by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "delete successful"),
            @ApiResponse(responseCode = "401", description = "not authorized",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "qualification is in use",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "resource not found",
                    content = @Content)})
    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteQualificationByDesignation(@PathVariable long id) {
        if (!this.service.qualificationExistsById(id)) {
            throw new ResourceNotFoundException("QualificationEntity not found on id = " + id);
        }
        this.service.delete(id);
    }

    @Operation(summary = "find employees by qualification id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of employees who have the desired qualification",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmployeesForAQualificationDTO.class))}),
            @ApiResponse(responseCode = "404", description = "qualification id does not exist",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "not authorized",
                    content = @Content)})
    @GetMapping("/{id}/employees")
    public ResponseEntity<EmployeesForAQualificationDTO> findAllEmployeesByQualification(@PathVariable long id) {
        var entity = this.service.readById(id);
        if (!entity.isPresent()) {
            throw new ResourceNotFoundException("QualificationEntity not found on id = " + id);
        } else {
            var qualificationEntity = entity.get();
            Set<EmployeeEntity> employeeList = this.service.readAllEmployeesByQualification(id);

            EmployeesForAQualificationDTO response = this.mappingService.mapToEmployeesForAQualificationDTO(qualificationEntity, employeeList);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }


    // Endpoint for PUT request /qualifications/{id}
    @Operation(summary = "updates a qualification")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "updated qualification",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = QualificationPostDTO.class))}),
            @ApiResponse(responseCode = "400", description = "invalid JSON posted",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "not authorized",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "resource not found",
                    content = @Content)})
    @PutMapping("/{id}")
    public ResponseEntity<QualificationGetDTO> updateQualification(@PathVariable long id, @RequestBody @Valid QualificationPostDTO qualificationPostDTO) {
        if (!this.service.qualificationExistsById(id)) {
            throw new ResourceNotFoundException("QualificationEntity not found on id = " + id);
        }
        QualificationEntity qualification = this.mappingService.mapQualificationPostDTOToQualification(qualificationPostDTO);
        qualification.setId(id);
        qualification = this.service.update(qualification);
        QualificationGetDTO qualificationGetDTO = this.mappingService.mapQualificationEntityToQualificationGetDTO(qualification);
        return new ResponseEntity<>(qualificationGetDTO, HttpStatus.OK);
    }

}
