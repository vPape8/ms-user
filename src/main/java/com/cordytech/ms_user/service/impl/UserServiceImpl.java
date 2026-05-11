package com.cordytech.ms_user.service.impl;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cordytech.ms_user.dto.UserRequestDTO;
import com.cordytech.ms_user.dto.UserResponseDTO;
import com.cordytech.ms_user.exception.ResourceConflictException;
import com.cordytech.ms_user.exception.ResourceNotFoundException;
import com.cordytech.ms_user.model.User;
import com.cordytech.ms_user.repository.UserRepository;
import com.cordytech.ms_user.service.UserService;

/**
 * User business logic implementation.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	/**
	 * Creates a user and stores password as BCrypt hash.
	 */
	@Override
	public UserResponseDTO create(UserRequestDTO request) {
		validateEmailAvailability(request.email(), null);
		User user = new User();
		applyRequest(user, request);
		return toResponse(userRepository.save(user));
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserResponseDTO> findAll() {
		return userRepository.findAll().stream()
				.map(UserServiceImpl::toResponse)
				.toList();
	}

	@Override
	@Transactional(readOnly = true)
	public UserResponseDTO findById(Long id) {
		return toResponse(findEntityById(id));
	}

	/**
	 * Updates user and re-hashes password when payload is received.
	 */
	@Override
	public UserResponseDTO update(Long id, UserRequestDTO request) {
		User user = findEntityById(id);
		validateEmailAvailability(request.email(), id);
		applyRequest(user, request);
		return toResponse(userRepository.save(user));
	}

	@Override
	public void delete(Long id) {
		User user = findEntityById(id);
		userRepository.delete(user);
	}

	private User findEntityById(Long id) {
		return userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
	}

	private void validateEmailAvailability(String email, Long currentId) {
		userRepository.findByEmail(email)
				.filter(existing -> currentId == null || !existing.getId().equals(currentId))
				.ifPresent(existing -> {
					throw new ResourceConflictException("Ya existe un usuario con email: " + email);
				});
	}

	private void applyRequest(User user, UserRequestDTO request) {
		user.setNombre(request.nombre());
		user.setApellido(request.apellido());
		user.setEmail(request.email());
		user.setPassword(passwordEncoder.encode(request.password()));
		user.setRol(request.rol());
		user.setEnabled(request.enabled());
	}

	private static UserResponseDTO toResponse(User user) {
		return new UserResponseDTO(
				user.getId(),
				user.getNombre(),
				user.getApellido(),
				user.getEmail(),
				user.getRol(),
				user.getCreatedAt(),
				user.getUpdatedAt(),
				user.isEnabled());
	}
}
