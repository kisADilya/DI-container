package ru.nsu.kisadilya.diContainer;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import ru.nsu.kisadilya.diContainer.config.ConfigReader;
import ru.nsu.kisadilya.diContainer.config.model.Bean;
import ru.nsu.kisadilya.diContainer.config.model.BeanConstructorArg;
import ru.nsu.kisadilya.diContainer.config.model.Config;
import ru.nsu.kisadilya.diContainer.config.model.ConstructorArg;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class Cocina {
    private final Config config;
    private final DependencyGraph dependencyGraph = new DependencyGraph();
    private List<String> sortedBeans;
    private final Map<String, Object> beanInstances = new HashMap<>();

    public Cocina(String configFileName) {
        config = ConfigReader.readConfig(configFileName);
        buildDependencyGraph();
    }

    /**
     * Получает бин по его типу
     */
    public <T> T getIngrediente(Class<T> beanType) {
        String targetBeanName = findBeanNameByType(beanType);
        if (targetBeanName == null) {
            throw new IllegalArgumentException("Bean of type " + beanType.getName() + " not found in config");
        }
        return getIngrediente(targetBeanName, beanType);
    }

    /**
     * Получает бин по его имени (без проверки типа)
     */
    @SuppressWarnings("unchecked")
    public <T> T getIngrediente(String beanName) {
        Bean beanDefinition = config.getBeans().get(beanName);
        if (beanDefinition == null) {
            throw new IllegalArgumentException("Bean with name " + beanName + " not found in config");
        }

        try {
            Class<?> beanClass = Class.forName(beanDefinition.getClassname());
            return (T) getIngrediente(beanName, beanClass);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Class not found for bean: " + beanName, e);
        }
    }

    /**
     * Получает бин по его имени с проверкой типа
     */
    public <T> T getIngrediente(String beanName, Class<T> beanType) {
        if (!config.getBeans().containsKey(beanName)) {
            throw new IllegalArgumentException("Bean with name " + beanName + " not found in config");
        }

        List<String> requiredBeans = findAllDependencies(beanName);

        DependencyGraph tempGraph = new DependencyGraph();
        for (String bean : requiredBeans) {
            tempGraph.addDependency(bean, dependencyGraph.getDependencies(bean));
        }

        sortedBeans = tempGraph.topologicalSort();
        initializeRequiredBeans(sortedBeans);

        Object bean = beanInstances.get(beanName);
        if (bean == null) {
            throw new IllegalStateException("Failed to create bean with name " + beanName);
        }

        if (!beanType.isInstance(bean)) {
            throw new ClassCastException("Bean " + beanName + " is not of type " + beanType.getName());
        }

        return beanType.cast(bean);
    }

    private void buildDependencyGraph() {
        for (var entry : config.getBeans().entrySet()) {
            String beanName = entry.getKey();
            Bean bean = entry.getValue();
            List<String> dependencies = BeanAnalyzer.getConstructorDependencies(bean, config.getBeans());
            dependencyGraph.addDependency(beanName, dependencies);
        }
    }

    private String findBeanNameByType(Class<?> beanType) {
        return BeanAnalyzer.findBeanNameByType(beanType, config.getBeans());
    }

    private List<String> findAllDependencies(String beanName) {
        List<String> dependencies = new ArrayList<>();
        collectDependencies(beanName, dependencies);
        return dependencies;
    }

    private void collectDependencies(String beanName, List<String> dependencies) {
        if (dependencies.contains(beanName)) {
            return;
        }

        dependencies.add(beanName);

        Bean bean = config.getBeans().get(beanName);
        if (bean == null) {
            throw new IllegalArgumentException("Bean not found: " + beanName);
        }

        List<String> beanDependencies = BeanAnalyzer.getConstructorDependencies(bean, config.getBeans());
        for (String dependency : beanDependencies) {
            collectDependencies(dependency, dependencies);
        }
    }

    private void initializeRequiredBeans(List<String> beansToInitialize) {
        for (String beanName : beansToInitialize) {
            if (!beanInstances.containsKey(beanName)) {
                try {
                    Bean beanDefinition = config.getBeans().get(beanName);
                    Object beanInstance = createBeanInstance(beanDefinition);
                    beanInstances.put(beanName, beanInstance);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to initialize bean: " + beanName, e);
                }
            }
        }
    }

    private Constructor<?> findSuitableConstructor(Class<?> beanClass) {
        Constructor<?>[] constructors = beanClass.getConstructors();
        if (constructors.length == 1) return constructors[0];

        for (Constructor<?> c : constructors) {
            if (c.isAnnotationPresent(Inject.class)) {
                return c;
            }
        }

        throw new RuntimeException("No suitable constructor found in " + beanClass.getName());
    }

    private Object createBeanInstance(Bean beanDefinition) throws Exception {
        Class<?> beanClass = Class.forName(beanDefinition.getClassname());

        Constructor<?> constructor = findSuitableConstructor(beanClass);


        Parameter[] parameters = constructor.getParameters();
        Object[] args = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];

            if (beanDefinition.getConstructorArgs() != null && i < beanDefinition.getConstructorArgs().size()) {
                args[i] = resolveArgumentValue(beanDefinition.getConstructorArgs().get(i));
                continue;
            }

            if (parameter.isAnnotationPresent(Named.class)) {
                args[i] = getIngrediente(parameter.getAnnotation(Named.class).value());
                continue;
            }

            args[i] = getIngrediente(parameter.getType());
        }

        return constructor.newInstance(args);
    }

    private Object resolveArgumentValue(ConstructorArg arg) {
        if (arg == null) return null;

        if ("ref".equals(arg.getType())) {
            Object value = arg.getValue();
            String refBeanName;

            if (value instanceof BeanConstructorArg) {
                refBeanName = ((BeanConstructorArg) value).getRef();
            } else {
                refBeanName = value.toString();
            }

            Object bean = beanInstances.get(refBeanName);
            if (bean == null) {
                throw new IllegalStateException("Dependency not found: " + refBeanName);
            }
            return bean;
        }
        return arg.getValue();
    }
}