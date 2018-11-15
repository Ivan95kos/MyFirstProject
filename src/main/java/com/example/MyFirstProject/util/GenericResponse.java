package com.example.MyFirstProject.util;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
public class GenericResponse {

        private String message;
//        private String error;
        private HttpStatus httpStatus;

        public GenericResponse(final String message) {
            super();
            this.message = message;
        }

        public GenericResponse(final String message, final HttpStatus httpStatus) {
            super();
            this.httpStatus=httpStatus;
            this.message = message;
        }

//        public GenericResponse(final String message, final HttpStatus httpStatus, final String error) {
//            super();
//            this.message = message;
//            this.httpStatus=httpStatus;
//            this.error = error;
//        }

//        public GenericResponse(List<ObjectError> allErrors, String error) {
//            this.error = error;
//            String temp = allErrors.stream().map(e -> {
//                if (e instanceof FieldError) {
//                    return "{\"field\":\"" + ((FieldError) e).getField() + "\",\"defaultMessage\":\"" + e.getDefaultMessage() + "\"}";
//                } else {
//                    return "{\"object\":\"" + e.getObjectName() + "\",\"defaultMessage\":\"" + e.getDefaultMessage() + "\"}";
//                }
//            }).collect(Collectors.joining(","));
//            this.message = "[" + temp + "]";
//        }
}
