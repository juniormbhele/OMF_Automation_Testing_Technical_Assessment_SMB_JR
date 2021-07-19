package Configuration;

import Util.ReporterOutput;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Config file for loading up the application.properties file
 */
public class PropertiesLoader {

    private static final Properties properties = new Properties();
    private ClassPathResource resource = new ClassPathResource("application.properties");
    private InputStream inputStream = null;

    /**
     * Loads up the application.properties file
     *
     * @return Instance of the Properties class
     */
    public Properties loadProperties() {
        try {
            inputStream = resource.getInputStream();
            properties.load(inputStream);
        } catch (IOException e) {
            ReporterOutput.ReporterLog(e.getMessage());
        }

        return properties;
    }

    /**
     * Sets a custom property
     *
     * @Returns instance of property
     */
    public Properties setProperty(String key, String value) {
        properties.setProperty(key, value);

        return properties;
    }

    /**
     * Gets a custom property
     *
     * @param key property key
     * @return
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * Gets a specific property per property key value
     *
     * @param key Property key to get
     * @return Property value
     */
    public String getProperties(String key) {
        return properties.getProperty(key);
    }
}
