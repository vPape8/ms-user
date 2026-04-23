package com.cordytech.ms_user.exception;

/**
 * Raised when business constraints conflict with current state.
 */
public class ResourceConflictException extends RuntimeException {

	public ResourceConflictException(String message) {
		super(message);
	}
}
