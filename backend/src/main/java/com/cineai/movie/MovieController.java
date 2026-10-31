package com.cineai.movie;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cineai.movie.dto.MovieResponse;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public List<MovieResponse> getMovies(@RequestParam(required = false) String genre) {
        return movieService.getMovies(genre);
    }

    @GetMapping("/{id}")
    public MovieResponse getMovie(@PathVariable Long id) {
        return movieService.getMovieById(id);
    }
}