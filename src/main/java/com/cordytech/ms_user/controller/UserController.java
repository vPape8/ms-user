package com.cordytech.ms_user.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.cordytech.ms_user.dto.ApiResponse;
import com.cordytech.ms_user.dto.UserRequestDTO;
import com.cordytech.ms_user.dto.UserResponseDTO;
import com.cordytech.ms_user.security.SecurityEndpoints;
import com.cordytech.ms_user.service.UserService;

import jakarta.validation.Valid;

/**
 * REST endpoints for user CRUD operations.
 */
@RestController
@RequestMapping(SecurityEndpoints.USERS_BASE_PATH)
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	/**
	 * Creates a user from validated request payload.
	 */
	@PostMapping
	public ResponseEntity<ApiResponse<UserResponseDTO>> create(@Valid @RequestBody UserRequestDTO request) {
		UserResponseDTO created = userService.create(request);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(created.id())
				.toUri();
		return ResponseEntity.created(location).body(ApiResponse.ok("Usuario creado", created));
	}

	@GetMapping
	public ResponseEntity<ApiResponse<List<UserResponseDTO>>> findAll() {
		return ResponseEntity.ok(ApiResponse.ok("Usuarios listados", userService.findAll()));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<UserResponseDTO>> findById(@PathVariable Long id) {
		return ResponseEntity.ok(ApiResponse.ok("Usuario obtenido", userService.findById(id)));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<UserResponseDTO>> update(@PathVariable Long id,
			@Valid @RequestBody UserRequestDTO request) {
		return ResponseEntity.ok(ApiResponse.ok("Usuario actualizado", userService.update(id, request)));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
		userService.delete(id);
		return ResponseEntity.ok(ApiResponse.ok("Usuario eliminado", null));
	}
}
