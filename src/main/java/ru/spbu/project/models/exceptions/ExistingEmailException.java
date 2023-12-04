package ru.spbu.project.models.exceptions;

public class ExistingEmailException extends Exception {
    public ExistingEmailException(String message) {
        super(message);
    }
}
