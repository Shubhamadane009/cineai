package com.cineai.cinema.dto;

import com.cineai.cinema.Cinema;

public record CinemaResponse(
        Long id,
        String name,
        String city,
        String address) {

    public static CinemaResponse from(Cinema cinema) {
        return new CinemaResponse(
                cinema.getId(),
                cinema.getName(),
                cinema.getCity(),
                cinema.getAddress());
    }
}