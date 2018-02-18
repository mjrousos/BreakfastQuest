package com.mjrousos.breakfastquest.puzzleservice.controllers;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import com.mjrousos.breakfastquest.puzzleservice.PuzzleNotFoundException;
import com.mjrousos.breakfastquest.puzzleservice.models.PuzzleDto;
import com.mjrousos.breakfastquest.puzzleservice.repositories.PuzzleRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

// TODO : Add auth

@Api(tags = "Puzzle Controller") // https://reflectoring.io/documenting-spring-data-rest-api-with-springfox/
@Controller
@RequestMapping("/puzzles")
public class PuzzleController {

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
        PuzzleDto puzzle = puzzles.findOne(id);
        if (puzzle == null) {
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }

        return CompletableFuture.supplyAsync(() -> {
            puzzles.delete(puzzle);
            return ResponseEntity.ok().build();
        });
    }

    // TODO : Add solve API
}
