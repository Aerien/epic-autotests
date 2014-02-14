package com.epicsell.pages.admin;

import com.epicsell.beans.Delivery;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: Jane T.
 * Date: 07.05.13
 */
public class DeliveryTypes extends AdminBasePage {
    public static final By add_delivery_method_button = By.xpath("//a[@id='yw0']");
    public static final By delivery_method_form = By.xpath("//form[@id='StoreDeliveryMethod']");
    public static final By delivery_method_save_button = By.xpath("//button[@id='submitStoreDeliveryMethod']");
    public static final By delivery_name_input = By.xpath("//input[@id='StoreDeliveryMethod_name']");
    public static final By delivery_price_input = By.xpath("//input[@id='StoreDeliveryMethod_price']");
    public static final By delivery_free_price_input = By.xpath("//input[@id='StoreDeliveryMethod_free_from']");
    public static final By delivery_description_input = By.xpath("//form[@id='StoreDeliveryMethod']//div[contains(@class,'editor')]");
    public static final String delivery_line = "//div[@id='deliveryMethodsListGrid']//tr[td]";
    public static final By delete = By.xpath(".//a[@class='delete']");

    public DeliveryTypes(WebDriver webDriver) {
        super(webDriver);
    }

    public DeliveryTypes addDeliveryMethod(Delivery delivery) {
        waiter().until(ExpectedConditions.presenceOfElementLocated(add_delivery_method_button)).click();
        waiter().until(ExpectedConditions.visibilityOf(
                waiter().until(ExpectedConditions.presenceOfElementLocated(delivery_method_form))));
        fillDeliveryMethodForm(delivery);
        findElement(delivery_method_save_button).click();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            //
        }
        return this;
    }

    public DeliveryTypes fillDeliveryMethodForm(Delivery delivery) {
        String name = delivery.getName();
        Boolean active = delivery.getActivity();
        BigDecimal price = delivery.getPrice();
        BigDecimal free_price = delivery.getFreePrice();
        String description = delivery.getDescription();
        List<String> payments = delivery.getPayments();

        if (name != null) {
            WebElement input = findElement(delivery_name_input);
            input.sendKeys(Keys.CONTROL, "a");
            input.sendKeys(Keys.BACK_SPACE);
            input.sendKeys(name);
        }
        String act_locator = "//div[@id='s2id_StoreDeliveryMethod_active']//a";
        WebElement activity = findElement(By.xpath(act_locator));
        activity.click();
        WebElement activity_input = waiter().until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath("//div[contains(@class,'with-searchbox')]//input")));
        if (active) {
            activity_input.sendKeys("Активен");
        } else {
            activity_input.sendKeys("Не активен");
        }
        activity_input.sendKeys(Keys.RETURN);
        if (price != null) {
            WebElement input = findElement(delivery_price_input);
            input.sendKeys(Keys.CONTROL, "a");
            input.sendKeys(Keys.BACK_SPACE);
            input.sendKeys(price.toString());
        }
        if (free_price != null) {
            WebElement input = findElement(delivery_free_price_input);
            input.sendKeys(Keys.CONTROL, "a");
            input.sendKeys(Keys.BACK_SPACE);
            input.sendKeys(free_price.setScale(2, RoundingMode.UNNECESSARY).toString());
        }
        if (description != null) {
            WebElement input = findElement(delivery_description_input);
            input.sendKeys(Keys.CONTROL, "a");
            input.sendKeys(Keys.BACK_SPACE);
            input.sendKeys(description);
        }
        if (payments != null) {
            for (String payment : payments) {
                WebElement checkbox = findElement(By.xpath("//input[contains(@id,'StoreDeliveryMethod_payment_methods')]"));
                if ((!checkbox.isSelected() &&
                        checkbox.findElements(By.xpath(".//ancestor::label[label[contains(text(),'" + payment + "')]]")).size() > 0) ||
                        (checkbox.isSelected())) {
                    checkbox.click();
                }
            }
        }
        return this;
    }

    public DeliveryTypes deleteDeliveryMethod(Delivery delivery) {
        openLastPage();
        WebElement deliveryElement = findElement(delivery_line +
                "[descendant::a[contains(text(),'" + delivery.getName() + "')]]");
        deliveryElement.findElement(delete).click();
        waitForAlert(1).accept();
        waiter().until(ExpectedConditions.stalenessOf(deliveryElement));
        return this;
    }
}
