package ru.nsu.kisadilya.diContainer.example.SimpleExample;

import lombok.Getter;

@Getter
public class Car {
    private final Engine engine;

    public Car(Engine engine) {
        this.engine = engine;
        System.out.println("wroom");
    }

    public void run() {
        engine.run();
    }
}
