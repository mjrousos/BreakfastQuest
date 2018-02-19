package com.mjrousos.breakfastquest.puzzleservice.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    // Annoyance : int -> enum transition is ridiculously overcomplicated :(

    // Caches enum values by ordinal
    private static Map<Integer, InstructionTypes> valueMap = new HashMap<Integer, InstructionTypes>();

    // Annoyance: This is useful. I think in .NET you'd need to work from IL for static initialization code like this.
    //            Shouldn't often be needed, but I'm all for options.
    static {
        for (InstructionTypes i : values()) {
            valueMap.put(i.ordinal(), i);
        }
    }

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

    public static InstructionTypes valueOf(int index) {
        return valueMap.get(index);
    }
}
