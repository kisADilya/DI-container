package ru.nsu.kisadilya.diContainer.config;


import org.junit.jupiter.api.Test;
import ru.nsu.kisadilya.diContainer.Cocina;
import ru.nsu.kisadilya.diContainer.example.InterfaceExample.Engine1;
import ru.nsu.kisadilya.diContainer.example.InterfaceExample.InterfaceCar;
import ru.nsu.kisadilya.diContainer.example.InterfaceExample.InterfaceEngine;
import ru.nsu.kisadilya.diContainer.example.NamedExample.CoolEngine;
import ru.nsu.kisadilya.diContainer.example.NamedExample.Engine;
import ru.nsu.kisadilya.diContainer.example.NamedExample.NamedCar;
import ru.nsu.kisadilya.diContainer.example.SimpleExample.Car;

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
}