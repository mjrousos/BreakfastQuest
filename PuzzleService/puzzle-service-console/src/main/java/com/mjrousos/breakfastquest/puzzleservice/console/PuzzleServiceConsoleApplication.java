package com.mjrousos.breakfastquest.puzzleservice.console;

import java.io.IOException;

import com.mjrousos.breakfastquest.puzzleservice.GameState;
import com.mjrousos.breakfastquest.puzzleservice.InvalidInstructionException;
import com.mjrousos.breakfastquest.puzzleservice.PuzzleUtilities;
import com.mjrousos.breakfastquest.puzzleservice.models.Instruction;
import com.mjrousos.breakfastquest.puzzleservice.models.InstructionTypes;
import com.mjrousos.breakfastquest.puzzleservice.models.Items;

// NO SPRING

import com.mjrousos.breakfastquest.puzzleservice.models.Puzzle;
import com.mjrousos.breakfastquest.puzzleservice.models.Tiles;

import biz.source_code.utils.RawConsoleInput;

public class PuzzleServiceConsoleApplication {
	private static boolean quitRequested = false;
	private static GameState state;

	public static void main(String[] args) {

		// Create initial (empty) game state
        state = new GameState(new Puzzle(12, 12, 1));
        state.setCreatorMode(true);

		try {
			printHeader();

			while (!quitRequested) {
				System.out.println();
				displayPuzzle();
				displayOptions();
				processCommand();
			}

			System.out.println();
			System.out.println("- Done -");
		} catch (Exception exc) {
			System.out.println("Unexpected exception: " + exc.toString());
		} finally {
			// Clean-up
			try {
				RawConsoleInput.resetConsoleMode();
			} catch (IOException e) {
				System.out.println("Error resetting console mode: " + e.toString());
			}
		}
	}

	private static void displayPuzzle() {
		System.out.println(ConsoleRenderer.RenderGameState(state));
	}

	private static void processCommand() throws IOException, InvalidInstructionException {
		// Annoyance : No ReadKey equivalent. So this becomes unnecessarily complicated
		// :(

		boolean keepReading = true;
		while (keepReading) {
			keepReading = false;
			int key = RawConsoleInput.read(true);
			switch (key) {
				case (int) 'Q':
				case (int) 'q':
					quitRequested = true;
                    break;
                case 57416: // Up
                    instruct(new Instruction(InstructionTypes.Up));
                    break;
                case 57419: // Left
                    instruct(new Instruction(InstructionTypes.Left));
                    break;
                case 57421: // Right
                    instruct(new Instruction(InstructionTypes.Right));
                    break;
                case 57424: // Down
                    instruct(new Instruction(InstructionTypes.Down));
                    break;
                case (int) 'T':
                case (int) 't':
                    int targetTile = getTargetTileType();
                    if (targetTile >= 0) {
                        instruct(new Instruction(InstructionTypes.Terraform, targetTile));
                    }
                    break;
                case (int) 'S':
                case (int) 's':
                    displayBoardString();
                    break;
                case (int) 'L':
                case (int) 'l':
                    ReadAndLoadBoard();
                    break;
                case (int) 'N':
                case (int) 'n':
                    ReadNewBoardParameters();
                    break;
                case (int) 'D':
                case (int) 'd':
                    int targetItem = getTargetItemType();
                    if (targetItem >= 0) {
                        instruct(new Instruction(InstructionTypes.Drop, targetItem));
                    }
                    break;
                case (int) 'P':
                case (int) 'p':
                    instruct(new Instruction(InstructionTypes.Pickup));
                    break;
				default:
					keepReading = true;
			}
		}
	}

	private static void ReadNewBoardParameters() {
        // Annoyance: No try parse :(
        // I guess that makes sense since Java doesn't have out or ref parameters.
        int width = 0;
        int height = 0;
        boolean createEmpty = false;

        while (width == 0) {
            try {
                System.out.print("Width: ");
                width = Integer.parseInt(System.console().readLine());
            } catch(NumberFormatException e) {
                // Annoyance: Why do I need to specify a variable identifier for the exception when
                // I have no intention of using it.
                System.out.println("Please enter an integer width");
            }
        }

        while (height == 0) {
            try {
                System.out.print("Height: ");
                height = Integer.parseInt(System.console().readLine());
            } catch(NumberFormatException e) {
                // Annoyance: Why do I need to specify a variable identifier for the exception when
                // I have no intention of using it.
                System.out.println("Please enter an integer height");
            }
        }

        System.out.print("Create empty board ('Y' for yes): ");
        String createEmptyAnswer = System.console().readLine();
        createEmpty = (createEmptyAnswer.equalsIgnoreCase("y") || createEmptyAnswer.equalsIgnoreCase("yes"));

        state = new GameState(new Puzzle(width, height, 1, createEmpty ? Tiles.None : Tiles.Grass));
        state.setCreatorMode(true);
	}

