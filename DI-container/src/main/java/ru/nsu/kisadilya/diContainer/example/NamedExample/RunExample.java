package ru.nsu.kisadilya.diContainer.example.NamedExample;

import ru.nsu.kisadilya.diContainer.Cocina;

public class RunExample {
    public static void main(String[] args) {
        Cocina cocina = new Cocina("DI-container/src/main/java/ru/nsu/kisadilya/diContainer/example/NamedExample/config.json");
        NamedCar car = (NamedCar) cocina.getIngrediente(NamedCar.class);
        car.run();
    }
}
