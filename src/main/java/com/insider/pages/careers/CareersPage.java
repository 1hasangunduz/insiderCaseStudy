package com.insider.pages.careers;

import com.insider.base.BasePage;
import com.insider.base.Driver;
import com.insider.utilities.Log;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class CareersPage extends BasePage {

    @FindBy(css = "#navbarNavDropdown li")
    List<WebElement> navBarItems;

    @FindBy(css = "#navbarNavDropdown .dropdown-sub")
    List<WebElement> navBarSubItems;

    @FindBy(id = "career-find-our-calling")
    WebElement careerFindOurCalling;

    @FindBy(css = "section[id=career-why-become-one-us]")
    WebElement careerWhyBecomeOneUs;

    @FindBy(css = "section[id=career-our-location]")
    WebElement careerOurLocation;

    @FindBy(css = "section[id='find-job-widget']")
    WebElement findJobWidget;

    @FindBy(xpath = "//h2[normalize-space()='Life at Insider']/ancestor::section")
    WebElement lifeAtInsider;

    @FindBy(id = "footer")
    WebElement footer;

    public CareersPage() {
        PageFactory.initElements(Driver.getDriver(), this);
    }

    @Step("Select navigation bar tab: {navBarTab}")
    public CareersPage selectNavBarTabItem(String navBarTab) {
        clickItemByText(navBarItems, navBarTab, "Click on nav bar: ");
        return this;
    }

    @Step("Select navigation bar sub-item: {navBarSubItem}")
    public CareersPage selectNavBarSubItem(String navBarSubItem) {
        clickItemByText(navBarSubItems, navBarSubItem, "Click on nav bar, sub item: ");
        return this;
    }

    private void clickItemByText(List<WebElement> elements, String text, String logMessage) {
        for (WebElement element : elements) {
            if (getTextOfElement(element).equals(text)) {
                clickElement(element, logMessage + text);
                return;
            }
        }
        Log.fail("Element with text '" + text + "' not found.");
    }

    @Step("Verify redirected URL contains: {url}")
    public CareersPage redirectUrlControl(String url) {
        redirectControl(url);
        return this;
    }

    @Step("Verify all required sections are displayed on the page")
    public CareersPage verifyCareerPageSections() {

        verifyElementDisplayed(careerFindOurCalling, "Career - Find Our Calling section");
        verifyElementDisplayed(careerWhyBecomeOneUs, "Career - Why Become One of Us section");
        verifyElementDisplayed(careerOurLocation, "Career - Our Location section");
        verifyElementDisplayed(findJobWidget, "Find Job Widget section");
        verifyElementDisplayed(lifeAtInsider, "Life at Insider section");
        verifyElementDisplayed(footer, "Footer section");
        return this;
    }
}
