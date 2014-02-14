package com.epicsell.tests.admin.catalog;

import com.epicsell.TestCore;
import com.epicsell.beans.Product;
import com.epicsell.beans.TestCategory;
import com.epicsell.drivers.ExtendedWebDriver;
import com.epicsell.links.AdminLinks;
import com.epicsell.pages.admin.Catalog;
import com.epicsell.pages.site.SiteMainPage;
import com.epicsell.pages.wizard.SiteLogin;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.math.BigDecimal;

/**
 * User: Aerien
 * Date: 08.04.13
 */
@Category(TestCategory.Ignored.class)
public class ProductCategories extends TestCore {
    private static final Logger logger = Logger.getLogger(ProductCategories.class);
    private static ExtendedWebDriver DRIVER;
    private Product product;

    @Before
    public void setUrl() {
        DRIVER = getWebDriver();
        new SiteLogin(DRIVER).login(test_shop);
        product = new Product();
        product.setName("test product " + System.currentTimeMillis());
        product.setPrice(new BigDecimal("155.00"));
    }

    @Test
    public void createProductWithNewCategory() {
        final String expectedCategory = "cat" + System.currentTimeMillis();
        product.setCategory(expectedCategory);
        new Catalog(DRIVER).createSimpleProduct(product)
                .open(test_shop.getShopUrl() + "/category/" + expectedCategory)
                .switchTo().frame(0);
        SiteMainPage page = new SiteMainPage(DRIVER);
        Assert.assertFalse(page.is404());
        Assert.assertTrue(page.getProductCards(product.getName()).size() > 0);
        logger.info("Тест создания товара с созданием новой категории пройден успешно");
    }

    @Test
    public void createProductWithSeveralNewCategories() {
        StringBuilder expectedCategories = new StringBuilder();
        final long rand = System.currentTimeMillis();
        expectedCategories.append("cat").append(rand / 10).append(",");
        expectedCategories.append("cat").append(rand / 20).append(",");
        expectedCategories.append("cat").append(rand / 30).append(",");
        expectedCategories.append("cat").append(rand / 40);
        product.setCategory(expectedCategories.toString());

        new Catalog(DRIVER).createSimpleProduct(product);
        for (String category : expectedCategories.toString().split(",")) {
            DRIVER.navigate().to(test_shop.getShopUrl() + "/category/" + category);
            DRIVER.loader();

            DRIVER.switchTo().frame(0);
            Assert.assertFalse("Category wasn't created", DRIVER.is404());
            Assert.assertTrue("Category is not assigned to product", new SiteMainPage(DRIVER).getProductCards(product.getName()).size() > 0);
            DRIVER.switchTo().defaultContent();
        }

        logger.info("Тест создания товара с созданием нескольких новых категорий пройден успешно");
    }

    @Test
    public void deleteCategoryFromProduct() {
        final long rand = System.currentTimeMillis();
        String category1 = "cat" + rand / 10;
        String category2 = "cat" + rand / 20;
        product.setCategory(category1 + "," + category2);

        Catalog catalog = new Catalog(DRIVER).createSimpleProduct(product);
        catalog.open(test_shop.getShopUrl() + "/category/" + category1)
                .switchTo().frame(0);
        Assert.assertFalse(DRIVER.is404());
        Assert.assertTrue(new SiteMainPage(DRIVER).getProductCards(product.getName()).size() > 0);
        DRIVER.navigate().to(test_shop.getShopUrl() + "/category/" + category2);
        DRIVER.switchTo().frame(0);
        Assert.assertFalse(DRIVER.is404());
        Assert.assertTrue(new SiteMainPage(DRIVER).getProductCards(product.getName()).size() > 0);

        product.setCategory(category2);
        ((Catalog) catalog.open(test_shop.getShopUrl() + AdminLinks.PRODUCTS.$()))
                .editSimpleProduct(product.getName(), product)
                .open(test_shop.getShopUrl() + "/category/" + category1)
                .switchTo().frame(0);
        Assert.assertFalse(DRIVER.is404());
        Assert.assertFalse(new SiteMainPage(DRIVER).getProductCards(product.getName()).size() > 0);
        DRIVER.navigate().to(test_shop.getShopUrl() + "/category/" + category2);
        DRIVER.switchTo().frame(0);
        Assert.assertFalse(DRIVER.is404());
        Assert.assertTrue(new SiteMainPage(DRIVER).getProductCards(product.getName()).size() > 0);
        logger.info("Тест создания товара с удалением одной из категорий с товара пройден успешно");
    }
}