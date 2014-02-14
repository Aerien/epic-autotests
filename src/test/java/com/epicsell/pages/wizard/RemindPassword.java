package com.epicsell.pages.wizard;

import com.epicsell.pages.site.SiteBasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Created with IntelliJ IDEA.
 * User: Jane T.
 * Date: 19.05.13
 */
public class RemindPassword extends SiteBasePage {
    public static final By email_input = By.id("RemindPasswordForm_email");
    public static final By submit = By.id("yw0");
    public static final By error = By.id("RemindPasswordForm_email_em_");

    public RemindPassword(WebDriver webDriver) {
        super(webDriver);
    }

    public SiteBasePage resetPassword(String email) {
        if (email != null) {
            waiter().until(ExpectedConditions.visibilityOfElementLocated(email_input)).sendKeys(email);
        }
        findElement(submit).click();
        return this;
    }
}
