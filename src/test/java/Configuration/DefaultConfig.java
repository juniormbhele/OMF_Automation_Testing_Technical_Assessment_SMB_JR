package Configuration;

import java.util.ResourceBundle;

public class DefaultConfig {

    private static final String TEST_ENV = "test_env";

    public static String getTestEnv() {
        return (getMavenValue() != null) ? getMavenValue()
                : getJenkinsValue() != null ? getJenkinsValue()
                : getValueFromDefaultProperties();
    }

    private static String getValueFromDefaultProperties() {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("default");
        return resourceBundle.getString(TEST_ENV);
    }

    private static String getMavenValue() {
        return formatValueString(System.getenv().get(TEST_ENV));
    }

    private static String getJenkinsValue() {
        return formatValueString(System.getenv().get(TEST_ENV));
    }

    private static String formatValueString(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        } else {
            return value.trim();
        }
    }

}