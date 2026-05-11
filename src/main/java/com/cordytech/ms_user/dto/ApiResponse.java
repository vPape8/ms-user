package com.cordytech.ms_user.dto;

import java.time.LocalDateTime;

/**
 * Standard response envelope for all API endpoints.
 */
public record ApiResponse<T>(
		boolean success,
		String message,
		T data,
		LocalDateTime timestamp) {

	public static <T> ApiResponse<T> ok(String message, T data) {
		return new ApiResponse<>(true, message, data, LocalDateTime.now());
	}

	public static <T> ApiResponse<T> error(String message, T data) {
		return new ApiResponse<>(false, message, data, LocalDateTime.now());
	}
}
