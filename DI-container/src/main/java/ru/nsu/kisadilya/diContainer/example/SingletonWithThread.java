package ru.nsu.kisadilya.diContainer.example;

import jakarta.inject.Inject;
import lombok.Getter;

@Getter
public class SingletonWithThread {

    @Inject
    private ThreadExample threadExample;

}
