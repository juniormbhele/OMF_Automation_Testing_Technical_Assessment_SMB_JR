package Pages;


import Poms.PomBase;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;


import java.util.List;
import java.util.NoSuchElementException;

import static Configuration.DriverUtil.WEB_DRIVER;
import static Util.ReporterOutput.ReporterLog;

public class OMFHomePage extends PomBase {


    JavascriptExecutor js = (JavascriptExecutor) WEB_DRIVER;
    @FindBy(xpath = "//a[@href='/personal']")
    private WebElement menuPersonButton;

    @FindBy(xpath = "//span[.='LEARN MORE']")
    private WebElement personalLoanLearnMoreButton;

    @FindBy(xpath = "//span[.='Bank & Borrow']")
    private WebElement bankAndBorrowButton;

    @FindBy(linkText = "Our Solutions")
    private WebElement ourSolutionsButton;

    @FindBy(xpath = "//li[3]/ul/li/ul/li[2]/a/span")
    private WebElement personLoanButton;

    @FindBy(xpath = "//a[@href='#calculator_container']")
    private WebElement calculatePersonalLoan;

    @FindBy(xpath = "//om-button[@id='undefined']/button\"")
    private WebElement personalLoanRequestedAmount;



    public OMFHomePage() {
        PageFactory.initElements(WEB_DRIVER, this);
    }

    public void clickOnPersonalLoanLearnMoreButton() {
        try {
        if (homePageTitleValidation()) {
            //TODO Investigate why the Learn More button is not working for personal loan.
            //clickOnBankAndBorrowButton();
            //js.executeScript("arguments[0].scrollIntoView();", personalLoanLearnMoreButton);
            //link to "Learn More" on Person loan cannot be clicked, Using the temporally solution, LinkText, PartialText, Xpath and CCSelector not working
            //personalLoanLearnMoreButton.click();
            WEB_DRIVER.get("https://www.oldmutual.co.za/personal/solutions/bank-and-borrow/personal-loans/");
            this.personalLoanPageTitleValidation();        }
        else {
            ReporterLog("Personal Loan - Learn More is not displayed");
        }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void selectPersonalLoanRequestAmount() {

        //TODO This dropdown is giving some issues, investigate here
//
//         boolean b = selectFromDropDownMenuByXpath
//                ("/html/body/div/div[1]/om-wc-config/div/om-page/div/div/main/om-application-container/div/om-1-col-layout/div/om-section/div/div[2]/div[2]/div/om-personal-loans-calculator/div/div[1]/form/div/om-form-dropdown-field-wrapper/div/div/div/om-form-dropdown-field/div/ul", "R50 000");
//
//        WebElement tobeCalcalatedAmountDrop = personalLoanRequestedAmount;
//        WebElement webElement = WEB_DRIVER.findElement(By.id("loanAmount"));
//        webElement.click();
//        Select tobeCalcalatedAmount  = new Select(WEB_DRIVER.findElement(By.id("loanAmount")));

//        WebElement dropdownElement = WEB_DRIVER.findElement(By.cssSelector("#loanAmount > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > om-form-dropdown-field:nth-child(2) > div:nth-child(1) > div:nth-child(3) > select:nth-child(1)"));
//        Select dropdown = new Select(dropdownElement);

//        WebElement btn =  WEB_DRIVER.findElement(By.cssSelector("#R250000"));

//        btn.click();

//        List<WebElement> allElements=WEB_DRIVER.findElements(By.cssSelector("ul.hide"));
//        System.out.println("Size of List: "+allElements.size());
//
//        for(WebElement ele :allElements) {
//            System.out.println("Name + Number===>"+ele.getText());
//        }


        // Open the dropdown so the options are visible
        WEB_DRIVER.findElement(By.className("dropdown-options-list hide")).click();
        // Get all of the options
        List<WebElement> options = WEB_DRIVER.findElements(By.className("select-wrapper"));
        // Loop through the options and select the one that matches
        String option = "R50 000";
        for (WebElement opt : options) {
            if (opt.getText().equals(option)) {
                opt.click();
                return;
            }
        }
        throw new NoSuchElementException("Can't find " + option + " in dropdown");

    }


        public void clickOnCalculatePersonLoanButton() {
            try {
                if (personalLoanPageTitleValidation()) {
                    // Scrolling down the page till the element is found
                    js.executeScript("arguments[0].scrollIntoView();", calculatePersonalLoan);
                    calculatePersonalLoan.click();

                }
                else {
                    ReporterLog("Personal Loan - Learn More is not displayed");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    public void clickOnBankAndBorrowButton() {
        try {
            if (homePageTitleValidation()) {
                // Scrolling down the page till the element is found
                js.executeScript("arguments[0].scrollIntoView();", bankAndBorrowButton);
                bankAndBorrowButton.click();

            }
            else {
                ReporterLog("Personal Loan - Learn More is not displayed");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private boolean personalLoanPageTitleValidation() {
        try {
            ReporterLog("Current Page is :" +WEB_DRIVER.getTitle());
            Assert.assertEquals(WEB_DRIVER.getTitle(), "Personal Loans | Apply Online | Old Mutual");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return WEB_DRIVER.getTitle().equalsIgnoreCase("Personal Loans | Apply Online | Old Mutual");

    }


    public boolean homePageTitleValidation()
    {
        try {
            ReporterLog("Current Page is :" +WEB_DRIVER.getTitle());
            Assert.assertEquals(WEB_DRIVER.getTitle(), "Bank and Borrow Solutions | Old Mutual");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return WEB_DRIVER.getTitle().equalsIgnoreCase("Bank and Borrow Solutions | Old Mutual");

    }

}