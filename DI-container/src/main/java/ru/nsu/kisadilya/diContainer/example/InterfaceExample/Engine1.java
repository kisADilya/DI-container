package ru.nsu.kisadilya.diContainer.example.InterfaceExample;

import lombok.Getter;

@Getter
public class Engine1 implements EngineInterface {
    private final Piston piston;

    public Engine1(Piston piston) {
        this.piston = piston;
        System.out.println("im just engine");
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

