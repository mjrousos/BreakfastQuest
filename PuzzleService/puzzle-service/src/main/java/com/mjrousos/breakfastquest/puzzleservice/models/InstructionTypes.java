package com.mjrousos.breakfastquest.puzzleservice.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum InstructionTypes {
	Noop,
	Up,
	Down,
	Right,
	Left,
	Forward,
	Backward,
	TurnLeft,
	TurnRight,
	Pickup,
	Drop,
	Terraform,
	Branch,
	BranchIfTileContains,
	BranchIfInventoryEqualTo,
	BranchIfInventoryMoreThan,
	BranchIfInventoryLessThan,
	Call,
	Return;

	public static InstructionTypes[] getBasicInstructionTypes() {
		return new InstructionTypes[] { Up, Down, Left, Right }; 
	}
	
	public static InstructionTypes[] getStandardInstructionTypes() {
		List<InstructionTypes> ret = new ArrayList<InstructionTypes>(Arrays.asList(getBasicInstructionTypes()));
		ret.addAll(Arrays.asList(new InstructionTypes[]{ Pickup, Drop }));
		
		// Annoyance: Why do I have to pass a new [] to the toArray call? Doesn't it know what kind of array to create?
		//            In fact, most of this method would be simpler in C#.
		return ret.toArray(new InstructionTypes[ret.size()]);
	}
	
	public static InstructionTypes[] getAdvancedInstructionTypes() {
		List<InstructionTypes> ret = new ArrayList<InstructionTypes>(Arrays.asList(getStandardInstructionTypes()));
		ret.addAll(Arrays.asList(new InstructionTypes[]{ Branch, BranchIfTileContains, Call, Return }));
		
		return ret.toArray(new InstructionTypes[ret.size()]);
	}
	
	public static InstructionTypes[] getCreatorInstructionTypes() {
		List<InstructionTypes> ret = new ArrayList<InstructionTypes>(Arrays.asList(getStandardInstructionTypes()));
		ret.addAll(Arrays.asList(new InstructionTypes[]{ Terraform }));
		
		return ret.toArray(new InstructionTypes[ret.size()]);
	} 
}
