package ru.nsu.kisadilya.diContainer;

import jakarta.inject.Inject;
import ru.nsu.kisadilya.diContainer.config.model.Bean;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BeanAnalyzer {

    public static boolean hasInjectAnnotation(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            Constructor<?>[] constructors = clazz.getConstructors();
            for (Constructor<?> constructor : constructors) {
                if (constructor.isAnnotationPresent(Inject.class)) {
                    return true;
                }
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Class not found: " + className);
        }
        return false;
    }

    public static List<String> getConstructorDependencies(Bean bean) {
        List<String> dependencies = new ArrayList<>();
        if (bean.getConstructorArgs() != null) {
            for (var arg : bean.getConstructorArgs()) {
                if ("ref".equals(arg.getType()) || arg.getType().startsWith("ref")) {
                    dependencies.add((String) arg.getValue());
                }
            }
        }
        return dependencies;
    }
}
