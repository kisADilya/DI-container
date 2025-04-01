package ru.nsu.kisadilya.diContainer;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import ru.nsu.kisadilya.diContainer.config.model.Bean;
import ru.nsu.kisadilya.diContainer.config.model.BeanConstructorArg;
import ru.nsu.kisadilya.diContainer.config.model.ConstructorArg;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
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

    public static List<String> getConstructorDependencies(Bean bean, Map<String, Bean> allBeans) {
        List<String> dependencies = new ArrayList<>();
        try {
            if (bean.getConstructorArgs() != null && !bean.getConstructorArgs().isEmpty()) {
                for (ConstructorArg arg : bean.getConstructorArgs()) {
                    if ("ref".equals(arg.getType())) {
                        Object value = arg.getValue();
                        String refName = value instanceof BeanConstructorArg
                                ? ((BeanConstructorArg) value).getRef()
                                : value.toString();
                        dependencies.add(refName);
                    }
                }
                return dependencies;
            }

            Class<?> beanClass = Class.forName(bean.getClassname());
            Constructor<?> constructor = selectConstructor(beanClass);

            for (Parameter parameter : constructor.getParameters()) {
                if (parameter.isAnnotationPresent(Named.class)) {
                    dependencies.add(parameter.getAnnotation(Named.class).value());
                } else {
                    String beanName = findBeanNameByType(parameter.getType(), allBeans);
                    if (beanName != null) {
                        dependencies.add(beanName);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to analyze dependencies for " + bean.getClassname(), e);
        }
        return dependencies;
    }

    private static Constructor<?> selectConstructor(Class<?> clazz) {
        List<Constructor<?>> injectConstructors = Arrays.stream(clazz.getDeclaredConstructors())
                .filter(c -> c.isAnnotationPresent(Inject.class))
                .toList();

        if (injectConstructors.size() > 1) {
            throw new RuntimeException("Multiple @Inject constructors found in " + clazz.getName());
        }

        if (!injectConstructors.isEmpty()) {
            return injectConstructors.get(0);
        }

        Constructor<?>[] constructors = clazz.getConstructors();
        if (constructors.length != 1) {
            throw new RuntimeException("No @Inject constructor and multiple constructors found in "
                    + clazz.getName());
        }

        return constructors[0];
    }
    public static String findBeanNameByType(Class<?> beanType, Map<String, Bean> beans) {
        Named namedAnnotation = beanType.getAnnotation(Named.class);
        if (namedAnnotation != null) {
            return namedAnnotation.value();
        }

        String targetClassName = beanType.getName();
        for (Map.Entry<String, Bean> entry : beans.entrySet()) {
            if (targetClassName.equals(entry.getValue().getClassname())) {
                return entry.getKey();
            }
        }

        if (beanType.isInterface()) {
            for (Map.Entry<String, Bean> entry : beans.entrySet()) {
                try {
                    Class<?> beanClass = Class.forName(entry.getValue().getClassname());
                    if (beanType.isAssignableFrom(beanClass)) {
                        return entry.getKey();
                    }
                } catch (ClassNotFoundException e) {
                    continue;
                }
            }
        }

        return null;
    }
}
