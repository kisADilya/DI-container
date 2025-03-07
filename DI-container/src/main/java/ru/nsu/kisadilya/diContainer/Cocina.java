package ru.nsu.kisadilya.diContainer;

public class Cocina {
    private ConfigReader configReader;
    public Cocina(String configFileName) {
        ConfigReader configReader = new ConfigReader(configFileName);
        configReader.readConfig();
    }

    public Object getIngrediente() {

    }

}
