package Tests;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;

import io.github.bonigarcia.wdm.managers.ChromeDriverManager;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.*;

import static org.testng.Assert.*;

import org.openqa.selenium.*;


public class SimplePersonalLoanTest {
    private WebDriver driver;
    private String baseUrl;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();

    @BeforeClass(alwaysRun = true)
    public void setUp() throws Exception {
        ChromeDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-fullscreen");
        driver = new ChromeDriver(options);
        baseUrl = "https://www.oldmutual.co.za";
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @Test
    public void personalLoanTestCase() throws Exception {
        driver.get(baseUrl + "/personal/solutions/bank-and-borrow");
        driver.findElement(By.linkText("Our Solutions"));
        driver.findElement(By.xpath("//om-wc-config[@id='app']/div/om-solutions-detail-page/om-page/div/div/div/div/header/div/om-header-with-breadcrumbs/div/om-main-navigation/div/nav/div/div[2]/om-main-navigation-menu/ul/li/ul/li[3]/ul/li/ul/li[2]/a/span")).click();
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='CALCULATE'])[1]/following::*[name()='svg'][1]")).click();
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Use our Personal Loan Calculator'])[1]/following::*[name()='svg'][2]")).click();
        driver.findElement(By.id("R50000")).click();
        driver.findElement(By.xpath("//om-button[@id='undefined']/button")).click();
        driver.findElement(By.xpath("//om-form-dropdown-field-wrapper[@id='repaymentDuration']/div/div/div/om-form-dropdown-field/div/div/span")).click();
        driver.findElement(By.id("60 Months")).click();
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Back'])[1]/following::span[3]")).click();
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }

    private boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    private String closeAlertAndGetItsText() {
        try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            if (acceptNextAlert) {
                alert.accept();
            } else {
                alert.dismiss();
            }
            return alertText;
        } finally {
            acceptNextAlert = true;
        }
    }
}



