package com.epicsell.tests.admin.payments;

import com.epicsell.TestCore;
import com.epicsell.beans.TestCategory;
import com.epicsell.drivers.ExtendedWebDriver;
import com.epicsell.links.AdminLinks;
import com.epicsell.pages.wizard.SiteLogin;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * Created with IntelliJ IDEA.
 * User: Jane T.
 * Date: 13.04.13
 */
@Category(TestCategory.Ignored.class)
@Ignore("Не реализован")
public class PaymentActivation extends TestCore {
    private static ExtendedWebDriver DRIVER;

    @Before
    public void setUrl() {
        DRIVER = getWebDriver();
        new SiteLogin(DRIVER).login(test_shop);
    }

    @Test
    public static void paymentMethods() {
        if (!DRIVER.getCurrentUrl().endsWith(AdminLinks.PAYMENT_METHODS.$())) {
            DRIVER.navigate().to(test_shop.getShopUrl() + AdminLinks.PAYMENT_METHODS.$());
        }

    }
}
