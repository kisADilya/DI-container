package ru.nsu.kisadilya.diContainer.example;

import jakarta.inject.Inject;

public class Car {
    private final Engine engine;

    @Inject
    public Car(Engine engine) {
        this.engine = engine;
        System.out.println("wroom");
    }

    public void run() {
        engine.run();
    }
}
