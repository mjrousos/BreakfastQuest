package com.mjrousos.breakfastquest.puzzleservice;

import java.util.ArrayDeque;
import java.util.Deque;

import com.mjrousos.breakfastquest.puzzleservice.models.Instruction;
import com.mjrousos.breakfastquest.puzzleservice.models.Items;
import com.mjrousos.breakfastquest.puzzleservice.models.Puzzle;
import com.mjrousos.breakfastquest.puzzleservice.models.SolutionRequirements;
import com.mjrousos.breakfastquest.puzzleservice.models.Tiles;

public class GameState {
	private Instruction[] instructions;
	private int instructionPointer;
	private int positionX;
	private int positionY;
	private byte orientation; // Right, Down, Left, Up
	private short[][] boardState;
	private int[] inventory;
	private boolean creatorMode;
	private Deque<Integer> callstack;

	public GameState() {
		// Initialize call stack
		setCallstack(new ArrayDeque<Integer>());
	}

    public GameState(Puzzle puzzle) {
    	this();
    	setBoardState(puzzle.getBoardState().clone());
    	setInstructions(new Instruction[puzzle.getMaxInstructionCount()]);
    	setPositionX(puzzle.getStartingPositionX());
    	setPositionY(puzzle.getStartingPositionY());
    	setOrientation(puzzle.getStartingOrientation());
    	setInventory(puzzle.getStartingInventory().clone());
    }

	public Boolean step() throws InvalidInstructionException {
		if (instructionPointer >= instructions.length) {
			return false;
		}

		switch (instructions[instructionPointer].getType()) {
			case Noop:
				instructionPointer++;
				break;
			case Right:
				move(1, 0);
				instructionPointer++;
				break;
			case Left:
				move(-1, 0);
				instructionPointer++;
				break;
			case Up:
				move(0, -1);
				instructionPointer++;
				break;
			case Down:
				move(0, 1);
				instructionPointer++;
				break;
			case TurnRight:
				setOrientation((byte) ((getOrientation() + 1) % 4));
				instructionPointer++;
				break;
			case TurnLeft:
				setOrientation((byte) ((getOrientation() - 1) % 4));
				instructionPointer++;
				break;
			case Forward:
				// Annoyance
				// Java doesn't allow passing by reference!
				// Since it also doesn't have multiple returns, out parameters, etc.,
				// I have to pass an array which is confusing and error-prone
				int[] shifts = new int[2];
				getShiftsFromOrientation(shifts);
				move(shifts[0], shifts[1]);
				instructionPointer++;
				break;
			case Backward:
				int[] backShifts = new int[2];
				getShiftsFromOrientation(backShifts);
				move(-backShifts[0], -backShifts[1]);
				instructionPointer++;
				break;
			case Pickup:
				// Get the current tile info
				short tile = getTile(getPositionX(), getPositionY());

				// Check the item type and, if not empty, add to inventory
				Items itemType = Items.getItemType(tile);
				if (itemType != Items.None) {
					getInventory()[itemType.ordinal()]++;
				}

				// Update the current tile to have no item
				setTile(getPositionX(), getPositionY(), Items.removeItem(tile));
				instructionPointer++;
				break;
			case Drop:
				// TODO : Bounds checking
				Items itemToDrop = Items.values()[instructions[instructionPointer].getTarget()];
				tile = getTile(getPositionX(), getPositionY());
				if (itemToDrop != Items.None && Items.getItemType(tile) == Items.None) {
                    // Check that the player either has the proper item in inventory or is in creator mode
                    if (getInventory()[itemToDrop.ordinal()] > 0 || isCreatorMode()) {
                        if (!isCreatorMode()) {
                            // Remove item from inventory
                            getInventory()[itemToDrop.ordinal()]--;
                        }

                        // Place on tile
                        setTile(getPositionX(), getPositionY(), Items.placeItem(tile, itemToDrop));
                    }
				}
				instructionPointer++;
				break;
			case Terraform:
				if (!isCreatorMode()) {
					throw new InvalidInstructionException("Tile type may only be set with creator mode enabled");
				}
				Tiles newTileType = Tiles.values()[instructions[instructionPointer].getTarget()];
				tile = getTile(getPositionX(), getPositionY());
				setTile(getPositionX(), getPositionY(), Tiles.setTileType(tile, newTileType));
				instructionPointer++;
				break;

			// TODO : Branch*, call, return

			default:
				throw new InvalidInstructionException("Unknown instruction: " + instructions[instructionPointer].toString());
		}

		return instructionPointer < instructions.length;
	}

