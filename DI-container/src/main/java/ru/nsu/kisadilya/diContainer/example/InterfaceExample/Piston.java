package ru.nsu.kisadilya.diContainer.example.InterfaceExample;

public class Piston {
    private final String up;
    private final String down;

    public Piston(String up, String down) {
        this.up = up;
        this.down = down;
        System.out.println("i was crated");
    }
    public void up() {
        System.out.println("Piston " + this.up);
    }
    public void down() {
        System.out.println("Piston " + this.down);
    }
}

