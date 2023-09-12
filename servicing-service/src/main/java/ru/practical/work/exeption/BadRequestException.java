package ru.practical.work.exeption;

public class BadRequestException extends IllegalArgumentException {
    public BadRequestException(final String message) {
        super(message);
    }
}
