package com.epicsell.pages.site;

import com.epicsell.beans.Delivery;
import com.epicsell.beans.Order;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: Jane T.
 * Date: 07.05.13
 */
public class BasketPage extends SiteBasePage {
    public static final By order_total_price = By.id("orderTotalPrice");
    public static final By price_without_delivery = By.xpath("//div[@id='total']/span");
    public static final By client_name_input = By.id("OrderCreateForm_name");
    public static final By client_email_input = By.id("OrderCreateForm_email");
    public static final By client_phone_input = By.id("OrderCreateForm_phone");
    public static final By client_address_input = By.id("OrderCreateForm_address");
    public static final By client_comment_input = By.id("OrderCreateForm_comment");
    public static final By finish = By.xpath("//form[@id='cart']//div[contains(@class,'step')][last()]//button");

    public BasketPage(WebDriver webDriver) {
        super(webDriver);
    }

    public BasketPage selectDeliveryType(Delivery delivery) {
        By delivery_type = By.xpath("//form[@id='cart']/div[@class='-step'][2]//label[span[contains(text(),'"
                + delivery.getName() + "')][contains(text(),'(" + delivery.getPrice().toString() +
                " руб.)')]][following-sibling::p[text()='" + delivery.getDescription() + "']]//input");
        waiter().until(ExpectedConditions.presenceOfElementLocated(delivery_type)).click();
        BigDecimal products_price = new BigDecimal(Double.parseDouble(findElement(price_without_delivery).getText()));
        BigDecimal order_price = new BigDecimal(Double.parseDouble(findElement(order_total_price).getText()));
        if (products_price.compareTo(delivery.getFreePrice()) >= 0) {
            Assert.assertEquals(products_price, order_price);
        } else {
            Assert.assertEquals(order_price, products_price.add(delivery.getPrice()));
        }
        return this;
    }

    public BasketPage fillUserInfo(Order order) {
        if (order.getClientName() != null) {
            findElement(client_name_input).sendKeys(order.getClientName());
        }
        if (order.getEmail() != null) {
            findElement(client_email_input).sendKeys(order.getEmail());
        }
        if (order.getPhone() != null) {
            findElement(client_phone_input).sendKeys(order.getPhone());
        }
        if (order.getAddress() != null) {
            findElement(client_address_input).sendKeys(order.getAddress());
        }
        if (order.getComment() != null) {
            findElement(client_comment_input).sendKeys(order.getComment());
        }
        return this;
    }

    public BasketPage finishOrder() {
        findElement(finish).click();
        return this;
    }

    public String getOrderNumber() {
        waiter().until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(org.openqa.selenium.WebDriver driver) {
                return findElement("//h1").getText().toLowerCase().contains("заказ");
            }
        });
        String number = findElement("//h1").getText();
        number = number.toLowerCase();
        number = number.substring(number.indexOf("#"), number.length());
        return number;
    }
}
