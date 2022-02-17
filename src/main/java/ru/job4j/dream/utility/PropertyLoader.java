package ru.job4j.dream.utility;

import java.io.*;
import java.util.Objects;
import java.util.Properties;

/**
 * Загружаем свойства из app.properties файла
 */
public class PropertyLoader {
    private static final Properties PROPERTIES = new Properties();

    private PropertyLoader() {
    }

    public static String get(String key) {
        if (PROPERTIES.isEmpty()) {
            try (InputStream inputStream = PropertyLoader.class.getResourceAsStream("/app.properties");
                 BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream)))) {
                PROPERTIES.load(reader);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return PROPERTIES.getProperty(key);
    }
}
