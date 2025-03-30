package ru.nsu.kisadilya.diContainer.example;

import jakarta.inject.Inject;
import jakarta.inject.Provider;

public class Car {
    private final Engine engine;
    private final Provider<Engine> providerEngine;
    @Inject
    public Car(Engine engine, Provider<Engine> engineProvider) {
        this.engine = engine;
        this.providerEngine = engineProvider;
        System.out.println("wroom");
    }

    public void run() {
        providerEngine.get();
        engine.run();
    }
}
