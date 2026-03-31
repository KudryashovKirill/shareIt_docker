package ru.practicum.shareit.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.util.exception.*;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class MyRestAdvice {
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorBody> noElementException(NoSuchElementException exception) {
        return new ResponseEntity<>(new ErrorBody(exception.getClass().getName(),
                exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MyException.class)
    public ResponseEntity<ErrorBody> myException(MyException exception) {
        return new ResponseEntity<>(new ErrorBody(exception.getClass().getName(),
                exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorBody> emailException(EmailAlreadyExistsException exception) {
        return new ResponseEntity<>(new ErrorBody(exception.getClass().getName(),
                exception.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorBody> illegalArgException(IllegalArgumentException exception) {
        return new ResponseEntity<>(new ErrorBody(exception.getClass().getName(),
                exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorBody> responseStatusException(ResponseStatusException exception) {
        return new ResponseEntity<>(new ErrorBody(exception.getClass().getName(),
                exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorBody> methodArgumentException(MethodArgumentNotValidException exception) {
        return new ResponseEntity<>(new ErrorBody(exception.getClass().getName(), exception.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalOwnerException.class)
    public ResponseEntity<ErrorBody> illegalOwnerException(IllegalOwnerException exception) {
        return new ResponseEntity<>(new ErrorBody(exception.getClass().getName(), exception.getMessage()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalItemException.class)
    public ResponseEntity<ErrorBody> illegalItemException(IllegalItemException exception) {
        return new ResponseEntity<>(new ErrorBody(exception.getClass().getName(), exception.getMessage()),
                HttpStatus.BAD_REQUEST);
    }
}
