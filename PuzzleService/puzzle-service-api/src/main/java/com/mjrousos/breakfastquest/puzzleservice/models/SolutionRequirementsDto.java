package com.mjrousos.breakfastquest.puzzleservice.models;

import com.mjrousos.breakfastquest.puzzleservice.PuzzleUtilities;

public class SolutionRequirementsDto {
	public int[] minInventoryCounts;
	public int[] maxInventoryCounts;
    public String boardStateString;

    public SolutionRequirementsDto() {

    }

    public SolutionRequirementsDto(SolutionRequirements requirements) {
        this.boardStateString = PuzzleUtilities.boardStateToString(requirements.getBoardState());
        this.maxInventoryCounts = requirements.getMaxInventoryCounts();
        this.minInventoryCounts = requirements.getMinInventoryCounts();
    }

    public SolutionRequirements toSolutionRequirements() {
        // Annoyance: No {} setting of local fields/properties on new type.
        // Annoyance: In case I haven't mentioned it recently, I miss var.
        SolutionRequirements ret = new SolutionRequirements();
        ret.setBoardState(PuzzleUtilities.boardStateFromString(this.boardStateString));
        ret.setMaxInventoryCounts(this.maxInventoryCounts);
        ret.setMinInventoryCounts(this.minInventoryCounts);

        return ret;
    }
}
