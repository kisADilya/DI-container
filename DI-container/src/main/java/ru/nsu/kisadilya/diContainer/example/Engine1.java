package ru.nsu.kisadilya.diContainer.example;

import jakarta.inject.Inject;

public class Engine1 {
    private Piston1 piston;

    @Inject
    public Engine1(Piston1 piston) {
        this.piston = piston;
        System.out.println("so am i");
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
