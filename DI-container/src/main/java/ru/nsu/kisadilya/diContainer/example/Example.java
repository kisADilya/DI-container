package ru.nsu.kisadilya.diContainer.example;


import jakarta.inject.Provider;
import ru.nsu.kisadilya.diContainer.Cocina;


public class Example {
    public static void main(String[] args) {
        Cocina cocina = new Cocina("src/main/java/ru/nsu/kisadilya/diContainer/example/config.json");

//        Car1 car1 = cocina.getIngrediente(Car1.class);
        Piston piston = cocina.getIngrediente("MyPiston");
        piston.up();

        Car car = cocina.getIngrediente(Car.class);
//        car1.run();
//        car.run();
        Provider<Gearbox> provider = cocina.getIngredienteProvider("Gearbox", Gearbox.class);
//        Provider<Gearbox> provider = cocina.getIngredienteProvider(Gearbox.class);
        provider.get().shiftDown();
//        Engine1 engine1 = cocina.getIngrediente("Engine1");
//        Piston1 piston1 = cocina.getIngrediente("MyPiston1");
//        engine1.run();

//        piston1.down();
    }
}
