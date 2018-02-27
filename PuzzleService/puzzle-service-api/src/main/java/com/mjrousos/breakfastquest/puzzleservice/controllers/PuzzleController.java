package com.mjrousos.breakfastquest.puzzleservice.controllers;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import com.mjrousos.breakfastquest.puzzleservice.GameState;
import com.mjrousos.breakfastquest.puzzleservice.InvalidInstructionException;
import com.mjrousos.breakfastquest.puzzleservice.exceptions.InvalidInstructionsException;
import com.mjrousos.breakfastquest.puzzleservice.exceptions.PuzzleNotFoundException;
import com.mjrousos.breakfastquest.puzzleservice.models.Instruction;
import com.mjrousos.breakfastquest.puzzleservice.models.InstructionTypes;
import com.mjrousos.breakfastquest.puzzleservice.models.PuzzleDto;
import com.mjrousos.breakfastquest.puzzleservice.models.SolutionRequirements;
import com.mjrousos.breakfastquest.puzzleservice.models.SolutionResponseDTO;
import com.mjrousos.breakfastquest.puzzleservice.repositories.PuzzleRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

// TODO : Add auth

@Api(tags = "Puzzle Controller") // https://reflectoring.io/documenting-spring-data-rest-api-with-springfox/
@Controller
@RequestMapping("/puzzles")
public class PuzzleController {

    @Value("${breakfastQuest.maxSteps}")
    private int maxSteps;

    @Value("${breakfastQuest.twoStarAllowance}")
    private double twoStarAllowance;

    private final PuzzleRepository puzzles;
    private static final Logger logger = LoggerFactory.getLogger(PuzzleController.class);

    @Autowired
    public PuzzleController(PuzzleRepository puzzles) {
        this.puzzles = puzzles;
    }

    // Annoyance: CompletableFutures are an ok way of making this async,
    // but much harder to use than async/await for cases involving multiple
    // async calls.
    @ApiOperation("Returns all puzzles")
    @RequestMapping(method=RequestMethod.GET)
    public @ResponseBody Future<List<PuzzleDto>> findAllPuzzles() {
        return CompletableFuture.supplyAsync(() -> puzzles.findAll());
    }

    @ApiOperation("Finds a puzzle by ID")
    @RequestMapping(value="/{id}", method=RequestMethod.GET)
    public @ResponseBody Future<PuzzleDto> findPuzzle(@PathVariable(name = "id", required = true) int id) {
        return CompletableFuture.supplyAsync(() -> {
            PuzzleDto puzzle = puzzles.findOne(id);
            if (puzzle == null) {
                throw new PuzzleNotFoundException(id);
            }

            return puzzle;
        });
    }

