package com.insider.tests.homepage;

import com.insider.base.BaseTest;
import com.insider.pages.homepage.HomePage;
import com.insider.utilities.PageInit;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.Test;


@Epic("Verify Navigation Tab Item Tests")
@Feature("Navigation Tab Item Functionality")
@Story("Checking Navigation And Sub Items")
public class HomePageTests extends BaseTest {

    @Description("Verify that all required sections on the Insider Home Page are displayed correctly.")
    @Test(testName = "Verify Home Page Sections are Visible")
    public void verifyNavigationHomePage_Case_1() {
        navigateToUrl();

        PageInit.get(HomePage.class)
                .verifyHomePageSections();
    }

}
