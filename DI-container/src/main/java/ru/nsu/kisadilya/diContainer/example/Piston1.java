package ru.nsu.kisadilya.diContainer.example;

public class Piston1 {
    private String up;
    private String down;

    public Piston1(String up, String down) {
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