    @ApiOperation("Create a new puzzle")
    @RequestMapping(method = RequestMethod.POST)
    public Future<ResponseEntity<Void>> addPuzzle(@RequestBody PuzzleDto newPuzzle) {
        if (newPuzzle.isValid()) {
            newPuzzle.lastUpdated = DateTime.now();

            // Annoyance: This has to be outside of the completable future because the
            // the current servlet request isn't available when the completable future runs on
            // another thread.
            UriComponentsBuilder resultLocationBuilder = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}");

            return CompletableFuture.supplyAsync(() -> {
                PuzzleDto result = puzzles.save(newPuzzle);
                logger.info("Created new puzzle {} \"{}\": {}", result.id, result.title, result.boardStateString);
                return ResponseEntity.created(resultLocationBuilder.buildAndExpand(result.id).toUri()).build();
            });
        } else  {
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
        }
    }

    @ApiOperation("Update a puzzle")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Future<ResponseEntity<PuzzleDto>> updatePuzzle(@PathVariable("id") int id, @RequestBody PuzzleDto updatedPuzzleDto) {
        PuzzleDto puzzle = puzzles.findOne(id);
        if (puzzle == null) {
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }

        updatedPuzzleDto.id = puzzle.id;
        updatedPuzzleDto.lastUpdated = DateTime.now();
        return CompletableFuture.supplyAsync(() -> {
            logger.info("Updated puzzle {}: {}", updatedPuzzleDto.id, updatedPuzzleDto.boardStateString);
            return ResponseEntity.ok(puzzles.save(updatedPuzzleDto));
        });
    }

    @ApiOperation("Delete a puzzle by ID")
    @RequestMapping(value="/{id}", method=RequestMethod.DELETE)
    public Future<ResponseEntity<Void>> deletePuzzle(@PathVariable(name = "id", required = true) int id) {
        CompletableFuture<PuzzleDto> puzzleFuture = CompletableFuture.supplyAsync(() -> {
            return puzzles.findOne(id);
        });

        return puzzleFuture.thenApplyAsync(puzzle -> {
            if (puzzle == null) {
                return ResponseEntity.notFound().build();
            }

            puzzles.delete(puzzle);
            return ResponseEntity.ok().build();
        });
    }

    @ApiOperation("Solve a puzzle")
    @RequestMapping(value="/solve/{id}", method=RequestMethod.PUT)
    public Future<ResponseEntity<SolutionResponseDTO>> solvePuzzle(@PathVariable(name = "id", required = true) int id, @RequestBody Instruction[] instructions) {
        if (instructions == null) {
            throw new InvalidInstructionsException();
        }

        Instruction[] preparedInstructions = trimTrailingNoOps(instructions);

        // Lookup puzzle
        CompletableFuture<PuzzleDto> puzzleFuture = CompletableFuture.supplyAsync(() -> {
            return puzzles.findOne(id);
        });

        // Apply instructions
        return puzzleFuture.thenApplyAsync(puzzleDto -> {
            if (puzzleDto == null) {
                return ResponseEntity.notFound().build();
            }

            // Check target instruction length
            if (preparedInstructions.length > puzzleDto.maxInstructionCount) {
                throw new InvalidInstructionsException();
            }

            // Load game state and get solution requirements
            GameState gameState = new GameState(puzzleDto.toPuzzle());
            gameState.setInstructions(preparedInstructions);
            SolutionRequirements solutionRequirements = puzzleDto.solution.toSolutionRequirements();
            SolutionResponseDTO response = new SolutionResponseDTO();

            // Step until solved or out of steps/instructions
            int steps = 0;
            try {
                do {
                    if (gameState.isSolved(solutionRequirements)) {
                        reportSolved(puzzleDto, preparedInstructions.length, steps, response);
                        break;
                    }
                } while (steps++ < maxSteps && gameState.step());

                // Final check in case the puzzle was solved with the last step
                if (gameState.isSolved(solutionRequirements)) {
                    reportSolved(puzzleDto, preparedInstructions.length, steps, response);
                }
			} catch (InvalidInstructionException e) {
				throw new InvalidInstructionsException();
			}

            if (response.score == 0) {
                logger.info("Puzzle {} not solved with {} instructions and {} steps earning {} stars", puzzleDto.id, preparedInstructions.length, steps, response.score);
            }

            return ResponseEntity.ok(response);
        });
    }

    // Annoyance : Can't fairly call this an annoyance since C# only just got this feature, but a nested method would be nice here.
	private void reportSolved(PuzzleDto puzzleDto, int instructionsCount, int steps,SolutionResponseDTO response) {
		response.score = getScoreFromInstructionCount(puzzleDto.targetInstructionCount, instructionsCount);
        logger.info("Puzzle {} solved with {} instructions and {} steps earning {} stars", puzzleDto.id, instructionsCount, steps, response.score);
	}

    // Trim trailing no-ops from solution instructions
	private Instruction[] trimTrailingNoOps(Instruction[] instructions) {
        int endIndex = instructions.length - 1;
        while (endIndex >= 0 && instructions[endIndex].getType() == InstructionTypes.Noop) {
            endIndex--;
        }

		Instruction[] preparedInstructions = new Instruction[endIndex + 1];
        for (int i = 0; i < endIndex + 1; i++) {
            preparedInstructions[i] = instructions[i];
        }

        return preparedInstructions;
	}

	private int getScoreFromInstructionCount(int targetInstructionCount, int instructionCount) {
		if (instructionCount <= targetInstructionCount) {
            return 3;
        } else if (instructionCount <= targetInstructionCount * (1 + twoStarAllowance)) {
            return 2;
        } else {
            return 1;
        }
	}
}
