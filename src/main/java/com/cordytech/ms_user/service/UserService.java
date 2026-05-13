package com.cordytech.ms_user.service;

import java.util.List;

import com.cordytech.ms_user.dto.UserRequestDTO;
import com.cordytech.ms_user.dto.UserResponseDTO;

/**
 * Business contract for user operations.
 */
public interface UserService {

	UserResponseDTO create(UserRequestDTO request);

	List<UserResponseDTO> findAll();

	UserResponseDTO findById(Long id);

	UserResponseDTO update(Long id, UserRequestDTO request);

	void delete(Long id);

	UserResponseDTO validarLogin(String email, String password);
}
