package ru.nsu.kisadilya.diContainer.example;

import jakarta.inject.Inject;

public class Car1 {
    private final Engine1 engine;

    @Inject
    public Car1(Engine1 engine) {
        this.engine = engine;
        System.out.println("wroom");
    }

    public void run() {
        engine.run();
    }
}
