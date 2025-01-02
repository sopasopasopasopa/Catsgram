package ru.yandex.practicum.catsgram.exception;

public class ConditionsNotMetException extends RuntimeException {
    public ConditionsNotMetException(String massage) {
        super(massage);
    }
}
