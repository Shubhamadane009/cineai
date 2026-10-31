package com.cineai.cinema;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cineai.cinema.dto.CinemaResponse;

@Service
@Transactional(readOnly = true)
public class CinemaService {

    private final CinemaRepository cinemaRepository;

    public CinemaService(CinemaRepository cinemaRepository) {
        this.cinemaRepository = cinemaRepository;
    }

    public List<CinemaResponse> getCinemas(String city) {
        List<Cinema> cinemas = (city == null || city.isBlank())
                ? cinemaRepository.findAll()
                : cinemaRepository.findByCityIgnoreCase(city.trim());

        return cinemas.stream().map(CinemaResponse::from).toList();
    }
}