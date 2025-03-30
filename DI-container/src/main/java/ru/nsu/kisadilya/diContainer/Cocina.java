package ru.nsu.kisadilya.diContainer;

import jakarta.inject.Inject;
import jakarta.inject.Provider;
import lombok.Getter;
import org.apache.commons.lang3.ArrayUtils;
import ru.nsu.kisadilya.diContainer.config.ConfigReader;
import ru.nsu.kisadilya.diContainer.config.model.Bean;
import ru.nsu.kisadilya.diContainer.config.model.BeanConstructorArg;
import ru.nsu.kisadilya.diContainer.config.model.Config;
import ru.nsu.kisadilya.diContainer.config.model.ConstructorArg;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
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
        String targetBeanName;
        if (beanType.isAssignableFrom(Provider.class)) {
            Class<?> actualBeanType = beanType.getGenericSuperclass().getClass();
            targetBeanName = findBeanNameByType(actualBeanType);
        } else {
            targetBeanName = findBeanNameByType(beanType);
        }
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
            List<String> dependencies = BeanAnalyzer.getConstructorDependencies(bean);
            dependencyGraph.addDependency(beanName, dependencies);
        }
    }

    private String findBeanNameByType(Class<?> beanType) {
        for (Map.Entry<String, Bean> entry : config.getBeans().entrySet()) {
            try {
                Class<?> beanClass = Class.forName(entry.getValue().getClassname());
                if (beanType.isAssignableFrom(beanClass)) {
                    return entry.getKey();
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Class not found for bean: " + entry.getKey(), e);
            }
        }
        return null;
    }

    private Bean findBeanByType(Class<?> beanType) throws ClassNotFoundException {
        Bean answer = null;

        for (Bean entry : config.getBeans().values()) {
            if (isBeanFitsInType(beanType, entry)) {
                if (answer == null) {
                    answer = entry;
                } else {
                    throw new RuntimeException("Found multiple beans for type " + beanType.getName());
                }
            }
        }

        return answer;
    }

    private boolean isBeanFitsInType(Class<?> beanType, Bean bean) throws ClassNotFoundException {
        if (beanType.isInterface()) {
            Class<?> beanClass = Class.forName(bean.getClassname());
            return beanClass.isAssignableFrom(beanType);
        } else {
            return beanType.getName().equals(bean.getClassname());
        }
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

        List<String> beanDependencies = BeanAnalyzer.getConstructorDependencies(bean);
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

    private Object createBeanInstance(Bean beanDefinition) throws Exception {
        Class<?> beanClass = Class.forName(beanDefinition.getClassname());

        List<Object> constructorArgs = getConstructorArgsObjects(beanDefinition);

        Class<?>[] argTypes = constructorArgs.stream()
                .map(Object::getClass)
                .toArray(Class<?>[]::new);
        try {
            Constructor<?> constructor = beanClass.getConstructor(argTypes);
            Object beanInstance = constructor.newInstance(constructorArgs.toArray());
            //TODO инициализировать поля
            return beanInstance;
        } catch (NoSuchMethodException e) {
            //adding providers

            var constructorCandidates = Arrays.stream(beanClass.getConstructors())
                    .filter(constructor -> constructor.isAnnotationPresent(Inject.class))
                    .filter(constructor -> constructor.getParameterCount() == argTypes.length)
                    .toList();

            if (constructorCandidates.isEmpty()) {
                System.out.println("No constructor found");
                throw e;
            }
//            System.out.println("Amount of constructors" + constructorCandidates.size());
            for (Constructor<?> constructor : constructorCandidates) {
//                if (Arrays.stream(constructor.getParameterTypes())
//                        .anyMatch(paramType -> !ArrayUtils.contains(argTypes, paramType)
//                        && !(paramType.isInstance(Provider.class)
//                                        && ArrayUtils.contains(argTypes, paramType.getGenericSuperclass().getClass()))
//                        )) {
//////
//                    System.out.println("Generic is " + Arrays.toString(constructor.getParameterTypes()));
//                    continue;
//                }

                Class<?>[] constructorArgTypes = constructor.getParameterTypes();
                for (int i = 0; i < constructorArgTypes.length; i++) {
                    if (constructorArgTypes[i].equals(Provider.class)) {
                        constructorArgs.set(i, getProviderOf(argTypes[i]));
                    }
                }

                Object beanInstance = constructor.newInstance(constructorArgs.toArray());
                //TODO инициализировать поля

                return beanInstance;
            }

            System.out.println("Did not work");
            throw e;
        }
    }

//    private List<Object> getProviders(List<Class<?>> providerTypes) throws Exception {
//        List<Object> providers = new ArrayList<>();
//        for (Class<?> providerType : providerTypes) {
//            String nameOfClassToProvide = providerType.getGenericSuperclass().getTypeName();
//            Class<?> classToProvide = Class.forName(nameOfClassToProvide);
//
////            Bean bean = config.getByBeanType(nameOfClassToProvide);
//
//            List<Object> constructorArgs = getConstructorArgsObjects(bean);
//
//            Class<?>[] argTypes = constructorArgs.stream()
//                    .map(Object::getClass)
//                    .toArray(Class<?>[]::new);
//
//            var provider = ProviderFactory.createProvider(classToProvide, argTypes);
//            providers.add(provider);
//        }

//        return providers;
//    }

    private Object getProviderOf(Class<?> clazz) throws Exception {

//        String nameOfClassToProvide = providerType.getGenericSuperclass().getTypeName();
//        Class<?> classToProvide = Class.forName(nameOfClassToProvide);

        Bean bean = config.getByBeanType(clazz.getName());

        List<Object> constructorArgs = getConstructorArgsObjects(bean);

        Class<?>[] argTypes = constructorArgs.stream()
                .map(Object::getClass)
                .toArray(Class<?>[]::new);

        return ProviderFactory.createProvider(clazz, constructorArgs.toArray());
    }

    private List<Object> getConstructorArgsObjects(Bean beanDefinition) {
        List<Object> constructorArgs = new ArrayList<>();
        if (beanDefinition.getConstructorArgs() != null) {
            for (ConstructorArg arg : beanDefinition.getConstructorArgs()) {
                Object argValue = resolveArgumentValue(arg);
                constructorArgs.add(argValue);
            }
        }
        return constructorArgs;
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