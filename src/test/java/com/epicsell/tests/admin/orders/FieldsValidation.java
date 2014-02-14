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
import org.openqa.selenium.support.Color;

import java.text.ParseException;

/**
 * Created with IntelliJ IDEA.
 * User: Jane T.
 * Date: 10.05.13
 */
@Category(TestCategory.Ignored.class)
public class FieldsValidation extends TestCore {
    Orders orders;
    Order order = Order.getValidOrder();

    @Before
    public void openOrdersPage() {
        orders = (Orders) new SiteLogin(getWebDriver())
                .login(test_shop2)
                .openMenuItem(AdminBasePage.MenuItem.ORDERS);
    }

    @Test
    public void emptyName() throws ParseException {
        order.setClientName("");
        orders.createOrder(order);
        Assert.assertEquals(Color.fromString("#B94A48").asRgba(),
                orders.findElement(Orders.user_name_input).getCssValue("color"));
    }

    @Test
    public void emptyEmail() throws ParseException {
        order.setEmail("");
        orders.createOrder(order);
        Assert.assertEquals(Color.fromString("#B94A48").asRgba(),
                orders.findElement(Orders.user_email_input).getCssValue("color"));
    }

    @Test
    public void emptyPhone() throws ParseException {
        order.setPhone("");
        orders.createOrder(order);
        Assert.assertEquals(Color.fromString("#B94A48").asRgba(),
                orders.findElement(Orders.user_phone_input).getCssValue("color"));
    }

    @Test
    public void invalidEmail() throws ParseException {
        order.setEmail("test");
        orders.createOrder(order);
        Assert.assertEquals(Color.fromString("#B94A48").asRgba(),
                orders.findElement(Orders.user_email_input).getCssValue("color"));
    }
}
