package dev.mucino.backend.controllers;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import dev.mucino.backend.exceptions.DataConflictException;
import dev.mucino.backend.exceptions.ResourceNotFoundException;
import dev.mucino.backend.exceptions.UserExistException;
import dev.mucino.backend.exceptions.UserNotFoundException;
import dev.mucino.backend.models.Error;

@RestControllerAdvice 
public class HandlerExceptionController {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Error> handleUserNotFound(UserNotFoundException ex) {
        Error error = new Error();
        error.setDate(new Date());
        error.setError("El usuario no fue encontrado en la base de datos");
        error.setMessage(ex.getMessage());
        error.setStatus(HttpStatus.NOT_FOUND.value());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Error> handleRoleNotFound(ResourceNotFoundException ex) {
        Error error = new Error();
        error.setDate(new Date());
        error.setError("Recurso no encontrado");
        error.setMessage(ex.getMessage());
        error.setStatus(HttpStatus.NOT_FOUND.value());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(UserExistException.class)
    public ResponseEntity<Error> handleUserExist(UserExistException ex) {
        Error error = new Error();
        error.setDate(new Date());
        error.setError("Conflicto con los datos del usuario");
        error.setMessage(ex.getMessage());
        error.setStatus(HttpStatus.BAD_REQUEST.value()); 
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(DataConflictException.class)
    public ResponseEntity<Error> handleDataException(UserExistException ex) {
        Error error = new Error();
        error.setDate(new Date());
        error.setError("Conflicto en los datos proporcionados");
        error.setMessage(ex.getMessage());
        error.setStatus(HttpStatus.BAD_REQUEST.value()); 
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }


}
