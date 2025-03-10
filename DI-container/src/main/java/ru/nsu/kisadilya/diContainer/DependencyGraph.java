package ru.nsu.kisadilya.diContainer;

import lombok.Getter;

import java.util.*;

@Getter
public class DependencyGraph {

    private List<String> result = new ArrayList<>();
    private Set<String> visited = new HashSet<>();
    private Set<String> visiting = new HashSet<>();
    private final Map<String, List<String>> graph = new HashMap<>();

    public void addDependency(String bean, List<String> dependencies) {
        graph.put(bean, dependencies);
    }

    public List<String> getDependencies(String bean) {
        return graph.getOrDefault(bean, Collections.emptyList());
    }

    public List<String> topologicalSort() {
        for (String node : graph.keySet()) {
            if (!visited.contains(node)) {
                visit(node);
            }
        }
        return result;
    }

    private void visit(String node) {
        if (visiting.contains(node)) {
            throw new IllegalStateException("Cycle detected: " + node);
        }
        if (!visited.contains(node)) {
            visiting.add(node);
            for (String dependency : graph.getOrDefault(node, Collections.emptyList())) {
                visit(dependency);
            }
            visiting.remove(node);
            visited.add(node);
            result.add(node);
        }
    }
}