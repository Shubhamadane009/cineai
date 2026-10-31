package com.cineai.movie;

import java.util.List;
import java.util.Arrays;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cineai.common.exception.BadRequestException;
import com.cineai.common.exception.ResourceNotFoundException;
import com.cineai.movie.dto.MovieResponse;

@Service
@Transactional(readOnly = true)
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<MovieResponse> getMovies(String genre) {
        List<Movie> movies = (genre == null || genre.isBlank())
                ? movieRepository.findAll()
                : movieRepository.findByGenre(parseGenre(genre));

        return movies.stream().map(MovieResponse::from).toList();
    }

    public MovieResponse getMovieById(Long id) {
        return movieRepository.findById(id)
                .map(MovieResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id " + id));
    }

    private Genre parseGenre(String value) {
        try {
            return Genre.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException(
                    "Unknown genre '" + value + "'. Valid values: " + Arrays.toString(Genre.values()));
        }
    }
}