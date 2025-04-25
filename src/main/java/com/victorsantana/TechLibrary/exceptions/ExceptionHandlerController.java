package com.victorsantana.TechLibrary.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerController {

    private ResponseEntity<RestErrorMessage> buildErrorResponse(HttpStatus status, String message) {
        RestErrorMessage errorMessage = new RestErrorMessage(status.value(), status.getReasonPhrase(), message);
        return ResponseEntity.status(status).body(errorMessage);
    }

    @ExceptionHandler(EmailAddressAlreadyRegisteredException.class)
    public ResponseEntity<RestErrorMessage> handleEmailAddressAlreadyRegisteredException(
            EmailAddressAlreadyRegisteredException exception
    ) {
        return buildErrorResponse(HttpStatus.CONFLICT, exception.getMessage());
    }
}