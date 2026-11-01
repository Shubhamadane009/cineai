package com.cineai.show.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.cineai.show.Show;

public record ShowResponse(
        Long id,
        Long movieId,
        String movieTitle,
        Long cinemaId,
        String cinemaName,
        String city,
        int screenNumber,
        LocalDateTime startTime,
        LocalDateTime endTime,
        BigDecimal basePrice) {

    public static ShowResponse from(Show show) {
        return new ShowResponse(
                show.getId(),
                show.getMovie().getId(),
                show.getMovie().getTitle(),
                show.getScreen().getCinema().getId(),
                show.getScreen().getCinema().getName(),
                show.getScreen().getCinema().getCity(),
                show.getScreen().getScreenNumber(),
                show.getStartTime(),
                show.getEndTime(),
                show.getBasePrice());
    }
}