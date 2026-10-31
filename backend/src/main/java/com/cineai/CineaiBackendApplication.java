package com.cineai;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CineaiBackendApplication {

    public static void main(String[] args) {
        // Must run before Spring starts: the Postgres driver reads the JVM
        // default timezone when it opens a connection. "Asia/Calcutta" is a
        // legacy alias that the Postgres 16 image rejects.
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));
        SpringApplication.run(CineaiBackendApplication.class, args);
    }
}