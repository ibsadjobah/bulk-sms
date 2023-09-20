package com.ibsadjobah.bulksms.bulksms.exception;

import com.ibsadjobah.bulksms.bulksms.model.HttpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<HttpResponse> methoodArgumentViolationHandler(MethodArgumentNotValidException exception)
    {
       /* Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : exception.getFieldErrors())
        {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }*/

       Map<String, String> errors = exception.getBindingResult().getFieldErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));

        HttpResponse httpResponse = HttpResponse.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message("Il y a des erreurs")
                .errors(errors)
                .build();

        return ResponseEntity.badRequest().body(httpResponse);
    }

    /*
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<HttpResponse> IllegalArgumentException(IllegalArgumentException exception){

        HttpResponse  httpResponse = HttpResponse.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(exception.getMessage())
                .build();

        return ResponseEntity.ok()
                .body(httpResponse);

    }*/




}
