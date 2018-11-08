package com.example.MyFirstProject.Error;

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

//    API

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler( { CustomException.class })
    public ResponseEntity<Object> handleBadRequest(final CustomException ex, final WebRequest request) {
        final String bodyOfResponse = ex.getMessage();
        return handleExceptionInternal(ex, new GenericResponse(messageSource.getMessage(bodyOfResponse, null, request.getLocale()), ex.getHttpStatus()), new HttpHeaders(), ex.getHttpStatus(), request);
    }
}
