package com.dario.pricingservice.adapter.input.rest;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ProblemDetail handleMissingParameter(
            MissingServletRequestParameterException exception
    ) {
        return badRequest(
                "Falta el parámetro obligatorio: "
                        + exception.getParameterName()
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ProblemDetail handleTypeMismatch(
            MethodArgumentTypeMismatchException exception
    ) {
        return badRequest(
                "El parámetro '" + exception.getName()
                        + "' tiene un formato inválido"
        );
    }

    @ExceptionHandler({
            ConstraintViolationException.class,
            HandlerMethodValidationException.class
    })
    public ProblemDetail handleValidationException(
            Exception exception
    ) {
        return badRequest("Los parámetros recibidos no son válidos");
    }

    private ProblemDetail badRequest(String detail) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                detail
        );

        problemDetail.setTitle("Solicitud inválida");

        return problemDetail;
    }
}