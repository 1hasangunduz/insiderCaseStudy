package com.insider.base;


import com.insider.configs.Configs;
import com.insider.utilities.Log;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import lombok.Getter;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;


public class Driver {

    private Driver() {
        throw new IllegalStateException("Driver class");
    }

    private static final ThreadLocal<WebDriver> DRIVER_TL = new ThreadLocal<>();
    public static final Configs config = Configs.getConfigs();

    private static final String START_FULL_SCREEN = "--start-fullscreen";
    private static final String HEADLESS = "--headless";
    private static final String INCOGNITO = "--incognito";
    private static final String WINDOW_SIZE = "--window-size=1920,1080";
    private static final String IGNORE_CERT_ERRORS = "--ignore-certificate-errors";
    private static final String ALLOW_INSECURE_LOCALHOST = "--allow-insecure-localhost";
    private static final String ACCEPT_INSECURE_CERTS = "--acceptInsecureCerts";
    private static final String DISABLE_INFOBARS = "disable-infobars";
    private static final String NO_SANDBOX = "--no-sandbox";
    private static final String DISABLE_GPU = "--disable-gpu";
    private static final String LANG_EN_US = "--lang=en_US";
    private static final String DISABLE_POPUP_BLOCKING = "--disable-popup-blocking";
    private static final String DISABLE_NOTIFICATIONS = "--disable-notifications";
    private static final String DISABLE_EXTENSIONS = "--disable-extensions";
    private static final String DISABLE_DEV_SHM_USAGE = "--disable-dev-shm-usage";
    private static final String DISABLE_CACHE = "--disable-cache";
    private static final String DISABLE_WEB_SECURITY = "--disable-web-security";
    private static final String DISABLE_SITE_ISOLATION_TRIALS = "--disable-site-isolation-trials";
    private static final String DISABLE_BROWSER_SIDE_NAVIGATION = "--disable-browser-side-navigation";

    @Getter
    private static String driverType;

    public static void setDriverType(String driverType) {
        Driver.driverType = driverType;
    }

    public static WebDriver getDriver(String browser) {
        setDriver(browser);
        return DRIVER_TL.get();
    }

    public static void setDriver(String browser) {
        WebDriver driver = switch (browser.toLowerCase()) {
            case "chrome" -> initializeChromeDriver();
            case "firefox" -> initializeFirefoxDriver();
            default -> initializeMobileDriver(browser);
        };
        DRIVER_TL.set(driver);
    }

    @Step("Initialize Chrome Driver with options")
    private static WebDriver initializeChromeDriver() {
        setDriverType("chrome");
        ChromeOptions options = createChromeOptions();
        options.addArguments(config.isHeadless() ? HEADLESS : START_FULL_SCREEN);
        return new ChromeDriver(options);
    }

    @Step("Initialize Firefox Driver with options")
    private static WebDriver initializeFirefoxDriver() {
        setDriverType("firefox");
        System.setProperty("webdriver.gecko.driver", "src/main/resources/geckodriver");
        FirefoxOptions options = createFirefoxOptions();
        options.addArguments(config.isHeadless() ? HEADLESS : START_FULL_SCREEN);
        return new FirefoxDriver(options);
    }

    @Step("Initialize Mobile Browser Driver with options")
    private static WebDriver initializeMobileDriver(String deviceName) {
        setDriverType(deviceName);
        ChromeOptions options = createChromeOptions();
        Map<String, String> mobileEmulation = new HashMap<>();
        mobileEmulation.put("deviceName", deviceName);
        options.setExperimentalOption("mobileEmulation", mobileEmulation);
        options.addArguments(config.isHeadless() ? HEADLESS : START_FULL_SCREEN);
        return new ChromeDriver(options);
    }

    @Step("Create Chrome Options")
    private static ChromeOptions createChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments(
                INCOGNITO,
                WINDOW_SIZE,
                IGNORE_CERT_ERRORS,
                ALLOW_INSECURE_LOCALHOST,
                ACCEPT_INSECURE_CERTS,
                DISABLE_INFOBARS,
                NO_SANDBOX,
                DISABLE_GPU,
                LANG_EN_US,
                DISABLE_POPUP_BLOCKING,
                DISABLE_NOTIFICATIONS,
                DISABLE_EXTENSIONS,
                DISABLE_DEV_SHM_USAGE,
                DISABLE_CACHE,
                DISABLE_WEB_SECURITY,
                DISABLE_SITE_ISOLATION_TRIALS,
                DISABLE_BROWSER_SIDE_NAVIGATION
        );
        return options;
    }

    @Step("Create Firefox Options")
    private static FirefoxOptions createFirefoxOptions() {
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments(
                "--private",
                IGNORE_CERT_ERRORS,
                ALLOW_INSECURE_LOCALHOST,
                ACCEPT_INSECURE_CERTS,
                DISABLE_INFOBARS,
                NO_SANDBOX,
                "--lang=en-US",
                DISABLE_POPUP_BLOCKING,
                DISABLE_NOTIFICATIONS,
                DISABLE_EXTENSIONS,
                DISABLE_DEV_SHM_USAGE,
                DISABLE_WEB_SECURITY,
                "--disable-features=IsolateOrigins,site-per-process",
                DISABLE_SITE_ISOLATION_TRIALS,
                DISABLE_GPU,
                "--allowed-ips",
                "--allow-running-insecure-content",
                "--allow-insecure-websocket-from-https-origin",
                "--allow-nacl-socket-api=localhost",
                "--allow-outdated-plugins",
                "--allow-sandbox-debugging",
                DISABLE_CACHE
        );
        return options;
    }

    public static WebDriver getDriver() {
        return DRIVER_TL.get();
    }


    public static void unloadDriver() {
        DRIVER_TL.remove();
    }


    public static void takeScreenshot(String name) {
        if (Driver.getDriver() == null) {
            Log.error("Driver is null. Cannot capture screenshot.");
            return;
        }

        try {
            var screenshotBytes = ((TakesScreenshot) Driver.getDriver()).getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment("Screenshot of " + name, new ByteArrayInputStream(screenshotBytes));
            Log.pass("Screenshot taken successfully for: " + name);
        } catch (TimeoutException e) {
            Log.fail("Timeout while taking screenshot: " + e.getMessage());
        } catch (Exception e) {
            Log.fail("Unexpected error while taking screenshot: " + e.getMessage());
        }
    }


}
