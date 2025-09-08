package de.szut.employee_administration_backend.controller.exceptionHandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class EmployeeAlreadyHasThisQualificationException extends RuntimeException {
    public EmployeeAlreadyHasThisQualificationException(String message) {
        super(message);
    }
}
