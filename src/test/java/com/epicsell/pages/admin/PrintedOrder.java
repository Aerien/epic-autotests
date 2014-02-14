package com.epicsell.pages.admin;

import com.epicsell.beans.Order;
import com.epicsell.beans.Product;
import com.epicsell.drivers.ExtendedWebDriver;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created with IntelliJ IDEA.
 * User: Jane T.
 * Date: 09.05.13
 */
public class PrintedOrder extends ExtendedWebDriver {
    public static final By order_number = By.xpath("//div[@id='header']//h1");
    public static final By order_date = By.xpath("//div[@id='header']//p");
    //    public static final By company_name = By.xpath("//div[@id='company']//h2");   //todo
    public static final By company_link = By.xpath("//div[@id='company']//p");
    public static final By customer_name = By.xpath("//div[@id='customer']//tr[td][1]");
    public static final By customer_phone = By.xpath("//div[@id='customer']//tr[td][2]");
    public static final By customer_email = By.xpath("//div[@id='customer']//tr[td][3]");
    public static final By customer_address = By.xpath("//div[@id='customer']//tr[td][4]");
    public static final String product_line = "//div[@id='purchases']//tr[td][not(count(td)=2)]";

    public PrintedOrder(WebDriver webDriver) {
        super(webDriver);
    }

    public PrintedOrder verifyOrderInfo(Order order) {
        Assert.assertEquals("Заказ " + order.getNumber(), findElement(order_number).getText());
        Assert.assertEquals("от " + order.getDate(), findElement(order_date).getText());
        String current_url = getCurrentUrl();
        current_url = current_url.substring(0, current_url.indexOf(".ru") + 3);
        Assert.assertEquals(current_url, findElement(company_link).getText());
        Assert.assertEquals(order.getClientName(), findElement(customer_name).getText());
        Assert.assertEquals(order.getPhone(), findElement(customer_phone).getText());
        Assert.assertEquals(order.getEmail(), findElement(customer_email).getText());
        Assert.assertEquals(order.getAddress(), findElement(customer_address).getText());
        Assert.assertEquals(order.getProducts().size(), findElements(product_line).size());
        for (Product product : order.getProducts()) {
            Integer qty = 1;  //todo qty when it will be done
            WebElement productLine = findElement(By.xpath(product_line +
                    "[descendant::span[contains(text(),'" + product.getName() + "')]]"));
            Assert.assertEquals(product.getPrice().setScale(2, RoundingMode.UNNECESSARY).toString(),
                    productLine.findElement(By.xpath(".//td[2]/span")).getText());
            Assert.assertEquals(qty.toString(), productLine.findElement(By.xpath(".//td[3]/span")).getText());
            BigDecimal result = product.getPrice().multiply(new BigDecimal(qty));
            Assert.assertEquals(result.setScale(2, RoundingMode.UNNECESSARY).toString(),
                    productLine.findElement(By.xpath(".//td[4]/span")).getText());
        }
        return this;
    }


//    public Orders backToOrders() {
//        return new Orders(this);
//    }
}
