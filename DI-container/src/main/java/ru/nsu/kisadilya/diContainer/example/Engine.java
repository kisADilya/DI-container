package ru.nsu.kisadilya.diContainer.example;

import jakarta.inject.Inject;

public class Engine {
    private Piston piston;

    @Inject
    public Engine(Piston piston) {
        this.piston = piston;
    }

    public void run() {
        try {
            for (int i = 0; i < 10; i++) {
                piston.up();
                Thread.sleep(500);
                piston.down();
                Thread.sleep(500);
            }
        } catch (InterruptedException ignored) {}
    }
}
