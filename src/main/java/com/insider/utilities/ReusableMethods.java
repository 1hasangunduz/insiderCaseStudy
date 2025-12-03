package com.insider.utilities;


import com.insider.base.BaseTest;
import com.insider.base.Driver;
import com.insider.configs.Configs;
import io.qameta.allure.Step;
import io.qameta.allure.testng.AllureTestNg;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Listeners;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;


@Listeners(AllureTestNg.class)
public class ReusableMethods extends BaseTest {

    public final Configs config = Configs.getConfigs();
    private final Logger logger = LogManager.getLogger(ReusableMethods.class);
    public static final int DEFAULT_WAIT_TIME = 3;

    public WebElement waitVisibleByLocator(WebElement element) {
        return waitVisibleByLocator(element, DEFAULT_WAIT_TIME);
    }

    /**
     * @param env : choose environment
     * @param url : choose url for environment (example: NoAdditionalQueues) or full url (example: /empty)
     */
    public void navigateToUrl(String env, String url) {
        String baseUrl;
        if (url == null || url.isEmpty()) {
            url = ConfigReader.getProperty("NoAdditionalQueues");
        }
        switch (env.toLowerCase()) {
            case "prod" -> baseUrl = "https://useinsider.com/";
            case "preprod" -> baseUrl = "https://preprod.useinsider.com";
            case "cloud" -> baseUrl = "https://cloud.useinsider.com";
            case "test" -> baseUrl = "https://test.useinsider.com";
            default -> {
                Log.warning("Invalid environment provided: " + env);
                return;
            }
        }
        String fullUrl = baseUrl + url;
        Driver.getDriver().get(fullUrl);
        Driver.getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(40));
        Log.pass("Application launched! URL: " + fullUrl);
        waitMs(3000);
        acceptCookies();
    }

    /*=======================================================================================*/

    @Step("Navigate to URL")
    public void navigateToUrl() {
        var env = config.env();
        String baseUrl;
        try {
            baseUrl = config.baseUrl();
            if (baseUrl == null || baseUrl.isEmpty()) {
                Log.warning("Invalid or missing base URL for environment: " + env);
                return;
            }
            Driver.getDriver().get(baseUrl);
            Driver.getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(40));
            Log.pass("Application launched! URL: " + baseUrl);
            waitMs(3000);
            acceptCookies();
        } catch (Exception e) {
            Log.error("Error navigating to URL: " + e.getMessage());
        }
    }

    /**
     * This method is used to accept cookies.
     * If it is not displayed, it will print a message on the console.
     * If it is displayed, it will click on the 'Accept All Cookies' button.
     */
    @Step("Accept browser cookies.")
    public void acceptCookies() {
        var acceptCookiesButton = Driver.getDriver().findElement(By.cssSelector("#cookie-law-info-bar #wt-cli-accept-all-btn"));
        if (isDisplayElement(acceptCookiesButton)) {
            Log.pass("->" + acceptCookiesButton.getSize().getHeight() + "x" + acceptCookiesButton.getSize().getWidth());
            assertEquals(checkWebElementSize(acceptCookiesButton), "29x112", " The 'Accept All Cookies' button size is not correct.");
            Log.pass("Confirmed that the 'Accept All Cookies' button size is correct. Button size --> " + checkWebElementSize(acceptCookiesButton));
            clickWithJS(acceptCookiesButton, "Accept all browser cookies!");
        } else {
            Log.pass("The 'Accept Cookies' button is not displayed.");
        }
    }

    @Step("Waits {time} seconds for the locator to become visible")
    public WebElement waitVisibleByLocator(WebElement element, int time) {
        try {
            new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(time)).until(ExpectedConditions.visibilityOf(element));
        } catch (Exception e) {
            logger.error("Web element is not visible!");
        }
        return element;
    }

    @Step("Hover on element")
    public void hoverElement(WebElement webElement, int second) {
        Actions action = new Actions(Driver.getDriver());
        action.moveToElement(webElement).pause(Duration.ofSeconds(second)).perform();

    }

    public String getTextElement(WebElement element) {
        return waitVisibleByLocator(element).getText();
    }

    public String getTextOfElement(WebElement elem) {
        String text = null;
        try {
            text = elem.getText();
            Log.pass(text);
        } catch (Exception exception) {
            Log.fail("Error while getting text of element: ", exception);
        }
        return text;
    }

    public boolean isDisplayElement(WebElement webElement) {
        WebElement element = waitVisibleByLocator(webElement);
        return element.isDisplayed();
    }

    public void clickElement(WebElement webElement, String message) {
        waitVisibleByLocator(webElement);
        scrollToElementBlockCenter(webElement);
        waitClickAbleByOfElement(webElement).click();
        Log.pass(message);
    }

    /**
     * @param webElement : get Images size like : 203x203
     */
    public String checkElementSize(WebElement webElement) {
        WebElement element = waitVisibleByLocator(webElement);
        return (element.getSize().getHeight() + "x" + element.getSize().getWidth());

    }

    @Step("Check Web Element Size")
    public String checkWebElementSize(WebElement webElement) {
        return (webElement.getSize().getHeight() + "x" + webElement.getSize().getWidth());
    }

    @Step("Click With JS")
    public void clickWithJS(WebElement element, String message) {
        ((JavascriptExecutor) Driver.getDriver()).executeScript("arguments[0].scrollIntoView(true);", element);
        ((JavascriptExecutor) Driver.getDriver()).executeScript("arguments[0].click();", element);
        Log.pass(message);
    }

    @Step("Scroll To Element Block Center")
    public void scrollToElementBlockCenter(WebElement element) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) Driver.getDriver();
            js.executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
        } catch (Exception e) {
            logger.fatal("Error while scrolling to the element : ", e);
        }

    }

    @Step("Switch To New Window")
    public void switchToNewWindow() {
        String origin = Driver.getDriver().getWindowHandle();
        for (String handle : Driver.getDriver().getWindowHandles()) {
            if (!handle.equals(origin)) {
                Driver.getDriver().switchTo().window(handle);
            }
        }
    }

    @Step("Close all tabs except the first tab and switch back to the main tab")
    public static void closeAllTabsExceptMain() {

        List<String> allTabs = new ArrayList<>(Driver.getDriver().getWindowHandles());
        String mainTab = allTabs.get(0);
        for (int i = 1; i < allTabs.size(); i++) {
            Driver.getDriver().switchTo().window(allTabs.get(i));
            Driver.getDriver().close();
        }

        Driver.getDriver().switchTo().window(mainTab);
    }


}