	private static void ReadAndLoadBoard() {
        System.out.println("Enter board state string:");
        String boardStateString = System.console().readLine().trim();
        state.setPositionX(0);
        state.setPositionY(0);
        short[][] newBoardState = PuzzleUtilities.boardStateFromString(boardStateString);
        if (newBoardState.length < 1) {
            System.out.println("Invalid board state");
            return;
        }
        state.setBoardState(newBoardState);
	}

	private static void displayBoardString() {
        System.out.println("Board state string:");
        System.out.println("----- ----- ----- ----- -----");
        System.out.println(PuzzleUtilities.boardStateToString(state.getBoardState()));
        System.out.println("----- ----- ----- ----- -----");
	}

	private static int getTargetTileType() throws IOException {
        //                  ---------1---------2---------3---------4---------5---------6---------7---------
        System.out.println("(1)None, (2)Grass, (3)Log, (4)Rock, (5)Tree, (6)Water, (7)Bridge, (8)LogBridge");
        switch (RawConsoleInput.read(true)) {
            case '1':
                return Tiles.None.ordinal();
            case '2':
                return Tiles.Grass.ordinal();
            case '3':
                return Tiles.Log.ordinal();
            case '4':
                return Tiles.Rock.ordinal();
            case '5':
                return Tiles.Tree.ordinal();
            case '6':
                return Tiles.Water.ordinal();
            case '7':
                return Tiles.BridgedWater.ordinal();
            case '8':
                return Tiles.LogOnBridgedWater.ordinal();
            default:
                return -1;
        }
    }

    private static int getTargetItemType() throws IOException {
        //                  ---------1---------2---------3---------4---------5---------6---------7---------
        System.out.println("(1)None, (2)Breakfast, (3)Acorn, (4)Apple, (5)Carrot, (6)Fish, (7)Mushroom, ");
        System.out.println("(8)Pear, (9)Strawberry");
        switch (RawConsoleInput.read(true)) {
            case '1':
                return Items.None.ordinal();
            case '2':
                return Items.Breakfast.ordinal();
            case '3':
                return Items.Acorn.ordinal();
            case '4':
                return Items.Apple.ordinal();
            case '5':
                return Items.Carrot.ordinal();
            case '6':
                return Items.Fish.ordinal();
            case '7':
                return Items.Mushroom.ordinal();
            case '8':
                return Items.Pear.ordinal();
            case '9':
                return Items.Strawberry.ordinal();
            default:
                return -1;
        }
	}

	private static void instruct(Instruction instruction) throws InvalidInstructionException {
        // TODO
        state.getInstructions()[0] = instruction;
        state.setInstructionPointer(0);
        state.step();
	}

	private static void displayOptions() {
		System.out.println("(N)ew, (L)oad, (S)ave, (P)ick-up, (D)rop, (T)erraform, (Q)uit, or move (arrows)");
	}

	private static void printHeader() {
		System.out.println("---------------------------------");
		System.out.println("- Breakfast Quest Puzzle Editor -");
		System.out.println("---------------------------------");
		System.out.println();
	}
}

/*
 *
 * // SPRING
 *
 * import org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.context.ApplicationContext; import
 * org.springframework.context.annotation.AnnotationConfigApplicationContext;
 * import org.springframework.context.annotation.Bean; import
 * org.springframework.context.annotation.ComponentScan; import
 * org.springframework.context.annotation.Configuration; import
 * org.springframework.stereotype.Component;
 *
 * import com.mjrousos.breakfastquest.puzzleservice.PuzzleService;
 *
 * @Component
 *
 * @Configuration
 *
 * @ComponentScan public class PuzzleServiceConsoleApplication {
 *
 * @Autowired private PuzzleService puzzleService;
 *
 * public static void main(String[] args) { ApplicationContext ctx = new
 * AnnotationConfigApplicationContext(PuzzleServiceConsoleApplication.class);
 * PuzzleServiceConsoleApplication app =
 * ctx.getBean(PuzzleServiceConsoleApplication.class); app.run(args); }
 *
 * private void run(String[] args) { final String message =
 * puzzleService.Echo("Hello!");
 * System.out.println("Response from puzzle service echo API: " + message); }
 *
 * @Bean public PuzzleService puzzleService() { return new PuzzleService(); } }
 */

/*
 *
 * // SPRING BOOT
 *
 * import org.springframework.boot.Banner; import
 * org.springframework.boot.CommandLineRunner; import
 * org.springframework.boot.SpringApplication; import
 * org.springframework.boot.autoconfigure.SpringBootApplication;
 *
 * @SpringBootApplication public class PuzzleServiceConsoleApplication
 * implements CommandLineRunner {
 *
 * @Autowired private PuzzleService puzzleService;
 *
 * public static void main(String[] args) { SpringApplication app = new
 * SpringApplication(PuzzleServiceConsoleApplication.class);
 * app.setBannerMode(Banner.Mode.OFF); app.run(args); }
 *
 * @Override public void run(String... args) {
 * System.out.println("Hello, world!"); }
 *
 * @Bean public PuzzleService puzzleService() { return new PuzzleService(); } }
 */
