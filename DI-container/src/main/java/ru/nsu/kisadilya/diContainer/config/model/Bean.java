package ru.nsu.kisadilya.diContainer.config.model;

import lombok.Getter;

import java.util.Map;

@Getter
public class Bean {
    private String name;
    private BeanScope scope;
    private Map<String, String> constructorArgs;
    private Map<String, String> properties;
}
