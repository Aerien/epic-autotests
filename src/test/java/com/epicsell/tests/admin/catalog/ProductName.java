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
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

import java.math.BigDecimal;
import java.util.List;

/**
 * User: Aerien
 * Date: 07.04.13
 */
@Category(TestCategory.Ignored.class)
public class ProductName extends TestCore {
    private static final Logger logger = Logger.getLogger(ProductName.class);
    private static Catalog catalog;

    @Before
    public void setUrl() {
        catalog = (Catalog) new SiteLogin(getWebDriver()).login(test_shop).openMenuItem(AdminBasePage.MenuItem.CATALOG);
    }

    @Test
    public void createProductWithValidName() {
        final String expectedName = "Тестовый продукт с валидным именем " + System.currentTimeMillis();
        final String expectedPrice = "100.00";
        Product product = new Product();
        product.setName(expectedName);
        product.setPrice(new BigDecimal(expectedPrice));

        catalog.createSimpleProduct(product);

        Assert.assertEquals(expectedName,
                catalog.findElement(Catalog.product_list_table_body).findElement(Catalog.name_col_link).getText());
        Assert.assertEquals(expectedPrice + " руб.",
                catalog.findElement(Catalog.product_list_table_body).findElement(Catalog.price_col).getText());
        logger.info("Тест создания товара с валидным именем пройден успешно");
    }

    @Test
    public void createProductWithDuplicatedName() {
        final String expectedName = "Тестовый продукт с валидным именем " + System.currentTimeMillis();
        final String expectedPrice = "100.00";
        final String expectedPrice2 = "200.00";
        Product product = new Product();
        Product product2 = new Product();
        product.setName(expectedName);
        product2.setName(expectedName);
        product.setPrice(new BigDecimal(expectedPrice));
        product2.setPrice(new BigDecimal(expectedPrice2));

        catalog.createSimpleProduct(product);
        catalog.createSimpleProduct(product2);
        final String productName = product.getName();
        List<WebElement> products = catalog.waiter().until(new ExpectedCondition<List<WebElement>>() {
            @Override
            public List<WebElement> apply(org.openqa.selenium.WebDriver driver) {
                List<WebElement> products;
                products = catalog.findElements(By.xpath("//a[text()='" + productName + "']"));
                return products.size() == 2 ? products : null;
            }
        });
        By parent = By.xpath("./ancestor::tr");
        Assert.assertEquals(expectedPrice2 + " руб.", products.get(0).findElement(parent).findElement(Catalog.price_col).getText());
        Assert.assertEquals(expectedPrice + " руб.", products.get(1).findElement(parent).findElement(Catalog.price_col).getText());
        logger.info("Тест создания товара с дублирующимся именем пройден успешно");
    }

    @Test
    public void createProductWithLongName() {
        final String expectedName = "Тестовый продукт с очень-очень длинным именем " + System.currentTimeMillis() +
                " йцукенгшщзхъфывапролджэячсмитьбю.йцукенгшщзхїфівапролджєячсмитьбю.спаропіпвн5у759762ороролрлрл" +
                " оооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооо" +
                " рррррррррррррррррррррррррррррррррррррррррррррррррррррррррррррррррррррррррррррррррррррррррррррр";
        final String expectedPrice = "100.00";
        Product product = new Product();
        product.setName(expectedName);
        product.setPrice(new BigDecimal(expectedPrice));
        catalog.createSimpleProduct(product);
        logger.info("Тест создания товара с длинным именем пройден успешно");
    }

    @Test
    public void createProductWithSpecialName() {
        final String expectedName = "Имя продукта \"Ёё!№;%:?*()_+-=/\\@#$%^&amp;:'gіїЇІ' " + System.currentTimeMillis();
        final String expectedPrice = "100.00";
        Product product = new Product();
        product.setName(expectedName);
        product.setPrice(new BigDecimal(expectedPrice));

        catalog.createSimpleProduct(product);

        Assert.assertEquals(expectedName,
                catalog.findElement(Catalog.product_list_table_body).findElement(Catalog.name_col_link).getText());
        Assert.assertEquals(expectedPrice + " руб.",
                catalog.findElement(Catalog.product_list_table_body).findElement(Catalog.price_col).getText());
        logger.info("Тест создания товара с именем со спецсимволами пройден успешно");
    }

    @Test
    public void editProductName() {
        final String baseName = "Prod name " + System.currentTimeMillis();
        final String expectedName = "Edited name " + System.currentTimeMillis();
        final String basePrice = "100.00";
        Product product = new Product();
        product.setName(baseName);
        product.setPrice(new BigDecimal(basePrice));

        catalog.createSimpleProduct(product);
        product.setName(expectedName);
        catalog.editSimpleProduct(baseName, product).waitForPopup();
        Assert.assertEquals(expectedName,
                catalog.findElement(Catalog.product_list_table_body).findElement(Catalog.name_col_link).getText());

        catalog.showProductOnSite(product)
                .switchTo().frame(0);
        assertEquals(product.getName().toUpperCase(),
                catalog.findElement(ProductPage.product_name).getText());

        logger.info("Тест редактирования имени товара пройден успешно");
    }
}