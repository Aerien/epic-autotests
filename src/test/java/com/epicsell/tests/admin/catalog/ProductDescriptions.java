package com.epicsell.tests.admin.catalog;

import com.epicsell.TestCore;
import com.epicsell.beans.Product;
import com.epicsell.beans.TestCategory;
import com.epicsell.drivers.ExtendedWebDriver;
import com.epicsell.pages.admin.Catalog;
import com.epicsell.pages.wizard.SiteLogin;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.math.BigDecimal;

/**
 * User: Aerien
 * Date: 08.04.13
 */
@Category(TestCategory.Ignored.class)
public class ProductDescriptions extends TestCore {
    private static final Logger logger = Logger.getLogger(ProductDescriptions.class);
    private static ExtendedWebDriver DRIVER;

    @Before
    public void setUrl() {
        DRIVER = getWebDriver();
        new SiteLogin(DRIVER).login(test_shop);
    }

    @Test
    public void createProductWithShortDescription() {
        final String expectedName = "Продукт с s_descr " + System.currentTimeMillis();
        final String expectedPrice = "101.00";
        final String expectedShortDescription = "short description";
        Product product = new Product();
        product.setName(expectedName);
        product.setPrice(new BigDecimal(expectedPrice));
        product.setActivity(true);
        product.setShortDescription(expectedShortDescription);

        new Catalog(DRIVER).createSimpleProduct(product).waitForPopup();
        DRIVER.findElement(By.linkText(expectedName)).click();
        DRIVER.waiter().until(ExpectedConditions.visibilityOfElementLocated(Catalog.product_edit_form));

        Assert.assertEquals(expectedShortDescription,
                DRIVER.findElement(Catalog.product_short_description_input).getText());

        logger.info("Тест создания товара с коротким описанием пройден успешно");
    }

    @Test
    public void createProductWithFullDescription() {
        final String expectedName = "Продукт с f_descr " + System.currentTimeMillis();
        final String expectedPrice = "101.00";
        final String expectedFullDescription = "full description";
        Product product = new Product();
        product.setName(expectedName);
        product.setPrice(new BigDecimal(expectedPrice));
        product.setFullDescription(expectedFullDescription);

        new Catalog(DRIVER).createSimpleProduct(product).waitForPopup();
        DRIVER.findElement(By.linkText(expectedName)).click();
        DRIVER.waiter().until(ExpectedConditions.visibilityOfElementLocated(Catalog.product_edit_form));

        Assert.assertEquals(expectedFullDescription,
                DRIVER.findElement(Catalog.product_full_description_input).getText());

        logger.info("Тест создания товара с полным описанием пройден успешно");
    }

    @Test
    public void editShortDescription() {
        final String expectedName = "Продукт с s_descr " + System.currentTimeMillis();
        final String expectedPrice = "101.00";
        final String baseShortDescription = "short description";
        final String expectedShortDescription = "new short description";
        Product product = new Product();
        product.setName(expectedName);
        product.setPrice(new BigDecimal(expectedPrice));
        product.setShortDescription(baseShortDescription);

        new Catalog(DRIVER).createSimpleProduct(product);

        product.setShortDescription(expectedShortDescription);
        new Catalog(DRIVER).editSimpleProduct(expectedName, product).waitForPopup();

        DRIVER.findElement(By.linkText(expectedName)).click();
        DRIVER.waiter().until(ExpectedConditions.visibilityOfElementLocated(Catalog.product_edit_form));

        Assert.assertEquals(expectedShortDescription,
                DRIVER.findElement(Catalog.product_short_description_input).getText());

        logger.info("Тест редактирования короткого описания товара пройден успешно");
    }

    @Test
    public void editFullDescription() {
        final String expectedName = "Продукт с f_descr " + System.currentTimeMillis();
        final String expectedPrice = "101.00";
        final String baseFullDescription = "full description";
        final String expectedFullDescription = "new full description";
        Product product = new Product();
        product.setName(expectedName);
        product.setPrice(new BigDecimal(expectedPrice));
        product.setFullDescription(baseFullDescription);

        new Catalog(DRIVER).createSimpleProduct(product);

        product.setFullDescription(expectedFullDescription);
        new Catalog(DRIVER).editSimpleProduct(expectedName, product).waitForPopup();

        DRIVER.findElement(By.linkText(expectedName)).click();
        DRIVER.waiter().until(ExpectedConditions.visibilityOfElementLocated(Catalog.product_edit_form));

        Assert.assertEquals(expectedFullDescription,
                DRIVER.findElement(Catalog.product_full_description_input).getText());

        logger.info("Тест редактирования полного описания товара пройден успешно");
    }
}