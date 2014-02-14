package com.epicsell.tests.admin.delivery;

import com.epicsell.TestCore;
import com.epicsell.beans.Delivery;
import com.epicsell.beans.TestCategory;
import com.epicsell.pages.admin.AdminBasePage;
import com.epicsell.pages.admin.Catalog;
import com.epicsell.pages.admin.DeliveryTypes;
import com.epicsell.pages.admin.Settings;
import com.epicsell.pages.wizard.SiteLogin;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: Jane T.
 * Date: 19.05.13
 */
@Category(TestCategory.Ignored.class)
public class DeliveryEditing extends TestCore {
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
    public void validDeliveryWithOnePaymentMethod() {
        ((DeliveryTypes) ((Settings)
                catalog.openMenuItem(AdminBasePage.MenuItem.SETTINGS))
                .openSettingsItem(Settings.SettingsItem.DELIVERY_TYPES))
                .addDeliveryMethod(delivery)
                .logout()
                .openProduct()
                .addProductToBasket()
                .openBasket()
                .selectDeliveryType(delivery);
    }
}
