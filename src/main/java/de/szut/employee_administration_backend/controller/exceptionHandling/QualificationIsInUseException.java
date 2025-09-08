package de.szut.employee_administration_backend.controller.exceptionHandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class QualificationIsInUseException extends RuntimeException {
    public QualificationIsInUseException(String message) {
        super(message);
    }
}
