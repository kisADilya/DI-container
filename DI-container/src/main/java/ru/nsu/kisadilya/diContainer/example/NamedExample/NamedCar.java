package ru.nsu.kisadilya.diContainer.example.NamedExample;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;

@Getter
public class NamedCar {
    private final CoolEngine engine;
    @Inject
    public NamedCar(@Named("CoolEngine") CoolEngine engine) {
        this.engine = engine;
        System.out.println("Car created with engine: " + engine.getClass().getSimpleName());
    }

    public void run() {
        engine.run();
    }
}
