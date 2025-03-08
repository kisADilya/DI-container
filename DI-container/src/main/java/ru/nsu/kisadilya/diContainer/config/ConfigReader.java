package ru.nsu.kisadilya.diContainer.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.nsu.kisadilya.diContainer.config.model.Config;
import java.io.File;
import java.io.IOException;

public class ConfigReader {
    public static Config readConfig(String filename) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(new File(filename), Config.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("Config file " + filename + " is invalid");
        }
    }

}
