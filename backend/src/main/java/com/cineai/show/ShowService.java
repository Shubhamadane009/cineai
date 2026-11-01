package com.cineai.show;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cineai.common.exception.ResourceNotFoundException;
import com.cineai.movie.MovieRepository;
import com.cineai.show.dto.ShowResponse;

@Service
@Transactional(readOnly = true)
public class ShowService {

    // When no date is given, return upcoming shows for this many days.
    private static final int DEFAULT_LOOKAHEAD_DAYS = 30;

    private final ShowRepository showRepository;
    private final MovieRepository movieRepository;

    public ShowService(ShowRepository showRepository, MovieRepository movieRepository) {
        this.showRepository = showRepository;
        this.movieRepository = movieRepository;
    }

    public List<ShowResponse> findShows(Long movieId, LocalDate date, String city) {
        if (!movieRepository.existsById(movieId)) {
            throw new ResourceNotFoundException("Movie not found with id " + movieId);
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime from;
        LocalDateTime to;

        if (date != null) {
            from = date.atStartOfDay();
            to = from.plusDays(1);
        } else {
            from = now;
            to = now.plusDays(DEFAULT_LOOKAHEAD_DAYS);
        }

        // Never return shows that have already started.
        if (from.isBefore(now)) {
            from = now;
        }

        List<Show> shows = (city == null || city.isBlank())
                ? showRepository.findByMovieAndTimeRange(movieId, from, to)
                : showRepository.findByMovieAndCityAndTimeRange(movieId, city.trim(), from, to);

        return shows.stream().map(ShowResponse::from).toList();
    }
}