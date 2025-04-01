package ru.nsu.kisadilya.diContainer.example.NamedExample;

import lombok.Getter;

@Getter
public class CoolEngine {
    private final Piston piston;

    public CoolEngine(Piston piston) {
        this.piston = piston;
        System.out.println("i am cool engine");
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

