package com.securetasker.exception;

import java.time.Instant;
import java.util.stream.Collectors;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ApiError> handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
    return buildError(ex.getMessage(), HttpStatus.NOT_FOUND, request.getRequestURI());
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ApiError> handleBadRequest(BadRequestException ex, HttpServletRequest request) {
    return buildError(ex.getMessage(), HttpStatus.BAD_REQUEST, request.getRequestURI());
  }

  @ExceptionHandler(ForbiddenException.class)
  public ResponseEntity<ApiError> handleForbidden(ForbiddenException ex, HttpServletRequest request) {
    return buildError(ex.getMessage(), HttpStatus.FORBIDDEN, request.getRequestURI());
  }

  @ExceptionHandler({BadCredentialsException.class, UsernameNotFoundException.class})
  public ResponseEntity<ApiError> handleAuthFailure(Exception ex, HttpServletRequest request) {
    return buildError("Invalid credentials", HttpStatus.UNAUTHORIZED, request.getRequestURI());
  }

  @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
  public ResponseEntity<ApiError> handleValidation(Exception ex, HttpServletRequest request) {
    String message;
    if (ex instanceof MethodArgumentNotValidException manv) {
      message = manv.getBindingResult().getFieldErrors().stream()
          .map(error -> error.getField() + ": " + error.getDefaultMessage())
          .collect(Collectors.joining(", "));
    } else {
      BindException bind = (BindException) ex;
      message = bind.getBindingResult().getFieldErrors().stream()
          .map(error -> error.getField() + ": " + error.getDefaultMessage())
          .collect(Collectors.joining(", "));
    }
    return buildError(message, HttpStatus.BAD_REQUEST, request.getRequestURI());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiError> handleGeneric(Exception ex, HttpServletRequest request) {
    return buildError("Unexpected error", HttpStatus.INTERNAL_SERVER_ERROR, request.getRequestURI());
  }

  private ResponseEntity<ApiError> buildError(String message, HttpStatus status, String path) {
    ApiError error = ApiError.builder()
        .timestamp(Instant.now())
        .status(status.value())
        .error(status.getReasonPhrase())
        .message(message)
        .path(path)
        .build();
    return ResponseEntity.status(status).body(error);
  }
}
