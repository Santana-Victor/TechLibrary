package com.victorsantana.TechLibrary.utils;

import java.util.UUID;

public interface UUIDGenerator {

    UUID randomUUID();

    UUID fromString(String id);
}