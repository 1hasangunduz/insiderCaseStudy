package com.insider.pages.homepage;

import com.insider.base.BasePage;
import com.insider.base.Driver;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


public class HomePage extends BasePage {

    @FindBy(css = "nav[id=navigation]")
    WebElement navBarHead;

    @FindBy(css = "*[id=desktop_hero_24]")
    WebElement desktopHero;

    @FindBy(css = "*[id=home_logo_reel]")
    WebElement homeLogoReel;

    @FindBy(css = "*[id='case-studies-home-dots']")
    WebElement caseStudiesHomeDots;

    @FindBy(css = "*[id='one_platform_infinite_cx']")
    WebElement onePlatformInfiniteCx;

    @FindBy(css = "*[id='journey-container']")
    WebElement journeyContainer;

    @FindBy(css = "*[id='sirius_ai']")
    WebElement siriusAi;

    @FindBy(css = "*[id='tabbed-content-home']")
    WebElement tabbedContentHome;

    @FindBy(css = "*[id='testimonials']")
    WebElement testimonials;

    @FindBy(css = "*[id='home-integrations']")
    WebElement homeIntegrations;

    @FindBy(css = "*[id='home-cta-banner']")
    WebElement homeCtaBanner;

    @FindBy(id = "footer")
    WebElement footer;

    public HomePage() {
        PageFactory.initElements(Driver.getDriver(), this);
    }

    @Step("Verify all required sections are displayed on the Home Page")
    public HomePage verifyHomePageSections() {

        verifyElementDisplayed(navBarHead, "Navigation Bar Head");
        verifyElementDisplayed(desktopHero, "Desktop Hero Section");
        verifyElementDisplayed(homeLogoReel, "Home Logo Reel Section");
        verifyElementDisplayed(caseStudiesHomeDots, "Case Studies Home Dots Section");
        verifyElementDisplayed(onePlatformInfiniteCx, "One Platform Infinite CX Section");
        verifyElementDisplayed(journeyContainer, "Journey Container Section");
        verifyElementDisplayed(siriusAi, "Sirius AI Section");
        verifyElementDisplayed(tabbedContentHome, "Tabbed Content Home Section");
        verifyElementDisplayed(testimonials, "Testimonials Section");
        verifyElementDisplayed(homeIntegrations, "Home Integrations Section");
        verifyElementDisplayed(homeCtaBanner, "Home CTA Banner Section");
        verifyElementDisplayed(footer, "Footer Section");

        return this;
    }
}
