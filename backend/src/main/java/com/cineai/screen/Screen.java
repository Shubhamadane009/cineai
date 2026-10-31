package com.cineai.screen;

import com.cineai.cinema.Cinema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "screens",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_screen_cinema_number",
        columnNames = {"cinema_id", "screen_number"})
)
public class Screen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cinema_id", nullable = false)
    private Cinema cinema;

    @Column(name = "screen_number", nullable = false)
    private int screenNumber;

    @Column(name = "total_seats", nullable = false)
    private int totalSeats;

    protected Screen() {
    }

    public Screen(Cinema cinema, int screenNumber, int totalSeats) {
        this.cinema = cinema;
        this.screenNumber = screenNumber;
        this.totalSeats = totalSeats;
    }

    public Long getId() { return id; }
    public Cinema getCinema() { return cinema; }
    public int getScreenNumber() { return screenNumber; }
    public int getTotalSeats() { return totalSeats; }
}