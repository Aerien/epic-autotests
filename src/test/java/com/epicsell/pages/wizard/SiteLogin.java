package com.epicsell.pages.wizard;

import com.epicsell.beans.Shop;
import com.epicsell.drivers.ExtendedWebDriver;
import com.epicsell.links.AdminLinks;
import com.epicsell.pages.admin.Orders;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Created with IntelliJ IDEA.
 * User: Jane T.
 * Date: 07.05.13
 */
public class SiteLogin extends ExtendedWebDriver {
    public static final By email_input = By.xpath("//input[@id='LoginForm_username']");
    public static final By password_input = By.xpath("//input[@id='LoginForm_password']");
    public static final By submit_button = By.xpath("//button[@id='yw0']");
    public static final By field_error = By.id("LoginForm_password_em_");
    public static final By error = By.xpath("//div[contains(@class,'control-group')][contains(@class,'error')]");
    public static final By forgot_password_link = By.xpath("//form[@id='LoginForm']//a");

    public SiteLogin(WebDriver webDriver) {
        super(webDriver);
    }

    public Orders login(Shop shop) {
        return login(shop.getEmail(), shop.getPassword(), shop);
    }

    public Orders login(String email, String password, Shop shop) {
        if (!getCurrentUrl().endsWith(AdminLinks.ADMIN_AUTH.$())) {
            navigate().to(shop.getShopUrl() + AdminLinks.ADMIN_AUTH.$());
        }
        if (email != null) {
            WebElement emailInput = findElement(email_input);
            emailInput.sendKeys(Keys.CONTROL, "a");
            emailInput.sendKeys(Keys.BACK_SPACE);
            emailInput.sendKeys(email);
        }
        if (password != null) {
            WebElement passwordInput = findElement(password_input);
            passwordInput.sendKeys(Keys.CONTROL, "a");
            passwordInput.sendKeys(Keys.BACK_SPACE);
            passwordInput.sendKeys(password);
        }
        findElement(submit_button).click();
        loader();
        return new Orders(this);
    }

    public Orders login(String email, String password) {
        if (email != null) {
            WebElement emailInput = findElement(email_input);
            emailInput.sendKeys(Keys.CONTROL, "a");
            emailInput.sendKeys(Keys.BACK_SPACE);
            emailInput.sendKeys(email);
        }
        if (password != null) {
            WebElement passwordInput = findElement(password_input);
            passwordInput.sendKeys(Keys.CONTROL, "a");
            passwordInput.sendKeys(Keys.BACK_SPACE);
            passwordInput.sendKeys(password);
        }
        findElement(submit_button).click();
        loader();
        return new Orders(this);
    }


    public RemindPassword forgotPassword() {
        findElement(forgot_password_link).click();
        return new RemindPassword(this);
    }

    public RemindPassword forgotPassword(Shop shop) {
        navigate().to(shop.getShopUrl() + AdminLinks.ADMIN_AUTH.$());
        findElement(forgot_password_link).click();
        return new RemindPassword(this);
    }
}
