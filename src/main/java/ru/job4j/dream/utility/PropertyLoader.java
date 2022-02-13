package ru.job4j.dream.utility;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;

/**
 * Загружаем свойства из app.properties файла
 */
public class PropertyLoader {
    private static Properties properties = new Properties();
    private static Path path = Path.of("resources", "app.properties");

    private PropertyLoader() {
    }

/*    private static void init() {
        try (BufferedInputStream bf = new BufferedInputStream(new FileInputStream(path.toFile()))) {
            properties.load(bf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/


/*    static {
        try (BufferedInputStream bf = new BufferedInputStream(new FileInputStream(path.toFile()))) {
            properties.load(bf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    public static String get(String key) {
        try (BufferedInputStream bf = new BufferedInputStream(new FileInputStream(path.toFile()))) {
            properties.load(bf);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
