package com.epicsell.tests.admin.automation.import_products;

import com.epicsell.TestCore;
import com.epicsell.beans.GenerateFileForUploadingProducts;
import com.epicsell.beans.Product;
import com.epicsell.beans.TestCategory;
import com.epicsell.drivers.ExtendedWebDriver;
import com.epicsell.pages.admin.Settings;
import com.epicsell.pages.wizard.SiteLogin;
import com.epicsell.tests.user_account.ShopCreation;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: Jane T.
 * Date: 06.04.13
 */
@Category(TestCategory.Ignored.class)
public class UploadingWithValidInfo extends TestCore {
    private static final Logger logger = Logger.getLogger(ShopCreation.class);
    private static ExtendedWebDriver DRIVER;
    private static String filePath = new File("").getAbsolutePath() + File.separator + "files" + File.separator
            + "productUpload" + System.currentTimeMillis() + ".csv";

    @Before
    public void setUrl() {
        DRIVER = getWebDriver();
        new SiteLogin(DRIVER).login(test_shop);
    }

    @Test
    public void uploadingShortFile() throws Exception {
        Integer prCount = 5;
        new GenerateFileForUploadingProducts(Product.getProducts(prCount), filePath);
        Assert.assertEquals("Создано продуктов: " + prCount + "\nОбновлено продуктов: 0",
                new Settings(DRIVER).uploadProductsFile(test_shop, filePath)
                        .findElement(Settings.upload_file_success_message).getText());
        logger.info("Тест загрузки файла товаров пройден успешно");
    }

    @Test
    public void uploadingLongFile() throws Exception {
        Integer prCount = 150;
        new GenerateFileForUploadingProducts(Product.getProducts(prCount), filePath);
        Assert.assertEquals("Создано продуктов: " + prCount + "\nОбновлено продуктов: 0",
                new Settings(DRIVER).uploadProductsFile(test_shop, filePath).waiter()
                        .until(ExpectedConditions.presenceOfElementLocated(Settings.upload_file_success_message)).getText());
        logger.info("Тест загрузки файла товаров пройден успешно");
    }

    @Test
    public void uploadingFileWithValidInfo() throws Exception {
        Integer prCount = 1;
        new GenerateFileForUploadingProducts(Product.getProducts(prCount), filePath);
        Assert.assertEquals("Создано продуктов: " + prCount + "\nОбновлено продуктов: 0",
                new Settings(DRIVER).uploadProductsFile(test_shop, filePath)
                        .findElement(Settings.upload_file_success_message).getText());
        logger.info("Тест загрузки файла товаров пройден успешно");
    }

    @After
    public void delete() {
        File file = new File(filePath);
        if (file.exists()) {
            if (!file.delete()) {
                logger.info("can not delete file " + filePath);
            }
        }
    }
}
