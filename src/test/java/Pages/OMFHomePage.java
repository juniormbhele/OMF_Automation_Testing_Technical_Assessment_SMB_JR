package Pages;


import Poms.PomBase;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


import static Configuration.DriverUtil.WEB_DRIVER;
import static Util.ReporterOutput.ReporterLog;

public class OMFHomePage extends PomBase {

    @FindBy(xpath = "//*[@id=\"product_carousel\"]/om-product-carousel/div/om-section/div/div/div[2]/div/om-carousel-container/div/div[1]/ul/li[2]/om-refined-product-card/om-product-card/div/div[2]/div[2]/div[1]/om-button/a/span")
    private WebElement personalLoanLearnMoreButton;
    @FindBy(linkText = "Home")
    private WebElement homePageLink;


    public OMFHomePage() {
        PageFactory.initElements(WEB_DRIVER, this);
    }

    public void clickOnpersonalLoanLearnMoreButton() {
        if (waitForWebElement(personalLoanLearnMoreButton)) {
            personalLoanLearnMoreButton.click();
        }
        else {
            ReporterLog("Personal Loan - Learn More is not displayed");
        }

    }

    public void waitForHomePage() {
        waitForWebElement(homePageLink);
        ReporterLog("Home Page Link displayed");
    }

}