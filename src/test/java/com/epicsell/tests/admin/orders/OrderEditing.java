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

/**
 * Created with IntelliJ IDEA.
 * User: Jane T.
 * Date: 08.05.13
 */
@Category(TestCategory.Ignored.class)
public class OrderEditing extends TestCore {
    Orders orders;
    Order order = Order.getValidOrder();

    @Before
    public void openOrdersPage() {
        orders = (Orders) new SiteLogin(getWebDriver())
                .login(test_shop2)
                .openMenuItem(AdminBasePage.MenuItem.ORDERS);
    }

    @Test
    public void editPaymentStatus() {
        WebElement orderLine = orders.createOrder(order).waitFormInvisibility().getOrderLine(order);
        Assert.assertEquals("Не оплачен", orderLine.findElement(Orders.payment_status).getText());
        order.setPayed(true);
        orderLine = orders.editOrder(order).getOrderLine(order);
        Assert.assertEquals("Оплачен", orderLine.findElement(Orders.payment_status).getText());
    }

    @Test
    public void editClientName() {
        WebElement orderLine = orders.createOrder(order).waitFormInvisibility().getOrderLine(order);
        order.setNumber(orderLine.findElement(Orders.order_number).getText());
        order.setClientName("new client name");
        orderLine = orders.editOrder(order).waitFormInvisibility().getOrderLine(order);
        Assert.assertEquals(order.getClientName(), orderLine.findElement(Orders.client_name).getText());
    }

}
