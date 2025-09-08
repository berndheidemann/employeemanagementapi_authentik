package de.szut.employee_administration_backend.controller;

import de.szut.employee_administration_backend.controller.dtos.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface IEmployeeController {
    @Operation(summary = "creates a new employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "created employee",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmployeeResponseDTO.class))}),
            @ApiResponse(responseCode = "400", description = "invalid JSON posted",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "not authorized",
                    content = @Content)})
    ResponseEntity<EmployeeResponseDTO> createEmployee(@Valid @RequestBody EmployeeRequestDTO employeeRequestDTO);

    @Operation(summary = "delivers a list of all employees")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "list of employees",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmployeeResponseDTO.class))}),
            @ApiResponse(responseCode = "401", description = "not authorized",
                    content = @Content)})
    ResponseEntity<List<EmployeeResponseDTO>> findAll();

    @Operation(summary = "find employee by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "employee",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmployeeResponseDTO.class))}),
            @ApiResponse(responseCode = "404", description = "resource not found",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "not authorized",
                    content = @Content)})
    ResponseEntity<EmployeeResponseDTO> findById(@PathVariable long id);

    @Operation(summary = "updates employee by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "employee",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmployeeResponseDTO.class))}),
            @ApiResponse(responseCode = "404", description = "resource not found",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "not authorized",
                    content = @Content)})
    ResponseEntity<EmployeeResponseDTO> updateEmployee(@PathVariable Long id, @Valid @RequestBody EmployeeRequestPutDTO employeeRequestDTO);

    @Operation(summary = "updates employee by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "employee",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmployeeResponseDTO.class))}),
            @ApiResponse(responseCode = "404", description = "resource not found",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "not authorized",
                    content = @Content)})
    ResponseEntity<EmployeeResponseDTO> patchEmployee(@PathVariable Long id, @Valid @RequestBody EmployeeRequestPutDTO employeeRequestDTO);

    @Operation(summary = "deletes a employee by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "delete successful"),
            @ApiResponse(responseCode = "404", description = "resource not found",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "not authorized",
                    content = @Content)})
    void deleteCustomer(@PathVariable Long id);

    @Operation(summary = "adds a qualification to an employee by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "employee with a list of his qualifications",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmployeeNameAndSkillDataDTO.class))}),
            @ApiResponse(responseCode = "400", description = "invalid JSON posted or employee already has this qualification",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "not authorized",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "resource not found",
                    content = @Content)})
    ResponseEntity<EmployeeNameAndSkillDataDTO> addQualificationToEmployeeById(@PathVariable Long id,
                                                                               @Valid @RequestBody QualificationPostDTO qualificationPostDTO);

    @Operation(summary = "finds all qualifications of an employee by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "employee with a list of his qualifications",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmployeeNameAndSkillDataDTO.class))}),
            @ApiResponse(responseCode = "401", description = "not authorized",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "resource not found",
                    content = @Content)})
    ResponseEntity<EmployeeNameAndSkillDataDTO> findAllQualificationOfAEmployeeById(@PathVariable Long id);

    @Operation(summary = "deletes a qualification of an employee by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "employee with a list of his qualifications",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmployeeNameAndSkillDataDTO.class))}),
            @ApiResponse(responseCode = "401", description = "not authorized",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "resource not found",
                    content = @Content)})
    ResponseEntity<EmployeeNameAndSkillDataDTO> removeQualificationFromEmployee(@PathVariable Long eid, @PathVariable Long qid);


}
