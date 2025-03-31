package ru.nsu.kisadilya.diContainer;

import jakarta.inject.Inject;
import ru.nsu.kisadilya.diContainer.config.model.Bean;
import ru.nsu.kisadilya.diContainer.config.model.BeanConstructorArg;
import ru.nsu.kisadilya.diContainer.config.model.ConstructorArg;
import ru.nsu.kisadilya.diContainer.config.model.Named;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
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

    public static List<String> getConstructorDependencies(Bean bean, Map<String, Bean> allBeans) {
        List<String> dependencies = new ArrayList<>();
        try {
            if (bean.getConstructorArgs() != null) {
                for (ConstructorArg arg : bean.getConstructorArgs()) {
                    if ("ref".equals(arg.getType())) {
                        Object value = arg.getValue();

                        if (value instanceof BeanConstructorArg) {
                            String refName = ((BeanConstructorArg) value).getRef();
                            dependencies.add(refName);
                        }
                        else {
                            dependencies.add(value.toString());
                        }
                    }
                }
                return dependencies;
            }
            Class<?> beanClass = Class.forName(bean.getClassname());
            Constructor<?> constructor = beanClass.getConstructors()[0];

            for (Parameter parameter : constructor.getParameters()) {
                Named namedAnnotation = parameter.getAnnotation(Named.class);
                if (namedAnnotation != null) {
                    dependencies.add(namedAnnotation.value());
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

        String simpleName = beanType.getSimpleName();
        for (Map.Entry<String, Bean> entry : beans.entrySet()) {
            try {
                Class<?> beanClass = Class.forName(entry.getValue().getClassname());
                if (simpleName.equals(beanClass.getSimpleName())) {
                    return entry.getKey();
                }
            } catch (ClassNotFoundException e) {
                continue;
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
