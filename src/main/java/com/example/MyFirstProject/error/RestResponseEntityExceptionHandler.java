package com.example.MyFirstProject.error;

import com.example.MyFirstProject.exception.CustomException;
import com.example.MyFirstProject.util.GenericResponse;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@NoArgsConstructor
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler( { CustomException.class })
    public ResponseEntity<Object> handleRequest(final CustomException exception, final WebRequest request) {
        final String bodyOfResponse = exception.getMessage();
        return handleExceptionInternal(
                exception,
                new GenericResponse(messageSource.getMessage(bodyOfResponse, null, request.getLocale()), exception.getHttpStatus()),
                new HttpHeaders(),
                exception.getHttpStatus(),
                request);
    }
}
