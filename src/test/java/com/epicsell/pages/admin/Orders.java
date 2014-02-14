package com.epicsell.pages.admin;

import com.epicsell.beans.Order;
import com.epicsell.beans.Product;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Jane T.
 * Date: 07.05.13
 */
public class Orders extends AdminBasePage {
    public static final By create_order_button = By.xpath("//a[@id='yw0']");
    public static final By save_order_button = By.xpath("//button[@id='yw3']");
    public static final By order_form = By.xpath("//form[@id='Order']");
    public static final String order_line = "//div[@id='OrderView']//tr[td]";
    public static final By user_name_input = By.xpath("//input[@id='Order_user_name']");
    public static final By user_email_input = By.xpath("//input[@id='Order_user_email']");
    public static final By user_phone_input = By.xpath("//input[@id='Order_user_phone']");
    public static final By user_comment_input = By.xpath("//textarea[@id='Order_user_comment']");
    public static final By user_address_input = By.xpath("//textarea[@id='Order_user_address']");
    public static final By payed_checkbox = By.xpath("//input[@id='Order_paid']");
    //-------------------------------------------------------------------------------
    public static final By select_delivery = By.xpath("//div[@id='s2id_Order_delivery_id']");
    public static final By select_status = By.xpath("//div[@id='s2id_Order_status_id']");
    public static final By select_product = By.xpath("//div[@id='s2id_chooseOrderedProducts']");
    public static final By dropped_input = By.xpath("//div[contains(@class,'drop-active')]//input");
    //--------------------------------------------------------------------------------
    public static final By client_name = By.xpath(".//td[3]");
    public static final By payment_status = By.xpath(".//td[5]");
    public static final By order_date = By.xpath(".//td[1]");
    public static final By order_number = By.xpath(".//td[2]//a");
    public static final By order_delete = By.xpath(".//a[@class='delete']");
    public static final By order_print = By.xpath(".//td[last()]/a[2]");
    //----------------------------------------------------------------------------------
    public static final By product_line = By.xpath("//div[@id='orderedProducts']//tr");


    public Orders(WebDriver webDriver) {
        super(webDriver);
    }

