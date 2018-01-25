package com.mjrousos.breakfastquest.puzzleservice.models;

public class Instruction {
	private InstructionTypes type;
	private Short target;

    public Instruction(InstructionTypes type) {
        this(type, 0);
    }

    public Instruction(InstructionTypes type, int target) {
        this(type, (short)target);
    }

    public Instruction(InstructionTypes type, short target) {
        this.type = type;
        this.target = target;
    }

	public InstructionTypes getType() {
		return type;
    }

	public void setType(InstructionTypes type) {
		this.type = type;
	}
	public Short getTarget() {
		return target;
	}
	public void setTarget(Short target) {
		this.target = target;
	}

}
