package com.mjrousos.breakfastquest.puzzleservice.models;

import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAccessor;

import com.mjrousos.breakfastquest.puzzleservice.PuzzleUtilities;

@Document(collection = "puzzles")
public class PuzzleDto {
    @Id
    public int id;

    public String description;
    public String title;
    public byte difficulty;
    public boolean published;
    public int version;
    public String author;
    public DateTime lastUpdated;

    public String boardStateString;
	public SolutionRequirementsDto solution;
    public int targetInstructionCount;
    public int maxInstructionCount;
    public InstructionTypes[] availableInstructions;
    public int[] startingInventory;

	public int startingPositionX;
    public int startingPositionY;

    // 0 - 3: Right, Down, Left, Up
    public byte startingOrientation;

    public Puzzle toPuzzle() {
        Puzzle ret = new Puzzle(1, 1, maxInstructionCount);
        ret.setBoardState(PuzzleUtilities.boardStateFromString(boardStateString));
        ret.setId(id);
        ret.setAuthor(author);
        ret.setAvailableInstructions(availableInstructions);
        ret.setDescription(description);
        ret.setDifficulty(difficulty);
        ret.setLastUpdated(OffsetDateTime.ofInstant(Instant.ofEpochMilli(lastUpdated.getMillis()), ZoneId.of(lastUpdated.getZone().getID())));
        ret.setPublished(published);
        ret.setSolution(solution.toSolutionRequirements());
        ret.setStartingInventory(startingInventory);
        ret.setStartingOrientation(startingOrientation);
        ret.setStartingPositionX(startingPositionX);
        ret.setStartingPositionY(startingPositionY);
        ret.setTargetInstructionCount(targetInstructionCount);
        ret.setTitle(title);
        ret.setVersion(version);

        return ret;
    }

	public boolean isValid() {
        // TODO
        return true;
	}
}
