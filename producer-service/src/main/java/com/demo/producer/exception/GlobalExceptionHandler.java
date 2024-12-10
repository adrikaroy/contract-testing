package com.demo.producer.exception;

import com.demo.producer.response.ErrorResponse;
import com.demo.producer.util.ResponseStatus;
import jakarta.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<Object> handleCalculatorException(CustomException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ResponseStatus.ERROR, List.of(ex.getMessage()));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ResponseStatus.ERROR, List.of(ex.getMessage()));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    protected ResponseEntity<Object> constraintViolation(ConstraintViolationException ex) {
        List<String> templateMessages = new ArrayList<>();
        ex.getConstraintViolations().forEach(violation -> templateMessages.add(violation.getMessageTemplate()));
        ErrorResponse errorResponse = new ErrorResponse(ResponseStatus.ERROR, templateMessages);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    protected ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> details = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .toList();

        ErrorResponse errorResponse = new ErrorResponse(ResponseStatus.ERROR, details);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleTypeMismatchException(
            MethodArgumentTypeMismatchException ex, WebRequest webRequest) {
        String parameter = webRequest.getParameter(ex.getName());
        String message;
        if (parameter != null && ex.getRequiredType() != null) {
            message = "Unsupported operation : ".concat(parameter);
        } else {
            message = ex.getMessage();
        }

        ErrorResponse errorResponse = new ErrorResponse(ResponseStatus.ERROR, List.of(message));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleAllExceptions(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(ResponseStatus.ERROR, List.of(ex.getMessage()));
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
