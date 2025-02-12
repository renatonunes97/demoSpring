package com.example.demo.Exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Captura erros de validação e argumentos inválidos
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseError> handleIllegalArgumentException(IllegalArgumentException ex) {
        ResponseError response = new ResponseError(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), List.of());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Captura exceções genéricas do sistema
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseError> handleGeneralException(Exception ex) {
        ResponseError response = new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), List.of());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    // Captura erros de credenciais inválidas
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ResponseError> handleBadCredentialsException(BadCredentialsException ex) {
        ResponseError response = new ResponseError(HttpStatus.UNAUTHORIZED.value(), ex.getMessage(), List.of());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    // Captura usuário não encontrado
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ResponseError> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        ResponseError response = new ResponseError(HttpStatus.UNAUTHORIZED.value(), ex.getMessage(), List.of());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<ResponseError> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<FieldError> errors = ex.getBindingResult().getFieldErrors();
        List<ErrorParam> lisError = errors
                .stream()
                .map(fieldError -> new ErrorParam(fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();

        ResponseError responseError = new ResponseError(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Erro de validação", lisError);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(responseError);
    }

}


