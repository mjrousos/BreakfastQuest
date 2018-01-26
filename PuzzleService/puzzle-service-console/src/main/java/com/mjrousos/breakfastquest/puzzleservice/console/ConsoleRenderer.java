package com.mjrousos.breakfastquest.puzzleservice.console;

import com.mjrousos.breakfastquest.puzzleservice.GameState;
import com.mjrousos.breakfastquest.puzzleservice.models.Items;
import com.mjrousos.breakfastquest.puzzleservice.models.Tiles;

public class ConsoleRenderer {

    public static String RenderGameState(GameState state) {
        StringBuilder ret = new StringBuilder();
        short[][] boardState = state.getBoardState();
        int currentX = state.getPositionX();
        int currentY = state.getPositionY();

        // Build ASCII representation of board
        for (int y = 0; y < boardState.length; y++) {
            ret.append('|');
            for (int x = 0; x < boardState[y].length; x++) {
                if(x == currentX && y == currentY) {
                    ret.append(getAnimalCharForOrientation(state.getOrientation()));
                }
                else {
                    ret.append(getCharForTile(boardState[y][x]));
                }
                ret.append('|');
            }
            ret.append(System.lineSeparator());
        }

        // Display inventory information
        int[] inventory = state.getInventory();
        ret.append("Inventory:");
        for (int i = 1; i < Items.values().length; i++) {
            ret.append(" " + Items.values()[i].toString() + " (" + inventory[i] + ")");
        }
        ret.append(System.lineSeparator());

        // Show tile contents information
        ret.append("Current tile type: " + Tiles.getTileType(boardState[currentY][currentX]) + System.lineSeparator());
        ret.append("Current tile item: " + Items.getItemType(boardState[currentY][currentX]) + System.lineSeparator());

        return ret.toString();
    }

	private static Object getAnimalCharForOrientation(byte orientation) {
		switch(orientation) {
            case 0:
                return '>';
            case 1:
                return 'v';
            case 2:
                return '<';
            case 3:
                return '^';
            default:
                return 'o';
        }
	}

	private static char getCharForTile(short tile) {
		if (Items.getItemType(tile) == Items.Breakfast) {
            return '*';
        } else {
            switch (Tiles.getTileType(tile)) {
                case Rock:
                    return 'X';
                case Grass:
                    return ' ';
                case Tree:
                    return 'T';
                case Water:
                    return '~';
                case Log:
                    return '|';
                case BridgedWater:
                    return '=';
                case LogOnBridgedWater:
                    return '+';
                default:
                    return '?';
            }
        }
	}
}
