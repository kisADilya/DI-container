package ru.nsu.kisadilya.diContainer.example.InterfaceExample;

import jakarta.inject.Inject;

public class Car implements CarInterface {
    private final EngineInterface engine;

    @Inject
    public Car(EngineInterface engine) {
        this.engine = engine;
        System.out.println("Car created with engine: " + engine.getClass().getSimpleName());
    }

    public void run() {
        engine.run();
    }
}
