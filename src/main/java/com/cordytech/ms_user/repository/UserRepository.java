package com.cordytech.ms_user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cordytech.ms_user.model.User;

/**
 * Data access contract for users.
 */
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);
}
