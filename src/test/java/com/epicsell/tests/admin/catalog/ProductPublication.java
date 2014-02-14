package com.epicsell.tests.admin.catalog;

import com.epicsell.TestCore;
import com.epicsell.beans.Product;
import com.epicsell.beans.TestCategory;
import com.epicsell.pages.admin.AdminBasePage;
import com.epicsell.pages.admin.Catalog;
import com.epicsell.pages.site.ProductPage;
import com.epicsell.pages.wizard.SiteLogin;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.By;

import java.math.BigDecimal;

/**
 * User: Aerien
 * Date: 08.04.13
 */
@Category(TestCategory.Ignored.class)
public class ProductPublication extends TestCore {
    private static final Logger logger = Logger.getLogger(ProductPublication.class);
    Catalog catalog;
    private Product product;

    @Before
    public void setUrl() {
        catalog = (Catalog) new SiteLogin(getWebDriver()).login(test_shop).openMenuItem(AdminBasePage.MenuItem.CATALOG);
        product = new Product();
        product.setPrice(new BigDecimal("11.00"));
    }

    @Test
    public void createUnpublishedProduct() {
        final boolean isPublished = false;
        product.setName("test product " + System.currentTimeMillis());
        product.setActivity(isPublished);
        catalog.createSimpleProduct(product);
        assertEquals("Не активен",
                catalog.findElement(By.xpath("//a[text()='" + product.getName() + "']/ancestor::tr"))
                        .findElement(Catalog.state_col).getAttribute("data-original-title"));
        catalog.showProductOnSite(product)
                .switchTo().frame(0);
        assertTrue(catalog.is404());
        logger.info("Тест создания неопубликованного товара пройден успешно");
    }

    @Test
    public void createPublishedProduct() {
        final boolean isPublished = true;
        product.setName("test product " + System.currentTimeMillis());
        product.setActivity(isPublished);
        catalog.createSimpleProduct(product);
        Assert.assertEquals("Активен",
                catalog.findElement(By.xpath("//a[text()='" + product.getName() + "']/ancestor::tr"))
                        .findElement(Catalog.state_col).getAttribute("data-original-title"));
        catalog.showProductOnSite(product)
                .switchTo().frame(0);
        Assert.assertEquals(product.getName().toUpperCase(),
                catalog.findElement(ProductPage.product_name).getText());
        logger.info("Тест создания опубликованного товара пройден успешно");
    }

    @Test
    public void editPublishing() {
        boolean isPublished = false;
        product.setName("test product " + System.currentTimeMillis());
        product.setActivity(isPublished);

        catalog.createSimpleProduct(product);

        assertEquals("Не активен",
                catalog.findElement(By.xpath("//a[text()='" + product.getName() + "']/ancestor::tr"))
                        .findElement(Catalog.state_col).getAttribute("data-original-title"));
        isPublished = true;
        product.setActivity(isPublished);
        catalog.editSimpleProduct(product.getName(), product)
                .showProductOnSite(product)
                .switchTo().frame(0);
        assertEquals(product.getName().toUpperCase(),
                catalog.findElement(ProductPage.product_name).getText());
        logger.info("Тест изменения публикации товара пройден успешно");
    }
}