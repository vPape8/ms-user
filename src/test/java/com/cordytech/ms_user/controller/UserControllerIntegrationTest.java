package com.cordytech.ms_user.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.RecordComponent;
import java.util.List;

import com.cordytech.ms_user.dto.UserRequestDTO;
import com.cordytech.ms_user.dto.UserResponseDTO;
import com.cordytech.ms_user.exception.ResourceNotFoundException;
import com.cordytech.ms_user.model.Role;
import com.cordytech.ms_user.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

/**
 * Verifies basic user flow with real Spring components.
 */
@SpringBootTest
@Transactional
class UserControllerIntegrationTest {

	@Autowired
	private UserController userController;

	@Autowired
	private UserRepository userRepository;

	@BeforeEach
	void cleanData() {
		userRepository.deleteAll();
	}

	@Test
	void shouldExecuteCrudFlowWithoutExposingPassword() {
		UserRequestDTO createRequest = new UserRequestDTO(
				"Ana",
				"Perez",
				"ana@example.com",
				"AnaPass123",
				Role.USER,
				true);

		UserResponseDTO created = userController.create(createRequest).getBody().data();
		assertNotNull(created.id());
		assertEquals("ana@example.com", created.email());

		assertNoPasswordInResponseContract();

		String persistedPassword = userRepository.findById(created.id())
				.orElseThrow()
				.getPassword();
		assertFalse("AnaPass123".equals(persistedPassword));
		assertTrue(persistedPassword.startsWith("$2"));

		List<UserResponseDTO> users = userController.findAll().getBody().data();
		assertEquals(1, users.size());

		UserResponseDTO found = userController.findById(created.id()).getBody().data();
		assertEquals(created.id(), found.id());

		UserRequestDTO updateRequest = new UserRequestDTO(
				"Ana Maria",
				"Perez Soto",
				"ana.maria@example.com",
				"AnaPass456",
				Role.ADMIN,
				true);

		UserResponseDTO updated = userController.update(created.id(), updateRequest).getBody().data();
		assertEquals("ana.maria@example.com", updated.email());
		assertEquals(Role.ADMIN, updated.rol());

		userController.delete(created.id());
		assertTrue(userRepository.findById(created.id()).isEmpty());

		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
				() -> userController.findById(created.id()));
		assertNotNull(exception.getMessage());
	}

	private static void assertNoPasswordInResponseContract() {
		boolean containsPassword = false;
		for (RecordComponent component : UserResponseDTO.class.getRecordComponents()) {
			if ("password".equals(component.getName())) {
				containsPassword = true;
				break;
			}
		}
		assertFalse(containsPassword);
	}
}
