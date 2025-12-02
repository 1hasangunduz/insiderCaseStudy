package com.insider.base;

import com.insider.configs.Configs;
import com.insider.listener.Listener;
import com.insider.utilities.ReusableMethods;
import io.qameta.allure.Step;
import org.testng.annotations.*;
import com.insider.utilities.Log;

import java.time.Duration;

import static com.insider.base.Driver.unloadDriver;


@Listeners({Listener.class})
public class BaseTest implements WaitConditions {
    private static final int DEFAULT_TIMEOUT = 40;

    @BeforeMethod(alwaysRun = true)
    public static void setupTest() {
        var browser = Configs.getConfigs().browser();
        var selectedBrowser = (browser == null || browser.isEmpty()) ? "chrome" : browser;
        Driver.setDriver(selectedBrowser);
        Log.pass("Browser initialized: " + selectedBrowser);
        Log.pass("Window size: " + Driver.getDriver().manage().window().getSize());
        Driver.getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(DEFAULT_TIMEOUT));
    }


    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        try {
            if (Driver.getDriver() != null) {
                Driver.getDriver().quit();
                Log.pass("Driver quit successfully, and session ended.");
            }
        } catch (Exception e) {
            Log.error("An error occurred during driver cleanup: " + e.getMessage());
        } finally {
            unloadDriver();
        }
    }


}
