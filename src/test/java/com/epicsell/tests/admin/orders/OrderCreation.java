package com.epicsell.tests.admin.orders;

import com.epicsell.TestCore;
import com.epicsell.beans.Order;
import com.epicsell.beans.TestCategory;
import com.epicsell.pages.admin.AdminBasePage;
import com.epicsell.pages.admin.Orders;
import com.epicsell.pages.wizard.SiteLogin;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.WebElement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Jane T.
 * Date: 08.05.13
 */
@Category(TestCategory.Ignored.class)
public class OrderCreation extends TestCore {
    Orders orders;
    Order order = Order.getValidOrder();

    @Before
    public void openOrdersPage() {
        orders = (Orders) new SiteLogin(getWebDriver())
                .login(test_shop2)
                .openMenuItem(AdminBasePage.MenuItem.ORDERS);
    }

    @SuppressWarnings("deprecation")
    @Test
    public void createValidOrder() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        WebElement orderLine = ((Orders) orders.createOrder(order).waitForPopup()).getOrderLine(order);
        Assert.assertEquals(order.getClientName(), orderLine.findElement(Orders.client_name).getText());
        Assert.assertEquals("Не оплачен", orderLine.findElement(Orders.payment_status).getText());
        Date orderDate = format.parse(orderLine.findElement(Orders.order_date).getText());
        Date today = Calendar.getInstance().getTime();
        Assert.assertEquals(today.getYear(), orderDate.getYear());
        Assert.assertEquals(today.getMonth(), orderDate.getMonth());
        Assert.assertEquals(today.getDay(), orderDate.getDay());
    }

}
