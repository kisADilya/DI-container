package ru.nsu.kisadilya.diContainer.example.NamedExample;

import jakarta.inject.Inject;
import jakarta.inject.Named;

public class Car {
    private final Engine engine;
    @Inject
    public Car(@Named("CoolEngine") Engine engine) {
        this.engine = engine;
        System.out.println("Car created with engine: " + engine.getClass().getSimpleName());
    }

    public void run() {
        engine.run();
    }
}
