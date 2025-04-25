package com.victorsantana.TechLibrary.exceptions;

public record RestErrorMessage(int status, String error, String message) {}