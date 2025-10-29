package com.bojanlukic.test.controller;

import com.bojanlukic.test.controller.dto.ApiResponse;
import com.bojanlukic.test.service.exception.AccountNotFoundException;
import com.bojanlukic.test.service.exception.TransactionNotExecutedException;
import com.bojanlukic.test.service.exception.WithdrawalNotSupportedException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
@ResponseBody()
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneralExceptionError(Exception exception) {
        return ResponseEntity.internalServerError().body(ErrorResponse.builder()
                .data(exception.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(exception.getMessage())
                .build());
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(AccountNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.builder()
                .data(exception.getMessage())
                .status(HttpStatus.NOT_FOUND)
                .message(exception.getMessage())
                .build());
    }

    @ExceptionHandler(WithdrawalNotSupportedException.class)
    public ResponseEntity<?> handleWithdrawalNotSupportedException(WithdrawalNotSupportedException exception) {
        return ResponseEntity.internalServerError().body(ErrorResponse.builder()
                .data(exception.getMessage())
                .status(HttpStatus.NOT_ACCEPTABLE)
                .message(exception.getMessage())
                .build());
    }

    @ExceptionHandler(TransactionNotExecutedException.class)
    public ResponseEntity<?> handleTransactionNotExecutedException(TransactionNotExecutedException exception) {
        return ResponseEntity.internalServerError().body(ErrorResponse.builder()
                .data(exception.getMessage())
                .status(HttpStatus.CONFLICT)
                .message(exception.getMessage())
                .build());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            org.springframework.http.HttpHeaders headers,
            org.springframework.http.HttpStatusCode status,
            org.springframework.web.context.request.WebRequest request) {

        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        DefaultMessageSourceResolvable::getDefaultMessage,
                        (oldValue, newValue) -> oldValue
                ));

        return ResponseEntity.badRequest()
                .body(new ApiResponse("Validation failed.", errors));
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ErrorResponse<T> {
        private T data;
        private HttpStatus status;
        private String message;
    }
}