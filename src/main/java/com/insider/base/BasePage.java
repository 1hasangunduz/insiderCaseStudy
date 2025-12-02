package com.insider.base;

import com.insider.utilities.Log;
import com.insider.utilities.ReusableMethods;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import static org.testng.Assert.assertTrue;

public class BasePage extends ReusableMethods {

    /*=======================================================================================*/

    @Step("Handle Pop-up")
    public void handlePopUp() {
        try {
            WebElement popUp = Driver.getDriver().findElement(By.xpath("//div[@class='popup']"));
            WebElement outOfPopUp = Driver.getDriver().findElement(By.xpath("//div[@class='overlay']"));
            if (isDisplayElement(popUp)) {
                clickElement(outOfPopUp, "Pop-up is closed.");
            }
        } catch (Exception e) {
            Log.pass("Pop-up is not displayed.");
        }
    }

    /*=======================================================================================*/

    /**
     * This method is close modal popup on the home page if it is displayed.
     * If it is not displayed, it will print a message on the console.
     * If it is displayed, it will click on the 'X' button on the home page popup.
     */
    @Step("Close Home Page Popup")
    public void closeHomePagePopup() {
        Log.pass("Close the home page popup.");
        var homePagePopupContainer = Driver.getDriver().findElement(By.xpath("//div[@class='homepage-popup']"));
        var closeHomePagePopupButton = Driver.getDriver().findElement(By.xpath("//div[@class='modal-close']"));
        if (isDisplayElement(homePagePopupContainer)) {
            Assert.assertEquals(checkElementSize(homePagePopupContainer), "520x700", "The home page popup size is not correct.");
            Log.pass("Confirmed that the home page popup size is correct. Modal size --> " + checkElementSize(homePagePopupContainer));
            clickElement(closeHomePagePopupButton, "Clicked on the 'X' button on the home page popup.");
        } else {
            Log.pass("The home page popup is not displayed.");
        }
    }
    /*=======================================================================================*/

    /*=======================================================================================*/

}
