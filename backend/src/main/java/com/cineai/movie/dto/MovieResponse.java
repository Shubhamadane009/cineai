package com.cineai.movie.dto;

import java.time.LocalDate;

import com.cineai.movie.Genre;
import com.cineai.movie.Movie;

public record MovieResponse(
        Long id,
        String title,
        String description,
        Genre genre,
        String language,
        int durationMinutes,
        LocalDate releaseDate,
        String posterUrl) {

    public static MovieResponse from(Movie movie) {
        return new MovieResponse(
                movie.getId(),
                movie.getTitle(),
                movie.getDescription(),
                movie.getGenre(),
                movie.getLanguage(),
                movie.getDurationMinutes(),
                movie.getReleaseDate(),
                movie.getPosterUrl());
    }
}