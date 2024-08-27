package com.examen.gerden.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

import static com.examen.gerden.backend.dtos.MyHttpResponse.response;

@RestControllerAdvice
public class AppExceptionHandler {
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> badCredentialsException(BadCredentialsException exception) {
        return response(HttpStatus.UNAUTHORIZED, exception.getMessage(), null);
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> exception(Exception exception) {
        return response(HttpStatus.UNAUTHORIZED, exception.getMessage(), null);
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> resourceNotFoundException(ResourceNotFoundException exception) {
        return response(HttpStatus.NOT_FOUND, exception.getMessage(), null);
    }
    @ExceptionHandler(ResourceAlreadyExistException.class)
    public ResponseEntity<Object> resourceAlreadyExistException(ResourceAlreadyExistException exception) {
        return response(HttpStatus.CONFLICT, exception.getMessage(), null);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> methodArgumentNotValidException(MethodArgumentNotValidException exception)
    {
        Map<String, String> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
        return response(HttpStatus.BAD_REQUEST, "NON OK", errors);
    }
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Object> usernameNotFoundException(UsernameNotFoundException exception)
    {
        return response(HttpStatus.NOT_FOUND, exception.getMessage(), null);
    }

}
