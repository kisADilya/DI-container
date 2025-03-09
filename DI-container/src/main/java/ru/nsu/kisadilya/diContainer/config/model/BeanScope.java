package ru.nsu.kisadilya.diContainer.config.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum BeanScope {
    SINGLETON,
    PROTOTYPE,
    THREAD;

    @JsonCreator
    public static BeanScope fromString(String value) {
        return BeanScope.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toString() {
        return name().toLowerCase();
    }
}

