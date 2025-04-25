package com.victorsantana.TechLibrary.utils;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UUIDGeneratorImpl implements UUIDGenerator {

    @Override
    public UUID randomUUID() {
        return UUID.randomUUID();
    }

    @Override
    public UUID fromString(String id) {
        return UUID.fromString(id);
    }
}