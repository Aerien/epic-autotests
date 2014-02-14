package com.epicsell.pages.wizard;

import com.epicsell.beans.Shop;
import com.epicsell.drivers.ExtendedWebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Created with IntelliJ IDEA.
 * User: Jane T.
 * Date: 07.05.13
 */
public class MainPage extends ExtendedWebDriver {
    private By email_input = By.id("signupForm_email");
    private By password_input = By.id("signupForm_password");
    private By create_shop_button = By.id("sugnup-submit");
    private By accept_offer_chbx = By.name("signupForm[offer]");

    private By alias_input = By.id("signupForm_alias");
    private By login = By.xpath("//header//a[contains(@class,'btn')]");

    public MainPage(WebDriver webDriver) {
        super(webDriver);
    }

    public SuccessShopRegistration createShop(Shop shop) {
        return createShop(shop, true);
    }

    public SuccessShopRegistration createShop(Shop shop, boolean acceptOffer) {
        fillShopRegistrationForm(shop, acceptOffer);
        findElement(create_shop_button).click();
        return new SuccessShopRegistration(this);
    }

    public MainPage fillShopRegistrationForm(Shop shop) {
        return fillShopRegistrationForm(shop, true);
    }

    public MainPage fillShopRegistrationForm(Shop shop, boolean acceptOffer) {
        waiter().until(ExpectedConditions.visibilityOfElementLocated(create_shop_button)).click();
        if (shop.getEmail() != null) {
            WebElement email = waiter().until(ExpectedConditions.visibilityOfElementLocated(email_input));
            email.sendKeys(shop.getEmail());
        }
        if (shop.getPassword() != null) {
            WebElement password = webDriver.findElement(password_input);
            password.sendKeys(shop.getPassword());
        }
        WebElement offerChbx = webDriver.findElement(accept_offer_chbx);
        if (acceptOffer ^ offerChbx.isSelected()) {
            offerChbx.click();
        }
        return this;
    }

    public MainLogin clickLoginButton() {
        findElement(login).click();
        return new MainLogin(this);
    }
}
