package ru.nsu.kisadilya.diContainer.example.NamedExample;


public class Engine {
    private final Piston piston;

    public Engine(Piston piston) {
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

