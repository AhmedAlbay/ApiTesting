package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Reads settings (like the API key) from config.properties
 * instead of hardcoding them directly in the test code.
 */
public class ConfigManager {

    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ConfigManager.class
                .getClassLoader()
                .getResourceAsStream("config.properties")) {

            if (input == null) {
                throw new RuntimeException("config.properties file not found in resources!");
            }
            properties.load(input);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties: " + e.getMessage(), e);
        }
    }

    public static String getApiKey() {
        return properties.getProperty("api.key");
    }
}