	private boolean move(int shiftX, int shiftY) {
		int destX = positionX + shiftX;
		int destY = positionY + shiftY;

        // Don't move off the edge of the board
		if (destX < 0 || destX >= getWidth() || destY < 0 || destY >= getHeight())
		{
			return false;
        }

        // Allow movement on all terrain in creator mode
        if (isCreatorMode()) {
            positionX = destX;
            positionY = destY;
            return true;
        }

		Tiles tileType = Tiles.getTileType(getTile(destX, destY));

		switch(tileType) {
			case BridgedWater:
			case Grass:
				// Move
				positionX = destX;
				positionY = destY;
				return true;
			case Log:
			case LogOnBridgedWater:
				if (moveLog(destX, destY, shiftX, shiftY)) {
					// If the log is moved, move animal
					positionX = destX;
					positionY = destY;
					return true;
				} else {
					// Log won't move
					return false;
				}
			default:
				// No-op for inaccessible tile types
				return false;

		}
	}

	private boolean moveLog(int logX, int logY, int shiftX, int shiftY) {
		int destX = logX + shiftX;
		int destY = logY + shiftY;

		// Check that the log is not being pushed out-of-bounds
		if (destX < 0 || destY < 0 || destX >= getWidth() || destY >= getHeight()) {
			return false;
		}

		Tiles destTile = Tiles.getTileType(getTile(destX, destY));
		switch (destTile) {
			case Grass:
			case BridgedWater:
			case Water:
				// Move log (update this tile and dest, return true)
				setTile(logX, logY, Tiles.removeLog(getTile(logX, logY)));
				setTile(destX, destY, Tiles.addLog(getTile(destX, destY)));
				return true;
			case Log:
			case LogOnBridgedWater:
				// Attempt to move the next log
				if (moveLog(destX, destY, shiftX, shiftY)) {
					// If next log moved, move this log and return true
					setTile(logX, logY, Tiles.removeLog(getTile(logX, logY)));
					setTile(destX, destY, Tiles.addLog(getTile(destX, destY)));
					return true;
				} else {
					// If next log did not move, then do not move this one, return false
					return false;
				}
			default:
				// Log cannot move
				return false;
		}
	}

	public void getShiftsFromOrientation(int[] shifts) {
		if (shifts.length < 2) {
			throw new IllegalArgumentException("Shifts array must contain at least two elements");
		}

		switch (getOrientation()) {
		case 0:
			shifts[0] = 1;
			shifts[1] = 0;
			break;
		case 1:
			shifts[0] = 0;
			shifts[1] = 1;
			break;
		case 2:
			shifts[0] = -1;
			shifts[1] = 0;
			break;
		case 3:
			shifts[0] = 0;
			shifts[1] = -1;
			break;
		}
	}

    // TODO : Change to return 'solution check report' instead of a bool which includes
    // 		  reasons the solution is wrong, if applicable.
    public boolean isSolved(SolutionRequirements requirements) {
    	if (requirements == null) {
    		return false;
    	}

    	// Check inventory
    	int[] inventory = getInventory();
    	for (int i = 0; i < Items.values().length; i++) {
    		if (requirements.getMinInventoryCount(i) > inventory[i] || requirements.getMaxInventoryCount(i) < inventory[i]) {
    			// Incorrect number of item i
    			return false;
    		}
    	}

    	// Check board state
    	short[][] requiredBoardState = requirements.getBoardState();
        if (!PuzzleUtilities.boardStateSatisfiesSolution(getBoardState(), requiredBoardState)) {
            return false;
        }

    	return true;
    }

	public int getHeight() {
		return boardState.length;
	}

	public int getWidth() {
		if (boardState.length < 1) {
			return 0;
		}

		return boardState[0].length;
	}

	public short getTile(int x, int y) {
		// TODO : Check bounds
		return boardState[y][x];
	}

	public void setTile(int x, int y, short tile) {
		// TODO : Check bounds
		boardState[y][x] = tile;
	}

	public Instruction[] getInstructions() {
		return instructions;
	}
	public void setInstructions(Instruction[] instructions) {
		this.instructions = instructions;
	}
	public int getInstructionPointer() {
		return instructionPointer;
	}
	public void setInstructionPointer(int instructionPointer) {
		this.instructionPointer = instructionPointer;
	}
	public int getPositionX() {
		return positionX;
	}
	public void setPositionX(int positionX) {
		this.positionX = positionX;
	}
	public int getPositionY() {
		return positionY;
	}
	public void setPositionY(int positionY) {
		this.positionY = positionY;
	}
	public short[][] getBoardState() {
		return boardState;
	}
	public void setBoardState(short[][] boardState) {
		this.boardState = boardState;
	}
	public int[] getInventory() {
		return inventory;
	}
	public void setInventory(int[] inventory) {
		this.inventory = inventory;
	}

	public boolean isCreatorMode() {
		return creatorMode;
	}

	public void setCreatorMode(boolean creatorMode) {
		this.creatorMode = creatorMode;
	}

	public Deque<Integer> getCallstack() {
		return callstack;
	}

	public void setCallstack(Deque<Integer> callstack) {
		this.callstack = callstack;
	}

	public byte getOrientation() {
		return orientation;
	}

	public void setOrientation(byte orientation) {
		this.orientation = orientation;
	}
}
