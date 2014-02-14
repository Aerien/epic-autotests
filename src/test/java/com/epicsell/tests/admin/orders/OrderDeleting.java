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
import org.openqa.selenium.By;

/**
 * Created with IntelliJ IDEA.
 * User: Jane T.
 * Date: 08.05.13
 */
@Category(TestCategory.Ignored.class)
public class OrderDeleting extends TestCore {
    Orders orders;
    Order order = Order.getValidOrder();

    @Before
    public void openOrdersPage() {
        orders = (Orders) new SiteLogin(getWebDriver())
                .login(test_shop2)
                .openMenuItem(AdminBasePage.MenuItem.ORDERS);
    }

    @Test
    public void deleteOrder() {
        order.setNumber(orders.createOrder(order).waitFormInvisibility()
                .getOrderLine(order).findElement(Orders.order_number).getText());
        orders.deleteOrder(order);
        Assert.assertFalse(orders.isElementPresent(By.xpath(Orders.order_line +
                "[descendant::a[contains(text(),'" + order.getNumber() + "')]]")));
    }
}
