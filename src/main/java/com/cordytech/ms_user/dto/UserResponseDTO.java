package com.cordytech.ms_user.dto;

import java.time.LocalDateTime;

import com.cordytech.ms_user.model.Role;

/**
 * Output payload for user endpoints.
 */
public record UserResponseDTO(
		Long id,
		String nombre,
		String apellido,
		String email,
		Role rol,
		LocalDateTime createdAt,
		LocalDateTime updatedAt,
		boolean enabled) {
}
