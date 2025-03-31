package ru.nsu.kisadilya.diContainer.config.model;

import lombok.Getter;

import java.util.Map;

@Getter
public class Config {
    private Map<String, Bean> beans;

    public Bean getByBeanType(String beanType) {
        return beans.values().stream().filter(bean -> bean.getClassname().equals(beanType)).findFirst().orElse(null);
    }
}
