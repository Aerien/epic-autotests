package com.epicsell.pages.site;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Created with IntelliJ IDEA.
 * User: Jane T.
 * Date: 07.05.13
 */
public class ProductPage extends SiteBasePage {
    public static final By buy_button = By.xpath("//form//button");
    public static final By product_name = By.tagName("h1");
    public static final By product_price = By.xpath("//node()[@class='-price']/span");
    public static final By product_images_list = By.xpath("//div[@class='es-gallery']//ul");

    public ProductPage(WebDriver webDriver) {
        super(webDriver);
    }

    public ProductPage addProductToBasket() {
        final String innerHtml = getInnerHtml(findElement(order_basket));
        click(waiter().until(ExpectedConditions.visibilityOf(waiter()
                .until(ExpectedConditions.presenceOfElementLocated(buy_button)))));
        waiter().until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(org.openqa.selenium.WebDriver driver) {
                return !innerHtml.equals(getInnerHtml(findElement(order_basket)));
            }
        });
        return this;
    }
}
