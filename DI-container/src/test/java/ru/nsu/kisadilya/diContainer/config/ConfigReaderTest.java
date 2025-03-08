package ru.nsu.kisadilya.diContainer.config;

import org.junit.jupiter.api.Test;
import ru.nsu.kisadilya.diContainer.config.model.Bean;
import ru.nsu.kisadilya.diContainer.config.model.Config;

import java.io.File;

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
        assertEquals("myService", bean.getName());
        assertEquals("singleton", bean.getScope());
        assertEquals("myRepository", bean.getConstructorArgs().getFirst().getRef());
        assertEquals("1000", bean.getProperties().get("timeout"));
    }
}