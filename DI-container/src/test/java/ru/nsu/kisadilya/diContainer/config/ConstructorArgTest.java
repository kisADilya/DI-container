package ru.nsu.kisadilya.diContainer.config;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ru.nsu.kisadilya.diContainer.config.model.BeanConstructorArg;
import ru.nsu.kisadilya.diContainer.config.model.Config;
import ru.nsu.kisadilya.diContainer.config.model.ConstructorArg;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ConstructorArgTest {
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void shouldParseIntValue() throws Exception {
        String json = """
        {
            "type": "int",
            "value": "4"
        }
        """;
        ConstructorArg arg = mapper.readValue(json, ConstructorArg.class);

        assertThat(arg.getValue())
                .isInstanceOf(Integer.class)
                .isEqualTo(4);
    }

    @Test
    void shouldParseFloatValue() throws Exception {
        String json = """
        {
            "type": "float",
            "value": "3.14"
        }
        """;
        ConstructorArg arg = mapper.readValue(json, ConstructorArg.class);

        assertThat(arg.getValue())
                .isInstanceOf(Float.class)
                .isEqualTo(3.14f);
    }

    @Test
    void shouldParseStringValue() throws Exception {
        String json = """
        {
            "type": "String",
            "value": "Hello"
        }
        """;
        ConstructorArg arg = mapper.readValue(json, ConstructorArg.class);
        assertThat(arg.getValue())
                .isInstanceOf(String.class)
                .isEqualTo("Hello");
    }

    @Test
    void shouldParseBeanReference() throws Exception {
        String json = """
        {
            "type": "ref",
            "value": "myRepository"
        }
        """;
        ConstructorArg arg = mapper.readValue(json, ConstructorArg.class);
        assertThat(arg.getValue())
                .isInstanceOf(BeanConstructorArg.class)
                .extracting("ref")
                .isEqualTo("myRepository");
    }

    @Test
    void shouldThrowOnInvalidInt() {
        String json = """
        {
            "type": "int",
            "value": "aaa"
        }
        """;
        assertThatThrownBy(() -> mapper.readValue(json, ConstructorArg.class))
                .isInstanceOf(JsonMappingException.class);
    }

    @Test
    void shouldParseConstructorArgsCorrectly() throws Exception {
        Config config = ConfigReader.readConfig("src/test/resources/improved.json");

        List<ConstructorArg> args = config.getBeans()
                .get("com.example.MyService")
                .getConstructorArgs();

        assertThat(args).hasSize(4);

        assertThat(args.get(0).getValue())
                .isInstanceOf(BeanConstructorArg.class)
                .extracting("ref").isEqualTo("myRepository");
        assertThat(args.get(1).getValue())
                .isInstanceOf(Integer.class)
                .isEqualTo(5);
        assertThat(args.get(2).getValue())
                .isInstanceOf(String.class)
                .isEqualTo("Vyzdoravlivaite!");
        assertThat(args.get(3).getValue())
                .isInstanceOf(Integer.class)
                .isEqualTo(42);
    }
    @Test
    void shouldParseConstructorWithException() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> {
            ConfigReader.readConfig("src/test/resources/improvedButWrong.json");
        });
    }
    @Test
    void shouldParseExistingRelativeFile() throws Exception {
        String relativePath = "src/test/resources/temp_test_file.txt";
        Files.writeString(Path.of(relativePath), "test content");

        try {
            String json = """
        {
            "type": "java.io.File",
            "value": "src/test/resources/temp_test_file.txt"
        }
        """;

            ConstructorArg arg = new ObjectMapper().readValue(json, ConstructorArg.class);

            assertThat((File)arg.getValue())
                    .exists()
                    .isFile()
                    .hasFileName("temp_test_file.txt");
        } finally {
            Files.deleteIfExists(Path.of(relativePath));
        }
    }
}
