package com.epicsell.tests.admin.delivery;

import com.epicsell.TestCore;
import com.epicsell.beans.Delivery;
import com.epicsell.beans.Order;
import com.epicsell.beans.TestCategory;
import com.epicsell.pages.admin.*;
import com.epicsell.pages.site.BasketPage;
import com.epicsell.pages.wizard.SiteLogin;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: Jane T.
 * Date: 18.05.13
 */
@Category(TestCategory.Ignored.class)
public class DeliveryDeleting extends TestCore {
    private static Delivery delivery;
    private static Catalog catalog;

    @Before
    public void resetPageSettings() {
        catalog = (Catalog) new SiteLogin(getWebDriver()).login(test_shop).openMenuItem(AdminBasePage.MenuItem.CATALOG);
        delivery = new Delivery();
        delivery.setName("test" + System.currentTimeMillis());
        delivery.setActivity(true);
        delivery.setDescription("test" + System.currentTimeMillis() + "description");
        delivery.addPayment("Оплата наличными");
        delivery.setPrice(new BigDecimal(120));
        delivery.setFreePrice(new BigDecimal(500));
    }

    @Test
    public void deleteDeliveryType() {
        ((DeliveryTypes) ((Settings)
                catalog.openMenuItem(AdminBasePage.MenuItem.SETTINGS))
                .openSettingsItem(Settings.SettingsItem.DELIVERY_TYPES))
                .addDeliveryMethod(delivery)
                .deleteDeliveryMethod(delivery);
    }

    @Ignore("Некорректного вида ошибка при удалении способа доставки, который используется в заказах")
    @Test
    public void deleteUsedDeliveryType() {
        Order order = Order.getValidOrder();
        BasketPage basket = ((DeliveryTypes) ((Settings)
                catalog.openMenuItem(AdminBasePage.MenuItem.SETTINGS))
                .openSettingsItem(Settings.SettingsItem.DELIVERY_TYPES))
                .addDeliveryMethod(delivery)
                .logout()
                .openProduct()
                .addProductToBasket()
                .openBasket()
                .selectDeliveryType(delivery)
                .fillUserInfo(order)
                .finishOrder();
        String number = basket.getOrderNumber();
        number = number.replace("#", "№");
        order.setNumber(number);
        Assert.assertEquals(delivery.getName(),
                ((Orders) ((DeliveryTypes) ((Settings) new SiteLogin(getWebDriver())
                        .login(test_shop)
                        .openMenuItem(AdminBasePage.MenuItem.SETTINGS))
                        .openSettingsItem(Settings.SettingsItem.DELIVERY_TYPES))
                        .deleteDeliveryMethod(delivery)
                        .openMenuItem(AdminBasePage.MenuItem.ORDERS))
                        .openEditForm(order)
                        .getDeliveryMethod());
    }

}
