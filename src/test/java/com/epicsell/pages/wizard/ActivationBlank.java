package com.epicsell.pages.wizard;

import com.epicsell.beans.Shop;
import com.epicsell.drivers.ExtendedWebDriver;
import com.epicsell.pages.site.SiteMainPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Created with IntelliJ IDEA.
 * User: Jane T.
 * Date: 14.05.13
 */
public class ActivationBlank extends ExtendedWebDriver {
    private static final By full_name_input = By.id("lastQuestion_full_name");
    private static final By city_input = By.id("lastQuestion_city");
    private static final By country_select = By.id("s2id_lastQuestion_country");
    private static final By activity_type_select = By.id("s2id_lastQuestion_store_type");
    private static final By submit = By.id("lastquestion-submit");

    public ActivationBlank(WebDriver webDriver) {
        super(webDriver);
    }

    public ActivationBlank fillForm(Shop shop) {
        if (shop.getClientName() != null) {
            waiter().until(ExpectedConditions.presenceOfElementLocated(full_name_input)).sendKeys(shop.getClientName());
        }
        if (shop.getCity() != null) {
            findElement(city_input).sendKeys(shop.getCity());
        }
        if (shop.getCountry() != null) {
            WebElement div = findElement(country_select);
            getMouse().click(((Locatable) div.findElement(By.xpath("./a"))).getCoordinates());
            waiter().until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//ul[@class='select2-results']/li/div[contains(text(),'"
                            + shop.getCountry() + "')]"))).click();
        }
        if (shop.getActivityType() != null) {
            WebElement div = findElement(activity_type_select);
            getMouse().click(((Locatable) div.findElement(By.xpath("./a"))).getCoordinates());
            waiter().until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//ul[@class='select2-results']/li/div[contains(text(),'"
                            + shop.getActivityType() + "')]"))).click();
        }
        findElement(city_input).click();
        findElement(By.id("lastQuestion_oferta")).click();
        return this;
    }

    public SiteMainPage setBaseClientInfo(Shop shop) {
        fillForm(shop).findElement(submit).click();
        return new SiteMainPage(this);
    }
}
