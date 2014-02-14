package com.epicsell.tests.admin.catalog;

import com.epicsell.TestCore;
import com.epicsell.beans.Product;
import com.epicsell.beans.TestCategory;
import com.epicsell.drivers.ExtendedWebDriver;
import com.epicsell.pages.admin.AdminBasePage;
import com.epicsell.pages.admin.Catalog;
import com.epicsell.pages.site.ProductPage;
import com.epicsell.pages.wizard.SiteLogin;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.math.BigDecimal;

/**
 * User: Aerien
 * Date: 08.04.13
 */
@Category(TestCategory.Ignored.class)
public class ProductPrice extends TestCore {
    private static final Logger logger = Logger.getLogger(ProductPrice.class);
    private static ExtendedWebDriver DRIVER;

    @Before
    public void setUrl() {
        DRIVER = getWebDriver();
        new SiteLogin(DRIVER).login(test_shop);
    }

    @Test
    public void createProductWithMaxPrice() {
        final String expectedName = "Продукт с max ценой " + System.currentTimeMillis();
        final String expectedPrice = "100000001.00";
        final String expectedValidPrice = "100000000.00";
        Product product = new Product();
        product.setName(expectedName);
        product.setPrice(new BigDecimal(expectedPrice));

        new Catalog(DRIVER).createSimpleProduct(product);

        assertEquals(expectedName,
                DRIVER.findElement(Catalog.product_list_table_body).findElement(Catalog.name_col_link).getText());
        assertEquals(expectedValidPrice + " руб.",
                DRIVER.findElement(Catalog.product_list_table_body).findElement(Catalog.price_col).getText());
        logger.info("Тест создания товара с максимальной ценой пройден успешно");
    }

    @Test
    public void editProductPrice() {
        final String baseName = "Prod " + System.currentTimeMillis();
        final String basePrice = "211.00";
        final String expectedPrice = "1234.00";
        Product product = new Product();
        product.setName(baseName);
        product.setPrice(new BigDecimal(basePrice));

        new Catalog(DRIVER).createSimpleProduct(product);

        product.setPrice(new BigDecimal(expectedPrice));
        new Catalog(DRIVER).editSimpleProduct(baseName, product)
                .showProductOnSite(product)
                .switchTo().frame(0);
        assertEquals(expectedPrice,
                DRIVER.findElement(ProductPage.product_price).getText());
        logger.info("Тест создания товара с максимальной ценой пройден успешно");
    }

    @Test
    public void createProductWithNegativePrice() {
        final String expectedName = "Продукт с отрицательной ценой " + System.currentTimeMillis();
        final String expectedPrice = "-10.00";
        Product product = new Product();
        product.setName(expectedName);
        product.setPrice(new BigDecimal(expectedPrice));

        DRIVER.findElement(Catalog.catalog_menu_link).click();
        DRIVER.waiter().until(ExpectedConditions.titleContains("Товары"));
        DRIVER.findElement(AdminBasePage.create_bean_button).click();
        DRIVER.waiter().until(ExpectedConditions.visibilityOfElementLocated(Catalog.product_edit_form));
        new Catalog(DRIVER).fillProductEditForm(product);

        DRIVER.findElement(Catalog.save_product_button).click();
        try {
            new WebDriverWait(DRIVER, 15).until(ExpectedConditions.invisibilityOfElementLocated(Catalog.product_edit_form));
        } catch (TimeoutException te) {/**/}

        Assert.assertTrue(DRIVER.findElement(Catalog.product_edit_form).isDisplayed());
        Assert.assertEquals("rgba(185, 74, 72, 1)", DRIVER.findElement(Catalog.product_price_input).getCssValue("border-bottom-color"));
        logger.info("Тест создания товара с отрицательной ценой пройден успешно");
    }

    @Test
    public void createProductWithWrongPrice() {
        final String expectedName = "Продукт с невалидной ценой " + System.currentTimeMillis();
        final String nonValidPrice = "hg";
        Product product = new Product();
        product.setName(expectedName);

        DRIVER.findElement(Catalog.catalog_menu_link).click();
        DRIVER.waiter().until(ExpectedConditions.titleContains("Товары"));
        DRIVER.findElement(AdminBasePage.create_bean_button).click();
        DRIVER.waiter().until(ExpectedConditions.visibilityOfElementLocated(Catalog.product_edit_form));
        new Catalog(DRIVER).fillProductEditForm(product);
        WebElement prod_price = DRIVER.findElement(Catalog.product_price_input);

        prod_price.clear();
        prod_price.sendKeys(nonValidPrice);
        DRIVER.blurField(prod_price);
        DRIVER.findElement(Catalog.save_product_button).click();
        try {
            new WebDriverWait(DRIVER, 15).until(ExpectedConditions.invisibilityOfElementLocated(Catalog.product_edit_form));
        } catch (TimeoutException te) {/**/}

        Assert.assertTrue(DRIVER.findElement(Catalog.product_edit_form).isDisplayed());
        Assert.assertEquals("rgba(185, 74, 72, 1)", DRIVER.findElement(Catalog.product_price_input).getCssValue("border-bottom-color"));
        logger.info("Тест создания товара с максимальной ценой пройден успешно");
    }
}