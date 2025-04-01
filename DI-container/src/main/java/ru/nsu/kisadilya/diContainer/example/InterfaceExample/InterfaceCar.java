package ru.nsu.kisadilya.diContainer.example.InterfaceExample;

import jakarta.inject.Inject;
import lombok.Getter;

@Getter
public class InterfaceCar implements CarInterface {
    private final EngineInterface engine;

    @Inject
    public InterfaceCar(EngineInterface engine) {
        this.engine = engine;
        System.out.println("Car created with engine: " + engine.getClass().getSimpleName());
    }

    public void run() {
        engine.run();
    }
}
