package ru.nsu.kisadilya.diContainer.example;

import java.util.Random;
import java.util.random.RandomGenerator;

public class ThreadExample {
    private final int value = Random.from(RandomGenerator.getDefault()).nextInt();

    public String getValue() {
        return Integer.toString(value);
    }
}
