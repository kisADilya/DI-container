package ru.nsu.kisadilya.diContainer.example.InterfaceExample;

import ru.nsu.kisadilya.diContainer.Cocina;

public class RunExample {
    public static void main(String[] args) {
        Cocina cocina = new Cocina("DI-container/src/main/java/ru/nsu/kisadilya/diContainer/example/InterfaceExample/config.json");
        Car car = (Car) cocina.getIngrediente(Car.class);
        car.run();
    }
}
