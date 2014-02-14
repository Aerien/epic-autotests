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
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Jane T.
 * Date: 06.04.13
 */
@Category(TestCategory.Ignored.class)
@RunWith(value = Parameterized.class)
public class UploadingWithEmptyInfo extends TestCore {
    private static final Logger logger = Logger.getLogger(ShopCreation.class);
    private static ExtendedWebDriver DRIVER;
    private static String filePath = new File("").getAbsolutePath() + File.separator + "files" + File.separator
            + "productUpload" + System.currentTimeMillis() + ".csv";
    private Product.Header header;

    @Before
    public void setUrl() {
        TestCore.setDriver();
        DRIVER = getWebDriver();
        new SiteLogin(DRIVER).login(test_shop);
    }

    public UploadingWithEmptyInfo(Product.Header header) {
        this.header = header;
    }

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][]{
                {Product.Header.NAME},
                {Product.Header.CATEGORY},
                {Product.Header.PRICE},
                {Product.Header.URL},
                {Product.Header.ACTIVITY},
                {Product.Header.IMAGE},
                {Product.Header.QTY},
                {Product.Header.SHORT_DESCRIPTION},
                {Product.Header.FULL_DESCRIPTION},
                {Product.Header.META_TITLE},
                {Product.Header.META_KEYWORDS},
                {Product.Header.META_DESCRIPTION},
        };
        return Arrays.asList(data);
    }

    @Test
    public void uploadingWithEmptyFields() throws Exception {
        Integer prCount = 5;
        List<Product.Header> headers = new ArrayList<Product.Header>();
        headers.add(header);
        new GenerateFileForUploadingProducts
                (Product.getProductsWithEmptyInfo(prCount, headers), filePath);
        Assert.assertEquals("Создано продуктов: " + prCount + "\nОбновлено продуктов: 0",
                new Settings(DRIVER).uploadProductsFile(test_shop, filePath).waiter()
                        .until(ExpectedConditions.presenceOfElementLocated(Settings.upload_file_success_message)).getText());
        List<WebElement> messages = DRIVER.findElements(Settings.upload_file_error_message);
        Integer i = 1;
        for (WebElement message : messages) {
            if (headers.contains(Product.Header.NAME) ||
                    headers.contains(Product.Header.PRICE)) {
                Assert.assertEquals("Ошибки импорта: Строка: " + i + ". " + header.getMessage(), message.getText());
            }
            i++;
        }
        if (headers.contains(Product.Header.NAME) ||
                headers.contains(Product.Header.PRICE)) {
            Assert.assertEquals(String.valueOf(i - 1), String.valueOf(prCount));
        } else {
            Assert.assertEquals(String.valueOf(i - 1), String.valueOf(0));
        }
        logger.info("Тест загрузки файла без имен товаров пройден успешно");
    }

    @After
    public void delete() {
        File file = new File(filePath);
        if (file.exists()) {
            if (!file.delete()) {
                logger.error("can not delete file " + filePath);
            }
        }
    }
}
