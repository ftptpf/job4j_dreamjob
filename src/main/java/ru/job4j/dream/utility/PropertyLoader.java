package ru.job4j.dream.utility;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Properties;

/**
 * Загружаем свойства из app.properties файла
 */
public class PropertyLoader {
    private static Properties properties = new Properties();
    private static Path path = Paths.get("resources", "app.properties");
    //private static Path path = Path.of("src/resources/app.properties");

    private PropertyLoader() {
    }

    private void init() {
        try (InputStream inputStream = getClass().getResourceAsStream("/app.properties");
             BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream)))) {
            properties.load(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


/*    static {
        try (BufferedInputStream bf = new BufferedInputStream(new FileInputStream(path.toFile()))) {
            properties.load(bf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    public static String get(String key) {
        init();

/*        try (BufferedInputStream bf = new BufferedInputStream(new FileInputStream("resources//app.properties"))) {
            properties.load(bf);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        return properties.getProperty(key);
    }

/*    public static void main(String[] args) {
*//*        try (BufferedInputStream bf = new BufferedInputStream(new FileInputStream(path.toFile()))) {
            properties.load(bf);
        } catch (IOException e) {
            e.printStackTrace();
        }*//*
        System.out.println(PropertyLoader.get("store"));
    }*/
}
