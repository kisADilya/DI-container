package ru.nsu.kisadilya.diContainer.config.model;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Constructor;

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
        if (rawValue == null || type == null) {
            return null;
        }

        String stringValue = rawValue.toString();
        String normalizedType = type.toLowerCase();

        try {
            return switch (normalizedType) {
                case "int", "integer" -> Integer.parseInt(stringValue);
                case "short" -> Short.parseShort(stringValue);
                case "long" -> Long.parseLong(stringValue);
                case "float" -> Float.parseFloat(stringValue);
                case "double" -> Double.parseDouble(stringValue);
                case "byte" -> Byte.parseByte(stringValue);
                case "boolean" -> Boolean.parseBoolean(stringValue);
                case "char", "character" -> stringValue.charAt(0);
                case "string" -> stringValue;
                case "ref" -> new BeanConstructorArg(stringValue);
                default -> convertComplexType(type, stringValue);
            };
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    String.format("Invalid value '%s' for type '%s'", stringValue, type), e);
        }
    }

    private Object convertComplexType(String typeName, String value) {
        try {
            Class<?> targetClass = Class.forName(typeName);

            if (targetClass.isEnum()) {
                return Enum.valueOf((Class<? extends Enum>) targetClass, value);
            }

            Constructor<?> ctor = targetClass.getConstructor(String.class);
            return ctor.newInstance(value);
        }
        catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Unknown type: " + typeName, e);
        }
        catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(
                    "Type " + typeName + " doesn't have String constructor", e);
        }
        catch (Exception e) {
            throw new IllegalArgumentException(
                    String.format("Failed to convert '%s' to %s", value, typeName), e);
        }
    }
}