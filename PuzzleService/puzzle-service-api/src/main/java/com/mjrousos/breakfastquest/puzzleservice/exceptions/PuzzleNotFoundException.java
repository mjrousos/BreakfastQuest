package com.mjrousos.breakfastquest.puzzleservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PuzzleNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public PuzzleNotFoundException(int puzzleId) {
		super("Could not find puzzle '" + puzzleId + "'.");
	}
}
