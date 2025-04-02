package ru.nsu.kisadilya.diContainer.example;


import jakarta.inject.Provider;
import ru.nsu.kisadilya.diContainer.Cocina;


public class Example {
    public static void main(String[] args) throws Exception{
        Cocina cocina = new Cocina("src/main/java/ru/nsu/kisadilya/diContainer/example/config.json");
//        CarInterfaceTest carInterfaceTest = (CarInterfaceTest) cocina.getIngrediente(CarInterfaceTest.class);
//        carInterfaceTest.run();

        ThreadExample ex = cocina.getIngrediente("ThreadScopeExample");
        Runnable task1 = () -> {
            System.out.println(ex.getValue());
        };

        Runnable task2 = () -> {
            System.out.println(ex.getValue());
        };

        Thread t1 = new Thread(task1, "Thread-1");
        Thread t2 = new Thread(task2, "Thread-2");

        t1.start();
        t2.start();
        t1.join();
        t2.join();
//        Car1 car1 = cocina.getIngrediente(Car1.class);
        Piston piston = cocina.getIngrediente("MyPiston");
        piston.up();

        Car car = cocina.getIngrediente(Car.class);
//        car1.run();
        car.run();
//        Provider<Gearbox> provider = cocina.getIngredienteProvider("Gearbox", Gearbox.class);
//        Provider<Gearbox> provider = cocina.getIngredienteProvider(Gearbox.class);
//        provider.get().shiftDown();
//        Engine1 engine1 = cocina.getIngrediente("Engine1");
//        Piston1 piston1 = cocina.getIngrediente("MyPiston1");
//        engine1.run();

//        piston1.down();
    }
}
