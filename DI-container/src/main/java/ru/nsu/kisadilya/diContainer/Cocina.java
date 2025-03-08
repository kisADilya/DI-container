package ru.nsu.kisadilya.diContainer;

import ru.nsu.kisadilya.diContainer.config.ConfigReader;
import ru.nsu.kisadilya.diContainer.config.model.Config;

public class Cocina {
    private final Config config;
    public Cocina(String configFileName) {
        config = ConfigReader.readConfig(configFileName);

        //используем конфиг дальше для инициализации всего, что можно сразу
    }

    public Object getIngrediente() {
        return new Object();
    }

}
