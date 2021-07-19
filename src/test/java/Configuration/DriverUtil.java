package Configuration;



import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

import static Util.ReporterOutput.ReporterLog;

public class DriverUtil {

    public static WebDriver WEB_DRIVER;
    private final PropertiesLoader propertiesLoader = new PropertiesLoader();
    private int screenWidth = 1920;
    private int screenHeight = 1080;

    public DriverUtil() {
        propertiesLoader.loadProperties();
        if (propertiesLoader.getProperties("runOnDocker").equals("false")) {
            switch (System.getProperty("os.name")) {
                case "Windows 10":
                    System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
                    ReporterLog("Setting up driver for Windows");
                    break;
                case "Linux":
                    System.setProperty("webdriver.chrome.driver", "chromedriver-linux");
                    ReporterLog("Setting up driver for Linux");
                    break;
                case "Mac OS X":
                    System.setProperty("webdriver.chrome.driver", "chromedriver-mac");
                    ReporterLog("Setting up driver for MAC");
                    break;
                default:
                    Assert.fail("Unable to detect current os: " + System.getProperty("os.name"));
            }
        } else {
            ReporterLog("Running On Docker");
        }
    }

    public String getBaseUrl() {
        String environment = DefaultConfig.getTestEnv();
        String url = "";
        switch (environment.toLowerCase()) {
            case "qa":
                url = propertiesLoader.getProperties("application.url.omf.qa");
                break;
            case "staging":
                url = propertiesLoader.getProperties("application.url.omf.staging");
                break;
            case "dev":
                url = propertiesLoader.getProperties("application.url.omf.dev");
                break;
            default:
                Assert.fail(environment + " is not a valid environment");
        }

        return url;
    }

    @BeforeMethod
    public void setupDriver() {
        getDriver();
    }


    @BeforeTest(alwaysRun = true)
    public void setupGlob() {
        propertiesLoader.loadProperties();

        if (DefaultConfig.getTestEnv().equalsIgnoreCase("dev")){
            propertiesLoader.setProperty("environment", "QA");
        } else {
            propertiesLoader.setProperty("environment", DefaultConfig.getTestEnv());
        }
    }

    private void getDriver() {
        if (propertiesLoader.getProperty("runOnDocker").equals("false")) {
//            ChromeOptions options = new ChromeOptions();
//            options.addArguments("--use-fake-ui-for-media-stream=1");
            WEB_DRIVER = new ChromeDriver();
            WEB_DRIVER.manage().window().setSize(new Dimension(screenWidth, screenHeight));
        } else if (propertiesLoader.getProperty("runOnDocker").equals("true")) {
            setUpDocker();
        } else {
            Assert.fail("Please set a value to key: runOnDocker on the application.properties file");
        }

        //propertiesLoader.setProperty("sessionId", ((RemoteWebDriver) WEB_DRIVER).getSessionId().toString());
        goToUrl(getBaseUrl());
    }

    private void setUpDocker() {
    }

    public void goToUrl(String url) {
        WEB_DRIVER.get(url);
    }

    public void tearDown() {
        WEB_DRIVER.close();
    }

    public DriverUtil getDriverUtil() {
        return this;
    }

    @AfterMethod
    public void tearDownTest() {
        tearDown();
    }

    @AfterMethod
    public void captureScreenShot(ITestResult testResult) throws IOException {
        String location = System.getProperty("user.dir");

        if (testResult.getStatus() == ITestResult.FAILURE) {
            File scrFile = ((TakesScreenshot) WEB_DRIVER).getScreenshotAs(OutputType.FILE);

            Date date = new Date();
            long time = date.getTime();
            Timestamp timestamp = new Timestamp(time);

            FileHandler.copy(scrFile, new File(location + "/src/test/resources/FailedScreenShots/" + testResult.getName() + "-" + timestamp + ".jpg"));
        }

    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }
}
