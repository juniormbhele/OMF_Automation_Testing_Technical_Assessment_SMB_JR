package Util;

import org.testng.Reporter;

/**
 * Default application logger class
 */
public class ReporterOutput {

    /**
     * Static method for logging access ability
     * Uses the default TestNG logging capability
     * Output the log result to the console with it system output command
     *
     * @param log String input details to log
     * @return Return logged string
     */
    public static String ReporterLog(String log) {
        System.out.println(log);
        Reporter.log(log);
        return log;
    }
}