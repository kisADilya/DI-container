package ru.nsu.kisadilya.diContainer.example;


import ru.nsu.kisadilya.diContainer.Cocina;

public class Example {
    public static void main(String[] args) {
        Cocina cocina = new Cocina("config.json");

        Car car = (Car) cocina.getIngrediente();
        car.run();
    }
}
