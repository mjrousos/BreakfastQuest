package com.mjrousos.breakfastquest.puzzleservice.repositories;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.mjrousos.breakfastquest.puzzleservice.models.PuzzleDto;
import java.util.List;

public interface PuzzleRepository extends MongoRepository<PuzzleDto, Integer> {
    public List<PuzzleDto> findByDifficulty(byte difficulty);
    public List<PuzzleDto> findByAuthor(String author);
}
