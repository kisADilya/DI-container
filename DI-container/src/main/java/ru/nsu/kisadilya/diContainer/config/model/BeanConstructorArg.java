package ru.nsu.kisadilya.diContainer.config.model;

import lombok.Getter;

@Getter
public class BeanConstructorArg {
    private final String ref;
    public BeanConstructorArg(String ref) {
        this.ref = ref;
    }
}
