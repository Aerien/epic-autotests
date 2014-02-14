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
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.math.BigDecimal;

/**
 * User: Aerien
 * Date: 08.04.13
 */
@Category(TestCategory.Ignored.class)
public class ProductSku extends TestCore {
    private static final Logger logger = Logger.getLogger(ProductSku.class);
    private static ExtendedWebDriver DRIVER;

    @Before
    public void setUrl() {
        DRIVER = getWebDriver();
        new SiteLogin(DRIVER).login(test_shop);
    }

    @Test
    public void createProductWithSimpleArticle() {
        final String expectedName = "Продукт с sku " + System.currentTimeMillis();
        final String expectedPrice = "101.00";
        final String expectedSku = "sku" + System.currentTimeMillis();
        Product product = new Product();
        product.setName(expectedName);
        product.setPrice(new BigDecimal(expectedPrice));
        product.setArticle(expectedSku);

        new Catalog(DRIVER).createSimpleProduct(product);
        WebElement savedProd = DRIVER.findElement(By.linkText(expectedName));
        By parent = By.xpath("./ancestor::tr");
        Assert.assertEquals(expectedSku, savedProd.findElement(parent).findElement(Catalog.sku_col).getAttribute("textContent"));
        savedProd.click();
        DRIVER.waiter().until(ExpectedConditions.visibilityOfElementLocated(Catalog.product_edit_form));

        Assert.assertEquals(expectedSku,
                DRIVER.findElement(Catalog.product_article_input).getAttribute("value"));
        logger.info("Тест создания товара с sku пройден успешно");
    }

    @Test
    public void createProductWithDuplicatedArticle() {
        final String expectedName = "Prod " + System.currentTimeMillis() / 2;
        final String expectedName2 = "Prod " + System.currentTimeMillis() / 3;
        final String expectedSku = "scu" + System.currentTimeMillis();
        final String expectedPrice = "100.00";
        final String expectedPrice2 = "200.00";
        Product product = new Product();
        Product product2 = new Product();
        product.setName(expectedName);
        product2.setName(expectedName2);
        product.setPrice(new BigDecimal(expectedPrice));
        product2.setPrice(new BigDecimal(expectedPrice2));
        product.setArticle(expectedSku);
        product2.setArticle(expectedSku);

        new Catalog(DRIVER).createSimpleProduct(product)
                .createSimpleProduct(product2);
        Assert.assertEquals(2, new Catalog(DRIVER).findElements(By.xpath("//td[@class='-sku'][text()='" + expectedSku + "']")).size());

        logger.info("Тест создания товара с дублирующимся sku пройден успешно");
    }

    @Test
    public void editArticle() {
        final String expectedName = "edit sku " + System.currentTimeMillis();
        final String expectedPrice = "101.00";
        final String baseSku = "sku";
        final String expectedSku = "new sku";
        Product product = new Product();
        product.setName(expectedName);
        product.setPrice(new BigDecimal(expectedPrice));
        product.setArticle(baseSku);

        new Catalog(DRIVER).createSimpleProduct(product);

        product.setArticle(expectedSku);
        new Catalog(DRIVER).editSimpleProduct(expectedName, product);

        DRIVER.findElement(By.linkText(expectedName)).click();
        DRIVER.waiter().until(ExpectedConditions.visibilityOfElementLocated(Catalog.product_edit_form));

        Assert.assertEquals(expectedSku,
                DRIVER.findElement(Catalog.product_article_input).getAttribute("value"));

        logger.info("Тест редактирования sku товара пройден успешно");
    }
}