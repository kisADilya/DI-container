package ru.nsu.kisadilya.diContainer.example;


import ru.nsu.kisadilya.diContainer.Cocina;

public class Example {
    public static void main(String[] args) {
        Cocina cocina = new Cocina("DI-container/src/main/java/ru/nsu/kisadilya/diContainer/example/config.json");
        CarInterfaceTest carInterfaceTest = (CarInterfaceTest) cocina.getIngrediente(CarInterfaceTest.class);
        carInterfaceTest.run();

    }
}
