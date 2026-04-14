package com.project.ecommercebackend.exception;

import com.project.ecommercebackend.dto.response.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class MyGlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(MyGlobalExceptionHandler.class);

    // 400 — Validation errors from @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidationException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        ApiResponse response = new ApiResponse("Validation failed", false, errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 400 — Constraint violations from @Validated on path/query params
    @ExceptionHandler(jakarta.validation.ConstraintViolationException.class)
    public ResponseEntity<ApiResponse> handleConstraintViolationException(
            jakarta.validation.ConstraintViolationException e) {
        ApiResponse response = new ApiResponse(e.getMessage(), false);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 400 — Generic business rule violation (catch-all for remaining cases)
    @ExceptionHandler(APIException.class)
    public ResponseEntity<ApiResponse> handleAPIException(APIException e) {
        ApiResponse response = new ApiResponse(e.getMessage(), false);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 400 — Out of stock
    @ExceptionHandler(OutOfStockException.class)
    public ResponseEntity<ApiResponse> handleOutOfStockException(OutOfStockException e) {
        ApiResponse response = new ApiResponse(e.getMessage(), false);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 400 — Insufficient stock for requested quantity
    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ApiResponse> handleInsufficientStockException(InsufficientStockException e) {
        Map<String, String> details = new HashMap<>();
        details.put("product", e.getProductName());
        details.put("requested", String.valueOf(e.getRequestedQuantity()));
        details.put("available", String.valueOf(e.getAvailableQuantity()));
        ApiResponse response = new ApiResponse(e.getMessage(), false, details);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 400 — Cart is empty during checkout
    @ExceptionHandler(EmptyCartException.class)
    public ResponseEntity<ApiResponse> handleEmptyCartException(EmptyCartException e) {
        ApiResponse response = new ApiResponse(e.getMessage(), false);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 401 — Authentication failures
    @ExceptionHandler({BadCredentialsException.class, AuthenticationException.class})
    public ResponseEntity<ApiResponse> handleAuthenticationException(AuthenticationException e) {
        ApiResponse response = new ApiResponse("Invalid credentials", false);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    // 404 — Resource not found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> handleResourceNotFoundException(ResourceNotFoundException e) {
        Map<String, String> details = new HashMap<>();
        details.put("resource", e.getResourceName());
        details.put("field", e.getFieldName());
        details.put("value", String.valueOf(e.getFieldValue()));
        ApiResponse response = new ApiResponse(e.getMessage(), false, details);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // 409 — Duplicate resource conflict
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiResponse> handleDuplicateResourceException(DuplicateResourceException e) {
        Map<String, String> details = new HashMap<>();
        details.put("resource", e.getResourceName());
        details.put("field", e.getFieldName());
        details.put("value", String.valueOf(e.getFieldValue()));
        ApiResponse response = new ApiResponse(e.getMessage(), false, details);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    // 500 — File storage failures
    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<ApiResponse> handleFileStorageException(FileStorageException e) {
        logger.error("File storage error: {}", e.getMessage(), e);
        ApiResponse response = new ApiResponse(e.getMessage(), false);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 500 — Catch-all for unexpected errors
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGenericException(Exception e) {
        logger.error("Unexpected error occurred", e);
        ApiResponse response = new ApiResponse("An unexpected error occurred", false);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
