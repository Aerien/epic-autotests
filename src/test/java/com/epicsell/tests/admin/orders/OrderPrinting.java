package com.epicsell.tests.admin.orders;

import com.epicsell.TestCore;
import com.epicsell.beans.Order;
import com.epicsell.beans.Product;
import com.epicsell.beans.TestCategory;
import com.epicsell.pages.admin.AdminBasePage;
import com.epicsell.pages.admin.Orders;
import com.epicsell.pages.wizard.SiteLogin;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Jane T.
 * Date: 08.05.13
 */
@Category(TestCategory.Ignored.class)
public class OrderPrinting extends TestCore {
    Orders orders;
    Order order = Order.getValidOrder();

    @Before
    public void openOrdersPage() {
        orders = (Orders) new SiteLogin(getWebDriver())
                .login(test_shop2)
                .openMenuItem(AdminBasePage.MenuItem.ORDERS);
    }

    @Test
    public void printOrder() {
        WebElement orderLine = orders.createOrder(order).waitFormInvisibility()
                .getOrderLine(order);
        order.setNumber(orderLine.findElement(Orders.order_number).getText());
        order.setDate(orderLine.findElement(Orders.order_date).getText());
        orders.openEditingForm(order.getNumber());
        List<Product> products = orders.getOrderedProducts();
        order.setProducts(products);
        orders.printOrder(order).verifyOrderInfo(order);
    }
}
