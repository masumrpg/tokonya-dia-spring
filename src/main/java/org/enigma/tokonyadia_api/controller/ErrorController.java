package org.enigma.tokonyadia_api.controller;

import jakarta.validation.ConstraintViolationException;
import org.enigma.tokonyadia_api.util.ResponseUtil;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ErrorController {

    @ExceptionHandler({ResponseStatusException.class})
    public ResponseEntity<?> handlingResponseStatusException(ResponseStatusException e) {
        HttpStatusCode statusCode = e.getStatusCode();
        return ResponseUtil.buildCommonResponse(HttpStatus.valueOf(statusCode.value()), e.getReason(), null);
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<?> handlingDataIntegrityViolationException(DataIntegrityViolationException e) {
        String message = "";
        HttpStatus status = HttpStatus.CONFLICT;

        if (e.getCause() != null) {
            String causeMessage = e.getCause().getMessage();
            if (causeMessage.contains("duplicate key value")) {
                message = "Data already exist.";
            } else if (causeMessage.contains("cannot be null")) {
                message = "Data cannot be null.";
                status = HttpStatus.BAD_REQUEST;
            } else if (causeMessage.contains("foreign key constraint")) {
                message = "Data cannot be deleted because it is used by other data.";
                status = HttpStatus.BAD_REQUEST;
            } else if (causeMessage.contains("Duplicate entry")) {
                message = "Data already exist.";
            } else {
                message = "Unexpected error occurred";
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }

        return ResponseUtil.buildCommonResponse(status, message, null);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<?> handlingConstraintViolationException(ConstraintViolationException e) {
        return ResponseUtil.buildCommonResponse(HttpStatus.BAD_REQUEST, e.getMessage(), null);
    }

}