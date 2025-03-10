package ru.nsu.kisadilya.diContainer;

import java.util.*;

public class TopologicalSort {

    public static List<String> sort(Map<String, List<String>> graph) {
        List<String> result = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        Set<String> visiting = new HashSet<>();

        for (String node : graph.keySet()) {
            if (!visited.contains(node)) {
                visit(node, graph, visited, visiting, result);
            }
        }

        return result;
    }

    private static void visit(String node, Map<String, List<String>> graph, Set<String> visited, Set<String> visiting, List<String> result) {
        if (visiting.contains(node)) {
            throw new IllegalStateException("Cycle detected: " + node);
        }
        if (!visited.contains(node)) {
            visiting.add(node);
            for (String dependency : graph.getOrDefault(node, Collections.emptyList())) {
                visit(dependency, graph, visited, visiting, result);
            }
            visiting.remove(node);
            visited.add(node);
            result.add(node);
        }
    }
}
