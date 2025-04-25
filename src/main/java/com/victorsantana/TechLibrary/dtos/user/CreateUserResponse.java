package com.victorsantana.TechLibrary.dtos.user;

import com.victorsantana.TechLibrary.entities.User;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

public class CreateUserResponse {

    private final static String TIMEZONE = "America/Sao_Paulo";
    private final UUID id;
    private final String name;
    private final String email;
    private final Instant createdAt;

    public CreateUserResponse(User entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.email = entity.getEmail();
        this.createdAt = entity.getCreatedAt();
    }

    public UUID getId() { return id; }

    public String getName() { return name; }

    public String getEmail() { return email; }

    public String getCreatedAt() { return this.formattedTimestamp(createdAt); }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CreateUserResponse that = (CreateUserResponse) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() { return Objects.hashCode(id); }

    @Override
    public String toString() {
        return "CreateUserResponse{id=%s, name='%s', email='%s', createdAt=%s}".formatted(id, name, email, createdAt);
    }

    private String formattedTimestamp(Instant instant) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        ZoneId zoneId = ZoneId.of(TIMEZONE);
        return instant.atZone(zoneId).withNano(0).format(formatter);
    }
}