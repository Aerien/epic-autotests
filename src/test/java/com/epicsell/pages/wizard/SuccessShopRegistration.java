package com.epicsell.pages.wizard;

import com.epicsell.drivers.ExtendedWebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Created with IntelliJ IDEA.
 * User: Jane T.
 * Date: 14.05.13
 */
public class SuccessShopRegistration extends ExtendedWebDriver {
    private static final By enter = By.xpath("//button[@data-lastquestion]");

    public SuccessShopRegistration(WebDriver webDriver) {
        super(webDriver);
    }

    public ActivationBlank enterShop() {
        waiter().until(ExpectedConditions.visibilityOf(
                waiter().until(ExpectedConditions.presenceOfElementLocated(enter)))).click();
        return new ActivationBlank(this);
    }
}
