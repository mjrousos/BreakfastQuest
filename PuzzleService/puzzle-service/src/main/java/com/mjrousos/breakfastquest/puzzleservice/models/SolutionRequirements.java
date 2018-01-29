package com.mjrousos.breakfastquest.puzzleservice.models;

public class SolutionRequirements {

	private int[] minInventoryCounts;
	private int[] maxInventoryCounts;
	private short[][] boardState;

	public SolutionRequirements() {
		// Default to only expecting breakfast to be picked up
		int[] expectedInventory = new int[Items.values().length];
		expectedInventory[Items.Breakfast.ordinal()] = 1;

		minInventoryCounts = expectedInventory.clone();
		maxInventoryCounts = expectedInventory.clone();

	}

	public short[][] getBoardState() {
		return boardState;
	}
	public void setBoardState(short[][] boardState) {
		this.boardState = boardState;
	}

	public int getMinInventoryCount(int i) {
		return minInventoryCounts[i];
	}

	public int getMaxInventoryCount(int i) {
		return maxInventoryCounts[i];
	}

	public int[] getMinInventoryCounts() {
		return minInventoryCounts;
    }

	public int[] getMaxInventoryCounts() {
		return maxInventoryCounts;
	}

    public void setMinInventoryCounts(int[] counts) {
        this.minInventoryCounts = counts;
    }

    public void setMaxInventoryCounts(int[] counts) {
        this.maxInventoryCounts = counts;
    }
}
