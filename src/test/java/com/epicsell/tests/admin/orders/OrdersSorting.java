package com.epicsell.tests.admin.orders;

import com.epicsell.TestCore;
import com.epicsell.beans.Order;
import com.epicsell.beans.TestCategory;
import com.epicsell.pages.admin.AdminBasePage;
import com.epicsell.pages.admin.Orders;
import com.epicsell.pages.wizard.SiteLogin;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * Created with IntelliJ IDEA.
 * User: Jane T.
 * Date: 10.05.13
 */
@Category(TestCategory.Ignored.class)
public class OrdersSorting extends TestCore {
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
//        List<WebElement> orderDates = orders.findElements(Orders.order_line);
//        for(WebElement )
    }
}
