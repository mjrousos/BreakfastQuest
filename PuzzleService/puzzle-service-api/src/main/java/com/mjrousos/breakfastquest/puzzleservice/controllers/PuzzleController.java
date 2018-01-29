package com.mjrousos.breakfastquest.puzzleservice.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.mjrousos.breakfastquest.puzzleservice.PuzzleNotFoundException;
import com.mjrousos.breakfastquest.puzzleservice.models.PuzzleDto;
import com.mjrousos.breakfastquest.puzzleservice.repositories.PuzzleRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "Puzzle Controller") // https://reflectoring.io/documenting-spring-data-rest-api-with-springfox/
@Controller
@RequestMapping("/puzzles")
public class PuzzleController {

    @Autowired
    private PuzzleRepository puzzles;

    @ApiOperation("Returns all puzzles")
    @RequestMapping(method=RequestMethod.GET)
    public @ResponseBody List<PuzzleDto> findAllPuzzles() {
        return puzzles.findAll();
    }

    @ApiOperation("Finds a puzzle by ID")
    @RequestMapping(value="/{id}", method=RequestMethod.GET)
    public @ResponseBody PuzzleDto findPuzzle(@PathVariable(name = "id", required = true) int id) {
        PuzzleDto puzzle = puzzles.findOne(id);
        if (puzzle == null) {
            throw new PuzzleNotFoundException(id);
        }

        return puzzle;
    }

    @ApiOperation("Create a new puzzle")
    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<Void> addPuzzle(@RequestBody PuzzleDto newPuzzle) {
        if (newPuzzle.isValid()) {
            PuzzleDto result = puzzles.save(newPuzzle);
            URI resultLocation = ServletUriComponentsBuilder
                                    .fromCurrentRequest()
                                    .path("/{id}")
                                    .buildAndExpand(result.id).toUri();
            return ResponseEntity.created(resultLocation).build();
        } else  {
            return ResponseEntity.badRequest().build();
        }
    }

    @ApiOperation("Update a puzzle")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<PuzzleDto> updatePuzzle(@PathVariable("id") int id, @RequestBody PuzzleDto updatedPuzzleDto) {
        PuzzleDto puzzle = puzzles.findOne(id);
        if (puzzle == null) {
            return ResponseEntity.notFound().build();
        }

        updatedPuzzleDto.id = puzzle.id;
        return new ResponseEntity<PuzzleDto>(puzzles.save(updatedPuzzleDto), HttpStatus.OK);
    }

    @ApiOperation("Delete a puzzle by ID")
    @RequestMapping(value="/{id}", method=RequestMethod.DELETE)
    public ResponseEntity<Void> deletePuzzle(@PathVariable(name = "id", required = true) int id) {
        PuzzleDto puzzle = puzzles.findOne(id);
        if (puzzle == null) {
            return ResponseEntity.notFound().build();
        }

        puzzles.delete(puzzle);

        return ResponseEntity.ok().build();
    }

    // TODO : Add solve API
}
