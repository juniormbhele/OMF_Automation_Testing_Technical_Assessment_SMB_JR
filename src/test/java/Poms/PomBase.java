package Poms;

import Configuration.PropertiesLoader;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.util.List;
import java.util.Random;

import static Configuration.DriverUtil.WEB_DRIVER;
import static Util.ReporterOutput.ReporterLog;

public class PomBase {

    private final PropertiesLoader propertiesLoader = new PropertiesLoader();
    private int elementWaitTimeout = 30;

    // Waits
    public void waitForElementToDisappearByXpath(String elementValue) {
        new WebDriverWait(WEB_DRIVER, elementWaitTimeout).until(driver -> (driver.findElements(By.xpath(elementValue)).size() == 0));
    }

    public void waitForWebElementToDisappear(WebElement webElement) {
        new WebDriverWait(WEB_DRIVER, elementWaitTimeout).until(driver -> (ExpectedConditions.invisibilityOf(webElement)));
    }

    public void waitForElementByXpath(String elementValue) {
        new WebDriverWait(WEB_DRIVER, elementWaitTimeout).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(elementValue)));
    }

    public boolean isXpathElementVisible(String elementValue) {
        try {
            return WEB_DRIVER.findElement(By.xpath(elementValue)).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public void waitForElementByName(String elementValue) {
        new WebDriverWait(WEB_DRIVER, elementWaitTimeout).until(ExpectedConditions.visibilityOfElementLocated(By.name(elementValue)));
    }

    public void waitForElementById(String elementValue) {
        new WebDriverWait(WEB_DRIVER, elementWaitTimeout).until(ExpectedConditions.presenceOfElementLocated(By.id(elementValue)));
    }

    public boolean waitForWebElement(WebElement webElement) {
        //Try find element, throw an element and return false if element not found
        try {
            new WebDriverWait(WEB_DRIVER, elementWaitTimeout).until(ExpectedConditions.visibilityOf(webElement));
            return true;
        } catch (Exception e) {
            ReporterLog(e.getMessage());
            ReporterLog("couldnt find element...");
            return false;
        }
    }

    // Clicks
    public void clickOnElementByXpath(String elementValue) {
        waitForElementByXpath(elementValue);

        try {
            WebElement element = WEB_DRIVER.findElement(By.xpath(elementValue));
            specialClickElement(element);
        } catch (StaleElementReferenceException e) {
            WebElement element = WEB_DRIVER.findElement(By.xpath(elementValue));
            specialClickElement(element);
        }
    }

    public void clickOnElementById(String elementValue) {
        waitForElementById(elementValue);
        WebElement element = WEB_DRIVER.findElement(By.id(elementValue));
        specialClickElement(element);
    }

    public void clickOnWebElement(WebElement webElement) {
        waitForWebElement(webElement);
        new WebDriverWait(WEB_DRIVER, elementWaitTimeout).until(ExpectedConditions.elementToBeClickable(webElement));
        specialClickElement(webElement);
    }

    // Send Text
    public void sendTextByXpath(String elementValue, String textValue) {
        waitForElementByXpath(elementValue);
        WebElement element = WEB_DRIVER.findElement(By.xpath(elementValue));

        element.click();
        element.clear();
        element.sendKeys(textValue);
    }

    public void sendTextByName(String elementValue, String textValue) {
        waitForElementByName(elementValue);
        WebElement element = WEB_DRIVER.findElement(By.name(elementValue));

        do {
            element.click();
            element.clear();
            element.sendKeys(textValue);
        } while (element.getText().equalsIgnoreCase(textValue));
    }

    public void sendTextById(String elementValue, String textValue) {
        waitForElementById(elementValue);
        WebElement element = WEB_DRIVER.findElement(By.id(elementValue));
        element.sendKeys(textValue);
    }

    public void sendTextByWebElement(WebElement webElement, String textValue) {
        waitForWebElement(webElement);
        webElement.sendKeys(textValue);
    }

    // Find Element
    public WebElement findElementById(String id) {
        waitForElementById(id);
        return WEB_DRIVER.findElement(By.id(id));
    }

    public WebElement findElementByXpath(String xpath) {
        waitForElementByXpath(xpath);
        return WEB_DRIVER.findElement(By.xpath(xpath));
    }

    // Drop Down Menus
    public boolean selectRandomFromDropDownMenuByXpath(String menuXpath) {
        Select dpMenu = new Select(findElementByXpath(menuXpath));

        List<WebElement> allOptions = dpMenu.getOptions();

        Random random = new Random();

        int index;
        do {
            index = random.nextInt(allOptions.size());
        } while (index == 0);

        WebElement optionToSelect = allOptions.get(index);

        optionToSelect.click();
        return true;
    }

    public boolean selectFromDropDownMenuByXpath(String menuXpath, String textToSelect) {
        WebElement dropDownMenuSeleniumObj = findElementByXpath(menuXpath);
        Select dpMenu = new Select(dropDownMenuSeleniumObj);

        List<WebElement> allOptions = dpMenu.getOptions();

        for (WebElement currentOption : allOptions) {
            if (currentOption.getText().equalsIgnoreCase(textToSelect)) {
                currentOption.click();
                return true;
            }
        }

        return false;
    }

    public void uploadImageByXpath(String xpath, String imagePath) {
        WEB_DRIVER.findElement(By.xpath(xpath)).sendKeys(imagePath);
    }

    public String getBaseUrl(String environment) {
        String url = "";
        switch (environment.toLowerCase()) {
            case "qa":
                url = propertiesLoader.getProperties("application.url.omf.qa");
                break;
            case "staging":
                url = propertiesLoader.getProperties("application.url.omf.staging");
                break;
            case "local":
                url = propertiesLoader.getProperties("application.url.omf.dev");
                break;
            default:
                Assert.fail(environment + " is not a valid environment");
        }

        return url;
    }

    private boolean specialClickElement(WebElement element) {
        try {
            element.click();
            return false;
        } catch (WebDriverException e) {
            JavascriptExecutor js = (JavascriptExecutor) WEB_DRIVER;
            js.executeScript("arguments[0].click();", element);
            return true;
        } catch (Exception e) {
            Assert.fail("Unable to click on element see stack trace");
            return false;
        }
    }
}
