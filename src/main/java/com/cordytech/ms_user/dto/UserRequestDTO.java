package com.cordytech.ms_user.dto;

import com.cordytech.ms_user.model.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Input payload for create and update user operations.
 */
public record UserRequestDTO(
		@NotBlank(message = "nombre es obligatorio")
		@Size(min = 2, max = 80, message = "nombre debe tener entre 2 y 80 caracteres")
		String nombre,
		@NotBlank(message = "apellido es obligatorio")
		@Size(min = 2, max = 80, message = "apellido debe tener entre 2 y 80 caracteres")
		String apellido,
		@NotBlank(message = "email es obligatorio")
		@Email(message = "email no es valido")
		String email,
		@NotBlank(message = "password es obligatorio")
		@Size(min = 8, max = 72, message = "password debe tener entre 8 y 72 caracteres")
		String password,
		@NotNull(message = "rol es obligatorio")
		Role rol,
		boolean enabled) {
}
