package com.victorsantana.TechLibrary.exceptions;

public class EmailAddressAlreadyRegisteredException extends RuntimeException {
    public EmailAddressAlreadyRegisteredException() {
        super("Email already registered.");
    }
}