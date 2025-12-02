package com.insider.base;


import com.insider.utilities.Log;

import io.qameta.allure.Step;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public interface WaitConditions {
    int DEFAULT_WAIT = 7;

    /********************* ELEMENT TO BE VISIBLE *********************/
    default WebElement waitClickAbleByOfElement(WebElement element) {
        try {
            return new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(DEFAULT_WAIT)).until(ExpectedConditions.elementToBeClickable(element));
        } catch (Exception e) {
            Log.warning("Web element is not clickable!");
        }
        return element;
    }

    @Step("Wait for {time} seconds")
    default WebElement waitClickAbleByOfElement(WebElement element, int time) {

        try {
            new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(time)).until(ExpectedConditions.elementToBeClickable(element));
        } catch (Exception e) {
            Log.error("Web element is not clickable!");
        }
        return element;
    }

    /********************* WAIT PAGE LOAD *********************/

    default void waitForPageToLoad() {
        waitForPageToLoad(DEFAULT_WAIT);
    }

    default void waitForPageToLoad(long timeOutInSeconds) {
        ExpectedCondition<Boolean> expectation = driver -> ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
        try {
            Log.pass("Waiting for page to load...");
            var wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(timeOutInSeconds));
            wait.until(expectation);
            Log.pass("Page loaded!");
        } catch (TimeoutException timeoutException) {
            Log.warning("Timeout waiting for Page Load Request to complete after " + timeOutInSeconds + " seconds");
        }
    }

    default void waitMs(int millisecond) {
        try {
            Thread.sleep(millisecond);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Thread was interrupted during sleep", e);
        }
    }

    default void waitForUrlContains(String keyword, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(timeoutSeconds));
        wait.until(ExpectedConditions.urlContains(keyword));
    }


}
