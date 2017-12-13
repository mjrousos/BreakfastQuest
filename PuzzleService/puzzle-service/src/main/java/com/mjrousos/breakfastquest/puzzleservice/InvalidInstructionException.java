package com.mjrousos.breakfastquest.puzzleservice;

public class InvalidInstructionException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public InvalidInstructionException(String message) {
		super(message);
	}
}
