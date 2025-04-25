package com.victorsantana.TechLibrary.utils;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.UUIDUtil;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UUIDGeneratorImpl implements UUIDGenerator {

    @Override
    public UUID randomUUID() {
        return Generators.timeBasedEpochGenerator().generate();
    }

    @Override
    public UUID fromString(String id) {
        return UUIDUtil.uuid(id);
    }
}