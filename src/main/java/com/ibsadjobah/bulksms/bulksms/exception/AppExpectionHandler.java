package com.ibsadjobah.bulksms.bulksms.exception;

import com.ibsadjobah.bulksms.bulksms.model.HttpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AppExpectionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<HttpResponse> notFoundHandler(ResourceNotFoundException exception)
    {
        HttpResponse httpResponse = HttpResponse.builder()
                .code(HttpStatus.NOT_FOUND.value())
                .message(exception.getMessage())
                .build();

        return ResponseEntity.badRequest().body(httpResponse);
    }

    @ExceptionHandler(ResourceAlreadyExistException.class)
    public ResponseEntity<HttpResponse> alreadyExistHandler(ResourceAlreadyExistException exception)
    {
        HttpResponse httpResponse = HttpResponse.builder()
                .code(HttpStatus.CONFLICT.value())
                .message(exception.getMessage())
                .build();

        return ResponseEntity.badRequest().body(httpResponse);
    }
}
