package com.epicsell.pages.wizard;

import com.epicsell.beans.Shop;
import com.epicsell.pages.site.SiteBasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

/**
 * Created with IntelliJ IDEA.
 * User: Jane T.
 * Date: 19.05.13
 */
public class MainRemindPassword extends SiteBasePage {
    public static final By email_input = By.id("RemindPasswordForm_email");
    public static final By submit = By.id("yw0");
    public static final By alias_select = By.id("RemindPasswordForm_alias");
    public static final By error = By.xpath("//form[@id='RemindPasswordForm']//span");

    public MainRemindPassword(WebDriver webDriver) {
        super(webDriver);
    }

    public SiteBasePage resetPassword(String email) {
        if (email != null) {
            waiter().until(ExpectedConditions.visibilityOfElementLocated(email_input)).sendKeys(email);
        }
        findElement(submit).click();
        return this;
    }

    public SiteBasePage resetPassword(Shop shop) {
        if (shop.getEmail() != null) {
            waiter().until(ExpectedConditions.visibilityOfElementLocated(email_input)).sendKeys(shop.getEmail());
        }
        findElement(submit).click();
        try {
            new Select(waiter().until(ExpectedConditions.presenceOfElementLocated(alias_select)))
                    .selectByVisibleText(shop.getShopShortUrl());
            findElement(submit).click();
        } catch (TimeoutException te) {
            //
        }
        return this;
    }
}
