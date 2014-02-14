package com.epicsell.tests.admin.catalog;

import com.epicsell.TestCore;
import com.epicsell.beans.GenerateFileForUploadingProducts;
import com.epicsell.beans.Product;
import com.epicsell.beans.TestCategory;
import com.epicsell.drivers.ExtendedWebDriver;
import com.epicsell.links.AdminLinks;
import com.epicsell.pages.admin.Catalog;
import com.epicsell.pages.admin.Settings;
import com.epicsell.pages.site.SiteMainPage;
import com.epicsell.pages.wizard.SiteLogin;
import com.epicsell.tests.user_account.ShopCreation;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.By;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: Jane T.
 * Date: 06.04.13
 */
@Category(TestCategory.Ignored.class)
public class ProductDeleting extends TestCore {
    private static final Logger logger = Logger.getLogger(ShopCreation.class);
    private static ExtendedWebDriver DRIVER;
    private static String filePath = new File("").getAbsolutePath() + File.separator + "files" + File.separator
            + "productUpload" + System.currentTimeMillis() + ".csv";

    @Before
    public void setUrl() {
        DRIVER = getWebDriver();
        new SiteLogin(DRIVER).login(test_shop3);
    }

    @Test
    public void deletePublishedProduct() throws Exception {
        Integer prCount = 1;
        ArrayList<Product> products = Product.getProducts(prCount);
        for (Product product : products) {
            product.setActivity(true);
        }
        new GenerateFileForUploadingProducts(products, filePath);
        Assert.assertEquals("Создано продуктов: " + prCount + "\nОбновлено продуктов: 0",
                new Settings(DRIVER).uploadProductsFile(test_shop3, filePath)
                        .findElement(Settings.upload_file_success_message).getText());
        DRIVER.get(test_shop3.getShopUrl() + AdminLinks.PRODUCTS.$());
        for (Product product : products) {
            Assert.assertFalse(new Catalog(DRIVER).deleteSimpleProduct(product.getName())
                    .findElements(By.xpath("//a[text()='" + product.getName() + "']")).size() > 0);
        }
        logger.info("Тест удаления товар пройден успешно");
    }

    @Test
    public void deleteUnpublishedProduct() throws Exception {
        Integer prCount = 1;
        ArrayList<Product> products = Product.getProducts(prCount);
        for (Product product : products) {
            product.setActivity(false);
            product.setName(product.getName().replace("name", "delete"));
        }
        new GenerateFileForUploadingProducts(products, filePath);
        Assert.assertEquals("Создано продуктов: " + prCount + "\nОбновлено продуктов: 0",
                new Settings(DRIVER).uploadProductsFile(test_shop3, filePath)
                        .findElement(Settings.upload_file_success_message).getText());
        DRIVER.get(test_shop3.getShopUrl() + AdminLinks.PRODUCTS.$());
        for (Product product : products) {
            Assert.assertFalse(new Catalog(DRIVER).deleteSimpleProduct(product.getName())
                    .findElements(By.xpath("//a[text()='" + product.getName() + "']")).size() > 0);
        }
        logger.info("Тест удаления товар пройден успешно");
    }

    @Test
    public void deleteProducts() throws Exception {
        Integer prCount = 3;
        ArrayList<Product> products = Product.getProducts(prCount);
        for (Product product : products) {
            product.setActivity(false);
        }
        new GenerateFileForUploadingProducts(products, filePath);
        Assert.assertEquals("Создано продуктов: " + prCount + "\nОбновлено продуктов: 0",
                new Settings(DRIVER).uploadProductsFile(test_shop3, filePath)
                        .findElement(Settings.upload_file_success_message).getText());
        DRIVER.open(test_shop3.getShopUrl() + AdminLinks.PRODUCTS.$());
        new Catalog(DRIVER).deleteSeveralProducts(products);
        for (Product product : products) {
            Assert.assertFalse(new SiteMainPage(DRIVER).getProductCards(product.getName()).size() > 0);
        }
        logger.info("Тест удаления нескольких товар пройден успешно");
    }

    @Test
    public void deleteAllProducts() throws Exception {
        Integer prCount = 15;
        ArrayList<Product> products = Product.getProducts(prCount);
        for (Product product : products) {
            product.setActivity(false);
        }
        new GenerateFileForUploadingProducts(products, filePath);
        Assert.assertEquals("Создано продуктов: " + prCount + "\nОбновлено продуктов: 0",
                new Settings(DRIVER).uploadProductsFile(test_shop3, filePath)
                        .findElement(Settings.upload_file_success_message).getText());
        DRIVER.open(test_shop3.getShopUrl() + AdminLinks.PRODUCTS.$());
        new Catalog(DRIVER).deleteProductsMethod();
        Assert.assertTrue(DRIVER.isElementPresent(By.xpath("//img[contains(@src,'cap-prod-img.png')]")));
        logger.info("Тест удаления всех товаров пройден успешно");
    }

    @Test
    public void deleteAllCategories() throws Exception {
        DRIVER.open(test_shop3.getShopUrl() + AdminLinks.PRODUCTS.$());
        By delete = By.xpath("//li[contains(@id,'StoreCategoryTreeNode')]//a[@class='delete']");
        new Catalog(DRIVER).deleteProductsMethod();
        while (DRIVER.isElementPresent(delete)) {
            DRIVER.findElement(delete).click();
            TimeUnit.SECONDS.sleep(2);
        }
        Assert.assertEquals(1, DRIVER.findElements(By.xpath("//div[@id='StoreCategoryTree']//ul")).size());
        logger.info("Тест удаления всех категорий пройден успешно");
    }

    @After
    public void deleteTestFile() {
        File csv = new File(filePath);
        if (csv.exists()) {
            if (!csv.delete()) {
                logger.error("Can't delete file " + csv.getAbsolutePath());
            }
        }
    }
}
