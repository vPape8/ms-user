package com.cordytech.ms_user.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cordytech.ms_user.dto.UserRequestDTO;
import com.cordytech.ms_user.dto.UserResponseDTO;
import com.cordytech.ms_user.security.JwtUtil;
import com.cordytech.ms_user.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final UserService userService;
	private final JwtUtil jwtUtil;

	public AuthController(UserService userService, JwtUtil jwtUtil) {
		this.userService = userService;
		this.jwtUtil = jwtUtil;
	}

	@PostMapping("/register")
	public ResponseEntity<UserResponseDTO> register(@RequestBody UserRequestDTO request) {
		UserRequestDTO registerRequest = new UserRequestDTO(
			request.nombre(),
			request.apellido(),
			request.email(),
			request.password(),
			com.cordytech.ms_user.model.Role.USER,
			true
		);
		UserResponseDTO response = userService.create(registerRequest);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody UserRequestDTO request) {
		try {
			UserResponseDTO user = userService.validarLogin(request.email(), request.password());
			
			String token = jwtUtil.generateToken(user.email(), user.rol().name());
			
			Map<String, Object> response = new HashMap<>();
			response.put("message", "Login exitoso");
			response.put("token", token);
			response.put("nombre", user.nombre());
			response.put("rol", user.rol().name());
			
			return ResponseEntity.ok(response);
			
		} catch (RuntimeException e) {
			Map<String, String> error = new HashMap<>();
			error.put("error", e.getMessage());
			return ResponseEntity.status(401).body(error);
		}
	}
}