    public Boolean isOrdersPage() {
        return waiter().until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                return webDriver.getCurrentUrl().contains("orders");
            }
        });
    }

    public Orders createOrder(Order order) {
        waiter().until(ExpectedConditions.presenceOfElementLocated(create_order_button)).click();
        waiter().until(ExpectedConditions.visibilityOfElementLocated(order_form));
        fillOrderForm(order);
        findElement(save_order_button).click();
        return this;
    }

    public Orders fillOrderForm(Order order) {
        if (order.getClientName() != null) {
            WebElement input = findElement(user_name_input);
            input.sendKeys(Keys.CONTROL, "a");
            input.sendKeys(Keys.BACK_SPACE);
            input.sendKeys(order.getClientName());
        }
        if (order.getEmail() != null) {
            WebElement input = findElement(user_email_input);
            input.sendKeys(Keys.CONTROL, "a");
            input.sendKeys(Keys.BACK_SPACE);
            input.sendKeys(order.getEmail());
        }
        if (order.getAddress() != null) {
            WebElement input = findElement(user_phone_input);
            input.sendKeys(Keys.CONTROL, "a");
            input.sendKeys(Keys.BACK_SPACE);
            input.sendKeys(order.getPhone());
        }
        if (order.getComment() != null) {
            WebElement input = findElement(user_comment_input);
            input.sendKeys(Keys.CONTROL, "a");
            input.sendKeys(Keys.BACK_SPACE);
            input.sendKeys(order.getComment());
        }
        if (order.getAddress() != null) {
            WebElement input = findElement(user_address_input);
            input.sendKeys(Keys.CONTROL, "a");
            input.sendKeys(Keys.BACK_SPACE);
            input.sendKeys(order.getAddress());
        }
        if (order.getPayed() != null) {
            WebElement input = findElement(payed_checkbox);
            if (order.getPayed() != input.isSelected()) {
                input.click();
            }
        }
        if (order.getDelivery() != null) {
            WebElement delivery = findElement(select_delivery);
            delivery.findElement(By.xpath(".//a")).click();
            WebElement input = waiter().until(ExpectedConditions.presenceOfElementLocated(dropped_input));
            input.sendKeys(order.getDelivery().getName());
            input.sendKeys(Keys.RETURN);
        }
        if (order.getStatus() != null) {
            WebElement status = findElement(select_status);
            status.findElement(By.xpath(".//a")).click();
            WebElement input = waiter().until(ExpectedConditions.presenceOfElementLocated(dropped_input));
            input.sendKeys(order.getStatus().getStatus());
            input.sendKeys(Keys.RETURN);
        }
        if (order.getProducts() != null) {
            for (Product product : order.getProducts()) {
                WebElement product_input = findElement(select_product);
                product_input.findElement(By.xpath(".//a")).click();
                final Integer count = findElements(product_line).size();
                WebElement input = waiter().until(ExpectedConditions.presenceOfElementLocated(dropped_input));
                input.sendKeys(product.getName());
                input.sendKeys(Keys.RETURN);
                try {
                    waiter().until(new ExpectedCondition<Boolean>() {
                        @Override
                        public Boolean apply(org.openqa.selenium.WebDriver driver) {
                            return findElements(product_line).size() > count;
                        }
                    });
                } catch (TimeoutException te) {
                    //
                }
            }
        }
        return this;
    }


    public List<Product> getOrderedProducts() {
        List<Product> products = new ArrayList<Product>();
        for (WebElement product : findElements(product_line)) {
            Product tmp_product = new Product();
            String src = product.findElement(By.xpath(".//img")).getAttribute("src");
            src = src.substring(src.lastIndexOf("/"), src.length());
            tmp_product.setImage(src);
            tmp_product.setName(product.findElement(By.xpath("./td[@class='-name']/a")).getText());
//            price = "Цена: 470.00 руб.";
            String price = product.findElement(By.xpath("./td[@class='-name']")).getText();
            price = price.substring(price.lastIndexOf("Цена:"), price.length());
            price = price.substring(price.indexOf(" "), price.lastIndexOf(" "));
            price = price.trim();
            tmp_product.setPrice(BigDecimal.valueOf(Double.parseDouble(price)));
            products.add(tmp_product);
        }
        return products;
    }

    public Orders waitFormInvisibility() {
        waiter().until(ExpectedConditions.invisibilityOfElementLocated(order_form));
        waiter().until(ExpectedConditions.visibilityOfElementLocated(popup)).click();
        waiter().until(ExpectedConditions.invisibilityOfElementLocated(popup));
        return this;
    }

    public WebElement getOrderLine(Order order) {
        if (order.getNumber() == null) {
            return findElement(By.xpath(Orders.order_line +
                    "[td[contains(text(),'" + order.getClientName() + "')]]"));
        } else {
            return findElement(By.xpath(Orders.order_line +
                    "[descendant::a[contains(text(),'" + order.getNumber() + "')]]"));
        }
    }

    public Orders openEditForm(Order order) {
        getOrderLine(order).findElement(order_number).click();
        waiter().until(ExpectedConditions.visibilityOfElementLocated(order_form));
        return this;
    }

    public String getDeliveryMethod() {
        return findElement(select_delivery).getText();
    }

    public Orders editOrder(Order order) {
        openEditForm(order).fillOrderForm(order);
        findElement(save_order_button).click();
        return this;
    }

    public Orders deleteOrder(Order order) {
        getOrderLine(order).findElement(order_delete).click();
        getAlert().accept();
        waiter().until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(Orders.order_line +
                "[descendant::a[contains(text(),'" + order.getNumber() + "')]]")));
        return this;
    }

    public PrintedOrder printOrder(Order order) {
        open(getOrderLine(order).findElement(order_print).getAttribute("href"));
        return new PrintedOrder(this);
    }
}
