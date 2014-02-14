package com.epicsell.pages.wizard;

import com.epicsell.beans.Shop;
import com.epicsell.drivers.ExtendedWebDriver;
import com.epicsell.pages.site.SiteMainPage;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: Jane T.
 * Date: 08.05.13
 */
public class MainLogin extends ExtendedWebDriver {
    public static final By shop_name_input = By.xpath("//input[@id='loginForm_alias']");
    public static final By email_input = By.xpath("//input[@id='loginForm_email']");
    public static final By password_input = By.xpath("//input[@id='loginForm_password']");
    public static final By errors = By.xpath("//span[contains(@class,'error')]");
    public static final By errors_fields = By.xpath("//node()[contains(@class,'error')]");
    public static final By submit = By.xpath("//button[@id='sugnup-submit']");
    public static final By alias = By.id("loginForm_alias");

    public MainLogin(WebDriver webDriver) {
        super(webDriver);
    }

    public MainLogin fillAuthorizationForm(Shop shop) {
        WebElement input;
        /*= findElement(shop_name_input);
        if (shop.getShopName() != null) {
            input.sendKeys(Keys.CONTROL, "a");
            input.sendKeys(Keys.BACK_SPACE);
            input.sendKeys(shop.getShopName());
        }*/
        input = waiter().until(ExpectedConditions.visibilityOfElementLocated(email_input));
        if (shop.getEmail() != null) {
            input.sendKeys(Keys.CONTROL, "a");
            input.sendKeys(Keys.BACK_SPACE);
            input.sendKeys(shop.getEmail());
        }
        input = findElement(password_input);
        if (shop.getPassword() != null) {
            input.sendKeys(Keys.CONTROL, "a");
            input.sendKeys(Keys.BACK_SPACE);
            input.sendKeys(shop.getPassword());
        }
        return this;
    }

    public ExtendedWebDriver login(Shop shop) {
        fillAuthorizationForm(shop);
        findElement(submit).click();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            //
        }
        if (isElementPresent(alias)) {
            new Select(findElement(alias)).selectByVisibleText(shop.getShopShortUrl());
            WebElement input = findElement(password_input);
            if (shop.getPassword() != null) {
                input.sendKeys(Keys.CONTROL, "a");
                input.sendKeys(Keys.BACK_SPACE);
                input.sendKeys(shop.getPassword());
            }
            findElement(submit).click();
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                //
            }
        }
        if (isElementPresent(errors) || isElementPresent(errors_fields)) {
            return this;
        } else {
            return new SiteMainPage(this);
        }
    }

    public MainRemindPassword forgotPassword() {
        waiter().until(ExpectedConditions.visibilityOfElementLocated(By.id("remind"))).click();
        return new MainRemindPassword(this);
    }
}
