package com.epicsell.tests.admin.automation.import_products;

import com.epicsell.TestCore;
import com.epicsell.beans.TestCategory;
import com.epicsell.drivers.ExtendedWebDriver;
import com.epicsell.links.AdminLinks;
import com.epicsell.pages.admin.Settings;
import com.epicsell.pages.wizard.SiteLogin;
import com.epicsell.tests.user_account.ShopCreation;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.By;

/**
 * Created with IntelliJ IDEA.
 * User: Jane T.
 * Date: 05.04.13
 */
@Category(TestCategory.Ignored.class)
public class UploadingWithoutFile extends TestCore {
    private static final Logger logger = Logger.getLogger(ShopCreation.class);
    private static ExtendedWebDriver DRIVER;

    @Before
    public void setUrl() {
        DRIVER = getWebDriver();
        new SiteLogin(DRIVER).login(test_shop);
    }

    @Test
    public void uploadingWithoutFile() throws Exception {
        Assert.assertEquals(test_shop.getShopUrl() + AdminLinks.PRODUCTS_IMPORT.$(),
                new Settings(DRIVER).uploadProductsFile(test_shop, null).getCurrentUrl());
        Assert.assertEquals("Ошибки импорта: Файл недоступен.",
                DRIVER.findElement(Settings.uploading_error_message).getText());
        Assert.assertTrue(DRIVER.findElements(Settings.footer_uploading_form).size() > 0);
        Assert.assertTrue(DRIVER.findElements(Settings.footer_uploading_form).get(0)
                .findElements(By.xpath(".//input[@type='file']")).size() > 0);
        logger.info("Тест формы загрузки файлов без указания файла пройден успешно");
    }
}
