package ru.nsu.kisadilya.diContainer;

import lombok.Getter;

import java.util.*;
@Getter
public class DependencyGraph {

    private final Map<String, List<String>> graph = new HashMap<>();

    public void addDependency(String bean, List<String> dependencies) {
        graph.put(bean, dependencies);
    }

    public List<String> getDependencies(String bean) {
        return graph.getOrDefault(bean, Collections.emptyList());
    }

    public Map<String, List<String>> getGraph() {
        return graph;
    }
}
