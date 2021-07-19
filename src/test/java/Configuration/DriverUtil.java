package Configuration;


import io.github.bonigarcia.wdm.managers.ChromeDriverManager;
import io.github.bonigarcia.wdm.managers.FirefoxDriverManager;
import io.github.bonigarcia.wdm.managers.InternetExplorerDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.io.FileHandler;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

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
    private static final String FIREFOX = "firefox";
    private static final String IE = "ie";
    private static final String DEFAULT = "chrome";

    public DriverUtil() {
        try {
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
                        System.setProperty("webdriver.chrome.driver", "chromedriver");
                        ReporterLog("Setting up driver for MAC");
                        break;
                    default:
                        Assert.fail("Unable to detect current os: " + System.getProperty("os.name"));
                }
            } else {
                ReporterLog("Running On Docker");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getBaseUrl() {
        String url = null;
        try {
            String environment = DefaultConfig.getTestEnv();
            url = "";
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return url;
    }

    @BeforeClass
    public void setupDriver() {
        getDriver();
    }


    @BeforeTest(alwaysRun = true)
    public void setupGlob() {
        try {
            propertiesLoader.loadProperties();

            if (DefaultConfig.getTestEnv().equalsIgnoreCase("dev")){
                propertiesLoader.setProperty("environment", "QA");
            } else {
                propertiesLoader.setProperty("environment", DefaultConfig.getTestEnv());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getDriver(){
        try {
            // Uses chrome driver by default
            String browser = propertiesLoader.getProperties("application.test.browser");
            if (browser == null) {
                browser = DEFAULT;
            }
            if (browser.toLowerCase().equals(FIREFOX)) {
                FirefoxDriverManager.firefoxdriver().setup();
                WEB_DRIVER = new FirefoxDriver();
                goToUrl(getBaseUrl());
            } else if (browser.toLowerCase().equals(IE)) {
                InternetExplorerDriverManager.iedriver().setup();
                WEB_DRIVER = new InternetExplorerDriver();
                WEB_DRIVER.manage().window().setSize(new Dimension(screenWidth, screenHeight));
                goToUrl(getBaseUrl());

            } else {
                ChromeDriverManager.chromedriver().setup();
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--start-fullscreen");
                WEB_DRIVER = new ChromeDriver(options);
                goToUrl(getBaseUrl());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUpDocker() {
        //TODO Set up Docker Code
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

    @AfterClass
    public void tearDownTest() {
        tearDown();
    }

    @AfterMethod
    public void captureScreenShot(ITestResult testResult) throws IOException {
        try {
            String location = System.getProperty("user.dir");

            if (testResult.getStatus() == ITestResult.FAILURE) {
                File scrFile = ((TakesScreenshot) WEB_DRIVER).getScreenshotAs(OutputType.FILE);

                Date date = new Date();
                long time = date.getTime();
                Timestamp timestamp = new Timestamp(time);

                FileHandler.copy(scrFile, new File(location + "/src/test/resources/FailedScreenShots/" + testResult.getName() + "-" + timestamp + ".jpg"));
            }
        } catch (WebDriverException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    //TODO Set up screen resolution
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
