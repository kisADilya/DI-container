package ru.nsu.kisadilya.diContainer;

import lombok.Getter;
import ru.nsu.kisadilya.diContainer.config.ConfigReader;
import ru.nsu.kisadilya.diContainer.config.model.Bean;
import ru.nsu.kisadilya.diContainer.config.model.Config;

import java.util.List;
import java.util.Map;
@Getter
public class Cocina {
    private final Config config;
    private final DependencyGraph dependencyGraph = new DependencyGraph();
    private final List<String> sortedBeans;
    public Cocina(String configFileName) {
        config = ConfigReader.readConfig(configFileName);
        buildDependencyGraph();
        sortedBeans = dependencyGraph.topologicalSort();
        initializeBeans(sortedBeans);
    }
    private void buildDependencyGraph() {
        for (var entry : config.getBeans().entrySet()) {
            String beanName = entry.getKey();
            Bean bean = entry.getValue();
            List<String> dependencies = BeanAnalyzer.getConstructorDependencies(bean);
            dependencyGraph.addDependency(beanName, dependencies);
        }
    }

    private void initializeBeans(List<String> sortedBeans) {
        for (String beanName : sortedBeans) {
            System.out.println("Initializing bean: " + beanName);
        }
    }

    public Object getIngrediente() {
        return new Object();
    }

}
