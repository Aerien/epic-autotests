package com.epicsell.pages.wizard;

import com.epicsell.beans.Shop;
import com.epicsell.pages.site.SiteBasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Created with IntelliJ IDEA.
 * User: Jane T.
 * Date: 19.05.13
 */
public class SetNewPassword extends SiteBasePage {
    public static final By password_input = By.id("ChangePasswordForm_password");
    public static final By confirm_password_input = By.id("ChangePasswordForm_passwordreply");
    public static final By pass_error = By.xpath("//input[@id='ChangePasswordForm_password']/following-sibling::span[1]");
    public static final By reply_pass_error = By.xpath("//input[@id='ChangePasswordForm_passwordreply']/following-sibling::span[1]");
    public static final By submit = By.xpath("//node()[@id='yw0' or @id='yw1'][contains(@class,'btn')]");

    public SetNewPassword(WebDriver webDriver) {
        super(webDriver);
    }

    public SiteLogin setPassword(String password, String confirm_password) {
        if (password != null) {
            findElement(password_input).sendKeys(password);
        }
        if (confirm_password != null) {
            findElement(confirm_password_input).sendKeys(confirm_password);
        }
        findElement(submit).click();
        return new SiteLogin(this);
    }

    public SiteLogin setPassword(Shop shop) {
        return setPassword(shop.getPassword(), shop.getPassword());
    }

    public SiteLogin setPassword(String password) {
        return setPassword(password, password);
    }
}
