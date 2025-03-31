package ru.nsu.kisadilya.diContainer.example;

import jakarta.inject.Inject;

public class CarInterfaceTest implements CarInterface {
    private final EngineInterface engine;

    @Inject
    public CarInterfaceTest(EngineInterface engine) {
        this.engine = engine;
        System.out.println("Car created with engine: " + engine.getClass().getSimpleName());
    }

    public void run() {
        engine.run();
    }
}
