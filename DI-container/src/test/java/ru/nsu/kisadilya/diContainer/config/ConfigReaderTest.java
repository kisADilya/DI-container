package ru.nsu.kisadilya.diContainer.config;

import org.junit.jupiter.api.Test;
import ru.nsu.kisadilya.diContainer.BeanAnalyzer;
import ru.nsu.kisadilya.diContainer.Cocina;
import ru.nsu.kisadilya.diContainer.config.model.Bean;
import ru.nsu.kisadilya.diContainer.config.model.BeanScope;
import ru.nsu.kisadilya.diContainer.config.model.Config;

import java.io.File;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ConfigReaderTest {

    @Test
    void readWrongConfig() {
        assertThrows(IllegalArgumentException.class, () -> {
            ConfigReader.readConfig("src/test/resources/testWrongConfig.json");
        });
    }

    @Test
    void readConfigSuccess() {
        Config config = ConfigReader.readConfig("src/test/resources/testConfig.json");
        assertNotNull(config);

        Bean bean = config.getBeans().get("com.example.MyService");
        Map.Entry<String, String> firstEntry = bean.getConstructorArgs().entrySet().iterator().next();

        String firstKey = firstEntry.getKey();
        String firstValue = firstEntry.getValue();

        assertEquals("myService", bean.getName());
        assertEquals(BeanScope.SINGLETON, bean.getScope());
        assertEquals("myRepository", firstValue);
        assertEquals("1000", bean.getProperties().get("timeout"));
    }

    @Test
    void testBuildGraph(){
        Cocina cocina = new Cocina("src/test/resources/testGraph.json");
        Map<String, List<String>> graph = cocina.getDependencyGraph().getGraph();
        assertTrue(graph.containsKey("myService"));
        assertTrue(graph.containsKey("myRepository"));
        assertEquals(List.of("myRepository"), graph.get("myService"));
        assertTrue(graph.get("myRepository").isEmpty());
    }
    @Test
    void testTopologicalSort() {
        Cocina cocina = new Cocina("src/test/resources/testTopSort.json");

        List<String> sortedBeans = cocina.getSortedBeans();

        List<String> expectedOrder = List.of(
                "beanD",
                "beanF",
                "beanE",
                "beanB",
                "beanC",
                "beanA"
        );

        assertEquals(expectedOrder, sortedBeans);
    }
    @Test
    void testHasInjectAnnotation_Car() {
        String className = "ru.nsu.kisadilya.diContainer.example.Car";
        boolean result = BeanAnalyzer.hasInjectAnnotation(className);
        assertTrue(result, "yes");
    }

    @Test
    void testHasInjectAnnotation_Engine() {
        String className = "ru.nsu.kisadilya.diContainer.example.Engine";
        boolean result = BeanAnalyzer.hasInjectAnnotation(className);
        assertTrue(result, "yes");
    }

    @Test
    void testHasInjectAnnotation_Piston() {
        String className = "ru.nsu.kisadilya.diContainer.example.Piston";
        boolean result = BeanAnalyzer.hasInjectAnnotation(className);
        assertFalse(result, "no");
    }

}