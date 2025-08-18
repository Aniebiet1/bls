package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class AppConfig {
    private static final Properties props = new Properties();
    private static boolean loaded = false;

    private static void ensureLoaded() {
        if (loaded) return;
        Path p = Path.of("config", "app.properties");
        if (!Files.exists(p)) {
            System.err.println("Warning: config/app.properties not found at " + p.toAbsolutePath());
            loaded = true;
            return;
        }
        try (FileInputStream in = new FileInputStream(p.toFile())) {
            props.load(in);
            loaded = true;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config/app.properties", e);
        }
    }

    public static String get(String key) {
        ensureLoaded();
        return props.getProperty(key);
    }

    public static String getOrDefault(String key, String def) {
        ensureLoaded();
        return props.getProperty(key, def);
    }
}
