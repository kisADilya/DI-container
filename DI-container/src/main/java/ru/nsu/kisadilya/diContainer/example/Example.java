package ru.nsu.kisadilya.diContainer.example;


import jakarta.inject.Provider;
import ru.nsu.kisadilya.diContainer.Cocina;


public class Example {
    public static void main(String[] args) {
        Cocina cocina = new Cocina("src/main/java/ru/nsu/kisadilya/diContainer/example/config.json");

//        Car1 car1 = (Car1) cocina.getIngrediente(Car1.class);
        Car car = (Car) cocina.getIngrediente(Car.class);
//        car1.run();
        car.run();
//        Engine1 engine1 = cocina.getIngrediente("Engine1");
//        Piston1 piston1 = cocina.getIngrediente("MyPiston1");
//        engine1.run();

//        piston1.down();
    }
}
