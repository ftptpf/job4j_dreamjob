package ru.job4j.dream.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.dream.store.DbStore;

import java.io.*;
import java.util.Objects;
import java.util.Properties;

/**
 * Загружаем свойства из app.properties файла
 */
public class PropertyLoader {
    private static final Properties PROPERTIES = new Properties();
    private static final Logger LOG = LoggerFactory.getLogger(DbStore.class.getName());

    private PropertyLoader() {
    }

    public static String get(String key) {
        if (PROPERTIES.isEmpty()) {
            try (InputStream inputStream = PropertyLoader.class.getResourceAsStream("/app.properties");
                 BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream)))) {
                PROPERTIES.load(reader);
            } catch (IOException e) {
                LOG.error("IO Load Property Exception information:", e);
            }
        }
        return PROPERTIES.getProperty(key);
    }
}
