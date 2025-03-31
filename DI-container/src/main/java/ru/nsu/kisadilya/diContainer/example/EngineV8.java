package ru.nsu.kisadilya.diContainer.example;

import jakarta.inject.Inject;

public class EngineV8 implements EngineInterface{
    Piston1 piston;

    @Inject
    public EngineV8(Piston1 piston) {
        this.piston = piston;
        System.out.println("so am i");
    }

    @Override
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
