package com.cordytech.ms_user.exception;

/**
 * Raised when a requested resource does not exist.
 */
public class ResourceNotFoundException extends RuntimeException {

	public ResourceNotFoundException(String message) {
		super(message);
	}
}
