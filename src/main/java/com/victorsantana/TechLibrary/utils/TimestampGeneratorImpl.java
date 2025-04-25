package com.victorsantana.TechLibrary.utils;

import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class TimestampGeneratorImpl implements TimestampGenerator {

    @Override
    public Instant now() {
        return Instant.now();
    }

    @Override
    public Instant fromString(String text) {
        return Instant.parse(text);
    }
}