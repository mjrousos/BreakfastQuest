package com.mjrousos.breakfastquest.puzzleservice.models;

public class Puzzle {
    private int id;
    private String description;
    private String title;
    private byte dificulty;
    private boolean published;
    private int version;
    private String author;
    
    private short[][] boardState;
	private SolutionRequirements solution;
    private int targetInstructionCount;
    private int maxInstructionCount;
    private InstructionTypes[] availableInstructions;
    private int[] startingInventory;
    
	private int startingPositionX;
    private int startingPositionY;   
    
    // 0 - 3: Right, Down, Left, Up
    private byte startingOrientation;
    
    public Puzzle(int width, int height, int maxInstructions) {
    	// Create empty board and instruction array
    	setBoardState(new short[height][width]);
    	setMaxInstructionCount(maxInstructionCount);
    	
    	// Default solution requirements (1 breakfast)
    	setSolution(new SolutionRequirements());
    	
    	// Default to standard instruction set
    	setAvailableInstructions(InstructionTypes.getStandardInstructionTypes());
    	
    	// Empty inventory
    	setStartingInventory(new int[Items.values().length]);
    	 
    	// Set version
    	setVersion(1);
    }
    
    // Annoyance: This would be so much more succinct with properties
    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public byte getDificulty() {
		return dificulty;
	}

	public void setDificulty(byte dificulty) {
		this.dificulty = dificulty;
	}

	public boolean getPublished() {
		return published;
	}

	public void setPublished(boolean published) {
		this.published = published;
	}

	public short[][] getBoardState() {
		return boardState;
	}

	public void setBoardState(short[][] boardState) {
		this.boardState = boardState;
	}

	public SolutionRequirements getSolution() {
		return solution;
	}

	public void setSolution(SolutionRequirements solution) {
		this.solution = solution;
	}

	public int getTargetInstructionCount() {
		return targetInstructionCount;
	}

	public void setTargetInstructionCount(int targetMoveCount) {
		this.targetInstructionCount = targetMoveCount;
	}

	public InstructionTypes[] getAvailableInstructions() {
		return availableInstructions;
	}

	public void setAvailableInstructions(InstructionTypes[] availableInstructions) {
		this.availableInstructions = availableInstructions;
	}

	public int getStartingPositionX() {
		return startingPositionX;
	}

	public void setStartingPositionX(int startingPositionX) {
		this.startingPositionX = startingPositionX;
	}

	public int getStartingPositionY() {
		return startingPositionY;
	}

	public void setStartingPositionY(int startingPositionY) {
		this.startingPositionY = startingPositionY;
	}

	public int getMaxInstructionCount() {
		return maxInstructionCount;
	}

	public void setMaxInstructionCount(int maxInstructionCount) {
		this.maxInstructionCount = maxInstructionCount;
	}
	
    public int[] getStartingInventory() {
		return startingInventory;
	}

	public void setStartingInventory(int[] startingInventory) {
		this.startingInventory = startingInventory;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public byte getStartingOrientation() {
		return startingOrientation;
	}

	public void setStartingOrientation(byte startingOrientation) {
		this.startingOrientation = startingOrientation;
	}
}