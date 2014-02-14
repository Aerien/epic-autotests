package com.epicsell.pages.site;

import com.epicsell.drivers.ExtendedWebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Created with IntelliJ IDEA.
 * User: Jane T.
 * Date: 07.05.13
 */
@SuppressWarnings("unused")
public class SiteBasePage extends ExtendedWebDriver {
    public static final By create_order_button = By.xpath("//div[@id='cart-mini']/div//a");
    public static final By order_basket = By.xpath("//div[@id='cart-mini']");
    public static final By site_name = By.xpath("//div[contains(@class,'site-name')]//span");
    public static final By copyright = By.xpath("//div[contains(@class,'copyright')]");
    public static final By phone = By.xpath("//span[@class='es-phone']");
    public static final By description = By.xpath("//*[contains(@class,'es-site-description')]");
    public static final By toolbox_button = By.xpath("//div[contains(@class,'toolbox-show')]");
    public static final By admin_pane_button = By.xpath("//a[@class='tb-admin-pane']");
    public static final By admin_pane = By.xpath("//div[@class='jspContainer']");

    public SiteBasePage(WebDriver webDriver) {
        super(webDriver);
    }

    public BasketPage openBasket() {
        waiter().until(ExpectedConditions.presenceOfElementLocated(create_order_button)).click();
        return new BasketPage(this);
    }
}
