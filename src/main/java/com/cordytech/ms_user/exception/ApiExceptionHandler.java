package com.cordytech.ms_user.exception;

import java.util.List;

import com.cordytech.ms_user.dto.ApiResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception mapper for consistent API responses.
 */
@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponse<ProblemDetail>> handleNotFound(ResourceNotFoundException exception) {
		ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
		problemDetail.setTitle("Recurso no encontrado");
		problemDetail.setDetail(exception.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(ApiResponse.error("No encontrado", problemDetail));
	}

	@ExceptionHandler(ResourceConflictException.class)
	public ResponseEntity<ApiResponse<ProblemDetail>> handleConflict(ResourceConflictException exception) {
		ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.CONFLICT);
		problemDetail.setTitle("Conflicto de recurso");
		problemDetail.setDetail(exception.getMessage());
		return ResponseEntity.status(HttpStatus.CONFLICT)
				.body(ApiResponse.error("Conflicto", problemDetail));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<ProblemDetail>> handleValidation(MethodArgumentNotValidException exception) {
		List<String> errors = exception.getBindingResult()
				.getFieldErrors()
				.stream()
				.map(ApiExceptionHandler::formatError)
				.toList();
		ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
		problemDetail.setTitle("Validacion fallida");
		problemDetail.setDetail("La solicitud contiene datos invalidos");
		problemDetail.setProperty("errors", errors);
		return ResponseEntity.badRequest().body(ApiResponse.error("Validacion fallida", problemDetail));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<ProblemDetail>> handleUnexpected(Exception exception) {
		ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		problemDetail.setTitle("Error interno");
		problemDetail.setDetail(exception.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(ApiResponse.error("Error interno del servidor", problemDetail));
	}

	private static String formatError(FieldError fieldError) {
		return fieldError.getField() + ": " + fieldError.getDefaultMessage();
	}
}
