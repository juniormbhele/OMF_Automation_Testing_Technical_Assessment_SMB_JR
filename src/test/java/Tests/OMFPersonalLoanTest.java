package Tests;


import Configuration.DriverUtil;
import Pages.OMFHomePage;
import Util.ReporterOutput;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class OMFPersonalLoanTest extends DriverUtil {

    private OMFHomePage omfHomePage;

    @BeforeMethod
    private void setupPageObjects() {
        omfHomePage = new OMFHomePage();

    }

    @Test
    public void navigateToOMFHomePage() {

        omfHomePage.homePageTitleValidation();
    }

    @Test
    public void navigateToPersonalLoanPage()  {
        omfHomePage.clickOnPersonalLoanLearnMoreButton();
        omfHomePage.clickOnCalculatePersonLoanButton();
        omfHomePage.selectPersonalLoanRequestAmount();
    }


}