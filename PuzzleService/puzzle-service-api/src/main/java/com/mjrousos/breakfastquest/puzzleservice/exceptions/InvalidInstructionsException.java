package com.mjrousos.breakfastquest.puzzleservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidInstructionsException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidInstructionsException() {
		super("Invalid instructions");
	}
}
