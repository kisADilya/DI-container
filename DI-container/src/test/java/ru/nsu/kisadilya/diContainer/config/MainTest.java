package ru.nsu.kisadilya.diContainer.config;


import org.junit.jupiter.api.Test;
import ru.nsu.kisadilya.diContainer.Cocina;
import ru.nsu.kisadilya.diContainer.example.Gearbox;
import ru.nsu.kisadilya.diContainer.example.InterfaceExample.InterfaceCar;
import ru.nsu.kisadilya.diContainer.example.InterfaceExample.InterfaceEngine;
import ru.nsu.kisadilya.diContainer.example.NamedExample.CoolEngine;
import ru.nsu.kisadilya.diContainer.example.NamedExample.NamedCar;
import ru.nsu.kisadilya.diContainer.example.SimpleExample.Car;
import ru.nsu.kisadilya.diContainer.example.SingletonWithThread;
import ru.nsu.kisadilya.diContainer.example.ThreadExample;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;

public class MainTest {

    @Test
    public void simpleTest() {
        Cocina cocina = new Cocina("src/main/java/ru/nsu/kisadilya/diContainer/example/SimpleExample/config.json");
        Car car = cocina.getIngrediente(Car.class);
        assertNotNull(car);
        assertNotNull(car.getEngine());
        assertNotNull(car.getEngine().getPiston());
        car.run();
    }

    @Test
    public void namedTest() {
        Cocina cocina = new Cocina("src/main/java/ru/nsu/kisadilya/diContainer/example/NamedExample/config.json");
        NamedCar namedCar = cocina.getIngrediente(NamedCar.class);
        assertNotNull(namedCar);
        assertNotNull(namedCar.getEngine());
        assertNotNull(namedCar.getEngine().getPiston());

        assertEquals(CoolEngine.class, namedCar.getEngine().getClass());
//        assertEquals(Engine.class, namedCar.getEngine().getClass());
        namedCar.run();
    }

    @Test
    public void interfaceTest() {
        Cocina cocina = new Cocina("src/main/java/ru/nsu/kisadilya/diContainer/example/InterfaceExample/config.json");
        InterfaceCar interfaceCar = cocina.getIngrediente(InterfaceCar.class);

        assertNotNull(interfaceCar);
        assertNotNull(interfaceCar.getEngine());
        assertNotNull(interfaceCar.getEngine().getPiston());

//        assertEquals(Engine1.class, interfaceCar.getEngine().getClass());
        assertEquals(InterfaceEngine.class, interfaceCar.getEngine().getClass());
        interfaceCar.run();
    }

    @Test
    public void prototypeTest() {
        Cocina cocina = new Cocina("src/main/java/ru/nsu/kisadilya/diContainer/example/config.json");

        var gearboxProvider = cocina.getIngredienteProvider(Gearbox.class);

        Gearbox gearbox = gearboxProvider.get();
        Gearbox gearbox2 = gearboxProvider.get();

        assertNotSame(gearbox, gearbox2);

        gearbox2.setManual(false);
        gearbox.setManual(true);

        assertNotSame(gearbox.isManual(), gearbox2.isManual());
    }

    @Test
    public void threadScopeTest() throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        Cocina cocina = new Cocina("src/main/java/ru/nsu/kisadilya/diContainer/example/config.json");

        ThreadExample ex = cocina.getIngrediente("ThreadScopeExample");

        Callable<String> task1 = ex::getValue;
        Callable<String> task2 = ex::getValue;

        Future<String> result1 = executor.submit(task1);
        Future<String> result2 = executor.submit(task2);
        assertNotEquals(result1.get(), result2.get());


    }

    @Test
    public void mixedTest() throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        Cocina cocina = new Cocina("src/main/java/ru/nsu/kisadilya/diContainer/example/config.json");
        SingletonWithThread ex = cocina.getIngrediente(SingletonWithThread.class);

        Callable<String> task1 = () -> ex.getThreadExample().getValue();
        Callable<String> task2 = () -> ex.getThreadExample().getValue();


        Future<String> result1 = executor.submit(task1);
        Future<String> result2 = executor.submit(task2);

        assertNotEquals(result1.get(), result2.get());
    }

}