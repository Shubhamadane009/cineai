package com.cineai.show;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShowRepository extends JpaRepository<Show, Long> {

    // JOIN FETCH loads show + movie + screen + cinema in ONE query,
    // so mapping to the DTO later triggers no extra lazy-loading queries.
    @Query("""
            select s from Show s
            join fetch s.movie m
            join fetch s.screen sc
            join fetch sc.cinema c
            where m.id = :movieId
              and s.startTime >= :from
              and s.startTime < :to
            order by s.startTime
            """)
    List<Show> findByMovieAndTimeRange(@Param("movieId") Long movieId,
                                       @Param("from") LocalDateTime from,
                                       @Param("to") LocalDateTime to);

    @Query("""
            select s from Show s
            join fetch s.movie m
            join fetch s.screen sc
            join fetch sc.cinema c
            where m.id = :movieId
              and lower(c.city) = lower(:city)
              and s.startTime >= :from
              and s.startTime < :to
            order by s.startTime
            """)
    List<Show> findByMovieAndCityAndTimeRange(@Param("movieId") Long movieId,
                                              @Param("city") String city,
                                              @Param("from") LocalDateTime from,
                                              @Param("to") LocalDateTime to);
}