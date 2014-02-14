package com.epicsell.tests.admin;

import com.epicsell.TestCore;
import com.epicsell.beans.TestCategory;
import com.epicsell.drivers.ExtendedWebDriver;
import com.epicsell.pages.site.SiteBasePage;
import com.epicsell.pages.wizard.SiteLogin;
import com.epicsell.tests.user_account.ShopCreation;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Created with IntelliJ IDEA.
 * User: Jane T.
 * Date: 13.04.13
 */
@Category(TestCategory.Ignored.class)
public class AdminPanel extends TestCore {
    private static final Logger logger = Logger.getLogger(ShopCreation.class);

    @Test
    public void openCloseAdminPanel() throws Exception {
        ExtendedWebDriver DRIVER = getWebDriver();
        new SiteLogin(DRIVER).login(test_shop);
        DRIVER.get(test_shop.getShopUrl());
        DRIVER.loader().waiter().until(ExpectedConditions.elementToBeClickable(SiteBasePage.toolbox_button)).click();
        DRIVER.waiter().until(ExpectedConditions.visibilityOfElementLocated(SiteBasePage.admin_pane));
        DRIVER.findElement(SiteBasePage.toolbox_button).click();
        DRIVER.waiter().until(ExpectedConditions.invisibilityOfElementLocated(SiteBasePage.admin_pane));
        logger.info("Тест отображения панели инструментов пройден успешно");
    }
}
