package ru.nsu.kisadilya.diContainer.config.model;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConstructorArg {
    private String type;
    private Object value;

    @JsonSetter("value")
    public void setValue(Object value) {
        this.value = convertValue(value);
    }
    private Object convertValue(Object rawValue) {

        String value = rawValue.toString();

        try {
            return switch (type.toLowerCase()) {
                case "int", "integer" -> Integer.parseInt(value);
                case "long" -> Long.parseLong(value);
                case "float" -> Float.parseFloat(value);
                case "double" -> Double.parseDouble(value);
                case "boolean" -> Boolean.parseBoolean(value);
                case "string" -> value;
                case "ref" -> new BeanConstructorArg(value);
                default -> throw new IllegalArgumentException("Unsupported type: " + type);
            };
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    String.format("Invalid value '%s' for type '%s'", value, type), e);
        }
    }
}
