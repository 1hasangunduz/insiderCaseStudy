package com.insider.utilities;


import com.insider.base.BasePage;
import com.insider.base.BaseTest;
import com.insider.base.Driver;
import com.insider.base.WaitConditions;
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

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;


@Listeners(AllureTestNg.class)
public class ReusableMethods extends BaseTest {
    public final Configs config = Configs.getConfigs();

    private final Logger logger = LogManager.getLogger(ReusableMethods.class);
    public final SecureRandom random = new SecureRandom();

    /*=======================================================================================*/
    public static final int DEFAULT_WAIT_TIME = 3;

    //===============Explicit Wait==============//


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


    /*=======================================================================================*/


    @Step("Waits {time} seconds for the locator to become visible")
    public WebElement waitVisibleByLocator(WebElement element, int time) {
        try {
            new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(time)).until(ExpectedConditions.visibilityOf(element));
        } catch (Exception e) {
            logger.error("Web element is not visible!");
        }
        return element;
    }

    /*=======================================================================================*/
    public String getCurrentURL() {
        var currentUrl = Driver.getDriver().getCurrentUrl();
        Log.pass("Current URL: " + currentUrl);
        return currentUrl;
    }
    /*=======================================================================================*/

    @Step("Navigate to back")
    public void navigateToBack() {
        Driver.getDriver().navigate().back();
        waitForPageToLoad(3);

    }

    /*=======================================================================================*/
    @Step("Hover on element")
    public void hoverElement(WebElement webElement, int second) {
        Actions action = new Actions(Driver.getDriver());
        action.moveToElement(webElement).pause(Duration.ofSeconds(second)).perform();

    }

    /*=======================================================================================*/

    public String getTextElement(WebElement element) {
        return waitVisibleByLocator(element).getText();
    }

    /*=======================================================================================*/

    public String getTextOfElement(WebElement elem) {

        String text = null;
        try {
            text = elem.getText();
            Log.pass(text);
        } catch (Exception exception) {
            logger.fatal("Error while getting text of element: ", exception);
        }
        return text;
    }

    /*=======================================================================================*/
    public boolean isDisplayElement(WebElement webElement) {
        WebElement element = waitVisibleByLocator(webElement);
        return element.isDisplayed();
    }

    /*=======================================================================================*/

    public String getDomPropertyElement(WebElement webElement, String attribute) {
        WebElement element = waitVisibleByLocator(webElement);
        return element.getDomProperty(attribute);
    }

    @Step("Get Dom Attribute Element")
    public List<String> getDomAttributeElement(List<WebElement> webElements, String attribute) {
        List<String> attributes = new ArrayList<>();
        for (WebElement element : webElements) {
            WebElement visibleElement = waitVisibleByLocator(element);
            String value = visibleElement.getDomAttribute(attribute);
            if (value != null) {
                attributes.add(value);
            }
        }
        return attributes;
    }

    @Step("Get Dom Property Element")
    public List<String> getDomPropertyElement(List<WebElement> webElements, String attribute) {
        List<String> properties = new ArrayList<>();
        for (WebElement element : webElements) {
            WebElement visibleElement = waitVisibleByLocator(element);
            String value = visibleElement.getDomProperty(attribute);
            if (value != null) {
                properties.add(value);
            }
        }
        return properties;
    }

    /*=======================================================================================*/

    public void clickElement(WebElement webElement, String message) {
        waitVisibleByLocator(webElement);
        scrollToElementBlockCenter(webElement);
        waitClickAbleByOfElement(webElement).click();
        Log.pass(message);
    }
    /*=======================================================================================*/

    /**
     * @param webElement : get Images size like : 203x203
     */
    public String checkElementSize(WebElement webElement) {
        WebElement element = waitVisibleByLocator(webElement);
        return (element.getSize().getHeight() + "x" + element.getSize().getWidth());

    }

    /*=======================================================================================*/
    @Step("Check Web Element Size")
    public String checkWebElementSize(WebElement webElement) {
        return (webElement.getSize().getHeight() + "x" + webElement.getSize().getWidth());
    }

    /*=======================================================================================*/

    @Step("Click With JS")
    public void clickWithJS(WebElement element, String message) {
        ((JavascriptExecutor) Driver.getDriver()).executeScript("arguments[0].scrollIntoView(true);", element);
        ((JavascriptExecutor) Driver.getDriver()).executeScript("arguments[0].click();", element);
        Log.pass(message);
    }

    /*=======================================================================================*/
    @Step("Scroll To Element Block Center")
    public void scrollToElementBlockCenter(WebElement element, String whereToScroll) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) Driver.getDriver();
            js.executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
            Log.pass("The object where your scroll operation was successful :  " + whereToScroll);
        } catch (Exception e) {
            logger.fatal("Error while scrolling to the element : ", e);
        }

    }

    /*=======================================================================================*/
    @Step("Scroll To Element Block Center")
    public void scrollToElementBlockCenter(WebElement element) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) Driver.getDriver();
            js.executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
        } catch (Exception e) {
            logger.fatal("Error while scrolling to the element : ", e);
        }

    }

    /*=======================================================================================*/
    @Step("Switch To New Window")
    public void switchToNewWindow() {
        String origin = Driver.getDriver().getWindowHandle();
        for (String handle : Driver.getDriver().getWindowHandles()) {
            if (!handle.equals(origin)) {
                Driver.getDriver().switchTo().window(handle);
            }
        }
    }
    /*=======================================================================================*/

    @Step("Close The Main Window Outside")
    public static void closeTheMainWindowOutside() {
        Set<String> windowHandles = Driver.getDriver().getWindowHandles();
        String activeWindowHandle = Driver.getDriver().getWindowHandle();
        for (String windowHandle : windowHandles) {
            if (!windowHandle.equals(activeWindowHandle)) {
                Driver.getDriver().switchTo().window(windowHandle);
                Driver.getDriver().close();
            }
        }
        Driver.getDriver().switchTo().window(activeWindowHandle);
    }

    @Step("Close all tabs except the first tab and switch back to the main tab")
    public static void closeAllTabsExceptMain() {

        // Get all open tabs
        List<String> allTabs = new ArrayList<>(Driver.getDriver().getWindowHandles());

        // First tab = main tab (index 0)
        String mainTab = allTabs.get(0);

        // Close ALL other tabs (starting from index 1)
        for (int i = 1; i < allTabs.size(); i++) {
            Driver.getDriver().switchTo().window(allTabs.get(i));
            Driver.getDriver().close();
        }

        // Switch back to the main tab
        Driver.getDriver().switchTo().window(mainTab);
    }


    /**
     * Driver tipinin mobile olup olmadigini kontrol edecek
     *
     * @return eğer mobile ise true, değilse false dönecek,
     */
    public boolean isMobile() {
        return Driver.getDriverType().equals("iPhone X");
    }

    /*=======================================================================================*/

    //*=======================================================================================*//*

    @Step("Broken Link Control")
    public void brokenLinkControl() {
        HttpURLConnection huc;
        List<WebElement> links = Driver.getDriver().findElements(By.tagName("a"));

        for (WebElement link : links) {
            String url = link.getDomAttribute("href");

            if (url == null || url.isEmpty()) {
                Log.pass("URL tag is not configured or is empty.");
                continue;
            }

            try {
                if (!url.startsWith("http") && !url.startsWith("https")) {
                    url = Configs.getConfigs().baseUrl() + url;

                }
                huc = (HttpURLConnection) (new URL(url).openConnection());
                huc.setRequestMethod("HEAD");
                huc.connect();

                int respCode = huc.getResponseCode();

                if (respCode >= 201) {
                    Log.warning(url + " is a BROKEN link... ResponseCode-> " + respCode);
                } else {
                    Log.pass(url + " is a valid link.... ResponseCode-> " + respCode);
                }

            } catch (IOException e) {
                Log.fail("Error while checking the link: " + url + " -> " + e.getMessage());
            }
        }
    }


    /*=======================================================================================*/


    public void sendKeysCharacters(WebElement inputElement, String text) {
        for (int i = 0; i < text.length(); i++) {
            String character = String.valueOf(text.charAt(i));
            waitMs(200);
            inputElement.sendKeys(character);
        }
    }

    /*=======================================================================================*/


    /**
     * @param element : Search results da example: monster girdiğimizde baska bir brand gelip gelmediğini bulmak için kullanılacaktı.
     */
    public void itemsTextCollector(List<WebElement> element) {
        List<String> collector = new ArrayList<>();
        for (WebElement webElement : element) {
            collector.add(getTextElement(webElement));
        }
        Log.pass("Collect item : " + collector);
    }

    /*=======================================================================================*/

    public void itemsSizeCollector(List<WebElement> element) {
        List<String> collector = new ArrayList<>();
        for (WebElement webElement : element) {
            collector.add(checkElementSize(webElement));
        }
        Log.pass("Collect item size : " + collector);
    }

    /*=======================================================================================*/

}