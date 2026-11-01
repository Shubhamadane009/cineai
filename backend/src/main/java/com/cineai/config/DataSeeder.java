package com.cineai.config;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cineai.cinema.Cinema;
import com.cineai.cinema.CinemaRepository;
import com.cineai.movie.Genre;
import com.cineai.movie.Movie;
import com.cineai.movie.MovieRepository;
import com.cineai.screen.Screen;
import com.cineai.screen.ScreenRepository;
import com.cineai.show.Show;
import com.cineai.show.ShowRepository;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private static final int DAYS_AHEAD = 7;

    // Slots are spaced so that the longest seeded movie (160 min) never
    // overlaps the next slot on the same screen.
    private static final LocalTime[] SLOTS = {
            LocalTime.of(10, 0),
            LocalTime.of(14, 0),
            LocalTime.of(18, 0),
            LocalTime.of(21, 30)
    };
    private static final BigDecimal[] SLOT_PRICES = {
            new BigDecimal("180.00"),
            new BigDecimal("220.00"),
            new BigDecimal("260.00"),
            new BigDecimal("300.00")
    };

    private final MovieRepository movieRepository;
    private final CinemaRepository cinemaRepository;
    private final ScreenRepository screenRepository;
    private final ShowRepository showRepository;

    public DataSeeder(MovieRepository movieRepository,
                      CinemaRepository cinemaRepository,
                      ScreenRepository screenRepository,
                      ShowRepository showRepository) {
        this.movieRepository = movieRepository;
        this.cinemaRepository = cinemaRepository;
        this.screenRepository = screenRepository;
        this.showRepository = showRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        // Idempotent: restarting the app must not create duplicates.
        if (movieRepository.count() > 0) {
            log.info("Seed data skipped: movies already exist");
            return;
        }

        List<Movie> movies = movieRepository.saveAll(buildMovies());
        List<Cinema> cinemas = cinemaRepository.saveAll(buildCinemas());
        List<Screen> screens = screenRepository.saveAll(buildScreens(cinemas));
        List<Show> shows = showRepository.saveAll(buildShows(movies, screens));

        log.info("Seeded {} movies, {} cinemas, {} screens, {} shows",
                movies.size(), cinemas.size(), screens.size(), shows.size());
    }

    private List<Movie> buildMovies() {
        LocalDate base = LocalDate.now().minusMonths(2);
        return List.of(
                movie("Midnight Ledger", Genre.THRILLER, "English", 118, base,
                        "An accountant finds a second set of books that someone will kill to keep hidden."),
                movie("The Silent Witness", Genre.THRILLER, "Hindi", 132, base,
                        "A deaf courier is the only person who saw the crime."),
                movie("Cold Harbor", Genre.THRILLER, "English", 109, base,
                        "A detective returns to a frozen port town to close an old case."),
                movie("Laugh Track", Genre.COMEDY, "Hindi", 124, base,
                        "A failing stand-up comic accidentally becomes a viral sensation."),
                movie("Monsoon Letters", Genre.ROMANCE, "Marathi", 138, base,
                        "Two strangers exchange letters through one rainy season in Pune."),
                movie("Starfall Protocol", Genre.SCI_FI, "English", 148, base,
                        "A crew has 72 hours to stop a falling satellite from erasing a city."),
                movie("Iron Circuit", Genre.ACTION, "Hindi", 156, base,
                        "A retired racer is pulled back into an underground street circuit."),
                movie("The Hollow Hour", Genre.HORROR, "English", 104, base,
                        "A night-shift nurse notices the clock stops at the same minute each night."),
                movie("Quiet Rivers", Genre.DRAMA, "English", 160, base,
                        "Three generations of a family gather to decide the fate of their ancestral home.")
        );
    }

    private Movie movie(String title, Genre genre, String language, int minutes,
                        LocalDate releaseDate, String description) {
        String poster = "https://placehold.co/300x450?text=" + title.replace(' ', '+');
        return new Movie(title, description, genre, language, minutes, releaseDate, poster);
    }

    private List<Cinema> buildCinemas() {
        return List.of(
                new Cinema("CineAI Plaza", "Pune", "12 FC Road, Shivajinagar"),
                new Cinema("Orion Multiplex", "Pune", "45 Baner Road, Baner"),
                new Cinema("Harbor Screens", "Mumbai", "7 Marine Drive, Churchgate")
        );
    }

    private List<Screen> buildScreens(List<Cinema> cinemas) {
        // Cinema 1: 3 screens, cinema 2: 2 screens, cinema 3: 2 screens
        int[] screenCounts = {3, 2, 2};
        int[] seatCounts = {120, 100, 80};

        List<Screen> screens = new ArrayList<>();
        for (int c = 0; c < cinemas.size(); c++) {
            for (int s = 1; s <= screenCounts[c]; s++) {
                screens.add(new Screen(cinemas.get(c), s, seatCounts[(c + s) % seatCounts.length]));
            }
        }
        return screens;
    }

    private List<Show> buildShows(List<Movie> movies, List<Screen> screens) {
        List<Show> shows = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (int day = 0; day < DAYS_AHEAD; day++) {
            LocalDate date = today.plusDays(day);
            for (int s = 0; s < screens.size(); s++) {
                for (int slot = 0; slot < SLOTS.length; slot++) {
                    // Rotate movies so each screen/day/slot gets variety.
                    Movie movie = movies.get((s * 3 + day * 2 + slot) % movies.size());
                    LocalDateTime start = LocalDateTime.of(date, SLOTS[slot]);
                    LocalDateTime end = start.plusMinutes(movie.getDurationMinutes());
                    shows.add(new Show(movie, screens.get(s), start, end, SLOT_PRICES[slot]));
                }
            }
        }
        return shows;
    }
}