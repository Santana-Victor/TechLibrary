package com.victorsantana.TechLibrary.utils;

import java.time.Instant;

public interface TimestampGenerator {

    Instant now();

    Instant fromString(String text);
}