package com.mjrousos.breakfastquest.puzzleservice;

import com.mjrousos.breakfastquest.puzzleservice.models.Items;
import com.mjrousos.breakfastquest.puzzleservice.models.Tiles;
import java.nio.ByteBuffer;
import java.util.Base64;

// Annoyance: No static classes? I guess that's fine...
public /*static*/ class PuzzleUtilities {
	public static final String boardStateToString(short[][] boardState) {
		return Base64.getEncoder().encodeToString(PuzzleUtilities.boardStateToBytes(boardState));
	}

	public static final short[][] boardStateFromString(String boardStateString) {
        try {
            return PuzzleUtilities.boardStateFromBytes(Base64.getDecoder().decode(boardStateString));
        } catch  (IllegalArgumentException e) {
            return new short[0][0];
        }
	}

	public static final short[][] boardStateFromBytes(byte[] bytes) {
		if (bytes.length < 10) {
			return new short[0][0];
		}

		ByteBuffer buffer = ByteBuffer.wrap(bytes);

		int height = buffer.getInt();
		int width = buffer.getInt();

		short[][] board = new short[height][width];

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				board[i][j] = buffer.getShort();
			}
        }

        return board;
	}

    // Serializes board state as:
    //  Byte range  | Purpose
    //  --------------------------
    //  0 - 3       | Board height
    //  4 - 7       | Board width
    //  Subsequent  | Every two bytes representing a width-first traversal of the board's tiles
    public static final byte[] boardStateToBytes(short[][] boardState) {
		if (boardState.length == 0 || boardState[0].length == 0) {
			return new byte[0];
		}

		int width = boardState.length;
		int height = boardState[0].length;

		ByteBuffer bytes = ByteBuffer.allocate(4 + 4 + 2 * width * height);

		bytes.putInt(height);
		bytes.putInt(width);

		for (short[] row : boardState) {
			if (row.length != width) {
				throw new IllegalArgumentException("Jagged boards are not supported");
			}

			for (short tile : row) {
				bytes.putShort(tile);
			}
		}

		return bytes.array();
    }

    public static final boolean boardStateSatisfiesSolution(short[][] boardState, short[][] requiredBoardState) {
        if (requiredBoardState != null) {
    		for (int i = 0; i < requiredBoardState.length; i++) { // For each row in required board state
    			for (int j = 0; j < requiredBoardState.length; j++) { // For each tile in row
    				Tiles requiredTileType = Tiles.getTileType(requiredBoardState[i][j]);
    				Items requiredItemType = Items.getItemType(requiredBoardState[i][j]);

    				// Only check if required state includes a non-none entry so that the
    				// required board state can be sparsely populated with just the bits that
    				// the solution cares about.
    				if (requiredTileType != Tiles.None) {
    					Tiles tileType = Tiles.getTileType(boardState[i][j]);
    					if (requiredTileType != tileType) {
    						return false;
    					}
    				}
    				if (requiredItemType != Items.None) {
    					Items itemType = Items.getItemType(boardState[i][j]);
    					if (requiredItemType != itemType) {
    						return false;
    					}
    				}
    			}
    		}
        }

        return true;
    }
}
