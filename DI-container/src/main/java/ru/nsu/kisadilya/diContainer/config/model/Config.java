package ru.nsu.kisadilya.diContainer.config.model;

import lombok.Getter;

import java.util.Map;

@Getter
public class Config {
    private Map<String, Bean> beans;
}
