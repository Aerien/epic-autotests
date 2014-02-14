package com.epicsell.tests.admin.catalog;

import com.epicsell.TestCore;
import com.epicsell.beans.Product;
import com.epicsell.beans.TestCategory;
import com.epicsell.pages.admin.AdminBasePage;
import com.epicsell.pages.admin.Catalog;
import com.epicsell.pages.site.ProductPage;
import com.epicsell.pages.wizard.SiteLogin;
import com.epicsell.utils.TestUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

/**
 * User: Aerien
 * Date: 08.04.13
 */
@Category(TestCategory.Ignored.class)
public class ProductImages extends TestCore {
    private static final Logger logger = Logger.getLogger(ProductImages.class);
    //    private static ExtendedWebDriver catalog;
    Catalog catalog;
    private Product product;

    @Before
    public void setUrl() {
        catalog = (Catalog) new SiteLogin(getWebDriver()).login(test_shop).openMenuItem(AdminBasePage.MenuItem.CATALOG);
        product = new Product();
        product.setName("test product " + System.currentTimeMillis());
        product.setPrice(new BigDecimal("155.00"));
    }

    @Test
    public void createProductWithOneImage() {
        final String expectedImage = TestUtils.getProjectLocation() + File.separator + "files" + File.separator
                + "resources" + File.separator + "image1.jpg";
        product.setImage(expectedImage);

        catalog.createSimpleProduct(product).refresh();
        catalog.findElement(By.xpath("//a[text()='" + product.getName() + "']")).click();
        catalog.waiter().until(ExpectedConditions.visibilityOfElementLocated(Catalog.product_edit_form));
        List<WebElement> imageList = catalog.findElement(Catalog.product_images_list).findElements(Catalog.product_images);
        Assert.assertEquals(1, imageList.size());
        String expectedImageLink = imageList.get(0).getAttribute("src");
        expectedImageLink = expectedImageLink.replace("138x138", "360x360");
        catalog.open(catalog.findElement(Catalog.product_show_on_site).getAttribute("href"))
                .loader().switchTo().frame(0);
        Assert.assertEquals(expectedImageLink,
                catalog.findElement(ProductPage.product_images_list).findElements(By.tagName("img")).get(0).getAttribute("src"));
        logger.info("Тест создания товара с заливкой изображения пройден успешно");
    }

    @Test
    public void createProductWithSeveralImages() {
        final String resourcePath = TestUtils.getProjectLocation() + File.separator + "files" + File.separator
                + "resources" + File.separator;
        final String expectedImage1 = resourcePath + "image1.jpg";
        final String expectedImage2 = resourcePath + "image2.jpg";
        final String expectedImage3 = resourcePath + "image3.jpg";
        product.setImage(expectedImage1 + "," + expectedImage2 + "," + expectedImage3);

        catalog.createSimpleProduct(product)
                .refresh();
        catalog.findElement(By.xpath("//a[text()='" + product.getName() + "']")).click();
        catalog.waiter().until(ExpectedConditions.visibilityOfElementLocated(Catalog.product_edit_form));
        List<WebElement> imageList = catalog.findElement(Catalog.product_images_list).findElements(Catalog.product_images);
        Assert.assertEquals(3, imageList.size());
        LinkedList<String> expectedImagesLinks = new LinkedList<String>();
        for (WebElement image : imageList) {
            String src = image.getAttribute("src");
            src = src.substring(src.lastIndexOf("/"), src.length());
            expectedImagesLinks.add(src);
        }
        catalog.get(catalog.findElement(Catalog.product_show_on_site).getAttribute("href"));
        catalog.loader();
        catalog.switchTo().frame(0);
        LinkedList<String> actualImagesLinks = new LinkedList<String>();
        for (WebElement image : catalog.findElement(ProductPage.product_images_list).findElements(Catalog.product_images)) {
            String src = image.getAttribute("src");
            src = src.substring(src.lastIndexOf("/"), src.length());
            actualImagesLinks.add(src);
        }
        Assert.assertArrayEquals(expectedImagesLinks.toArray(), actualImagesLinks.toArray());
        logger.info("Тест создания товара с заливкой нескольких изображений пройден успешно");
    }

    @Test
    public void addImageToExistedProduct() {
        final String resourcePath = TestUtils.getProjectLocation() + File.separator + "files" + File.separator
                + "resources" + File.separator;
        final String expectedImage1 = resourcePath + "image4.gif";
        Product product = new Product();
        product.setImage(expectedImage1);
        catalog.findElement(Catalog.catalog_menu_link).click();
        catalog.waiter().until(ExpectedConditions.titleContains("Товары"));
        catalog.findElement(Catalog.product_list_table_body).findElement(Catalog.name_col_link).click();
        catalog.waiter().until(ExpectedConditions.visibilityOfElementLocated(Catalog.product_edit_form));

        final int expectedImagesCount =
                catalog.findElement(Catalog.product_images_list).findElements(Catalog.product_images).size() + 1;
        WebElement input = catalog.findElement(Catalog.product_upload_photo_input);
        catalog.executeScript("arguments[0].scrollIntoView();", input);
        input.sendKeys(expectedImage1);
        catalog.waiter().until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return driver.findElements(Catalog.product_images).size() == expectedImagesCount;
            }
        });
        catalog.get(catalog.findElement(Catalog.product_show_on_site).getAttribute("href"));
        catalog.loader();
        catalog.switchTo().frame(0);
        Assert.assertEquals(expectedImagesCount,
                catalog.findElement(ProductPage.product_images_list).findElements(Catalog.product_images).size());

        logger.info("Тест добавления изображения к существующему товару пройден успешно");
    }

    @Test
    public void deleteImageFromProduct() {
        final String resourcePath = TestUtils.getProjectLocation() + File.separator + "files" + File.separator
                + "resources" + File.separator;
        final String expectedImage1 = resourcePath + "image1.jpg";
        final String expectedImage2 = resourcePath + "image2.jpg";
        final String expectedImage3 = resourcePath + "image3.jpg";
        product.setImage(expectedImage1 + "," + expectedImage2 + "," + expectedImage3);

//        catalog.findElement(By.xpath("//a")).sendKeys(Keys.F11);
        catalog.openMenuItem(AdminBasePage.MenuItem.CATALOG);
        catalog.createSimpleProduct(product).refresh();
        catalog.findElement(By.xpath("//a[text()='" + product.getName() + "']")).click();
        catalog.waiter().until(ExpectedConditions.visibilityOfElementLocated(Catalog.product_edit_form));
        List<WebElement> imageList = catalog.findElement(Catalog.product_images_list).findElements(Catalog.product_images);
        catalog.executeScript("arguments[0].scrollIntoView();", imageList.get(1));
        imageList.get(1).findElement(By.xpath("./ancestor::li//a[@class='delete']")).click();
        final int imagesCount = imageList.size();
        new WebDriverWait(catalog, 2).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return driver.findElement(Catalog.product_images_list).findElements(Catalog.product_images).size() < imagesCount;
            }
        });
        imageList = catalog.findElement(Catalog.product_images_list).findElements(Catalog.product_images);
        Assert.assertEquals(2, imageList.size());

        LinkedList<String> expectedImagesLinks = new LinkedList<String>();
        for (WebElement image : imageList) {
            String src = image.getAttribute("src");
            src = src.substring(src.lastIndexOf("/"), src.length());
            expectedImagesLinks.add(src);
        }
        catalog.showProductOnSite(product)
                .switchTo().frame(0);
        LinkedList<String> actualImagesLinks = new LinkedList<String>();
        for (WebElement image : catalog.findElement(ProductPage.product_images_list).findElements(Catalog.product_images)) {
            String src = image.getAttribute("src");
            src = src.substring(src.lastIndexOf("/"), src.length());
            actualImagesLinks.add(src);
        }
        Assert.assertArrayEquals(expectedImagesLinks.toArray(), actualImagesLinks.toArray());
        logger.info("Тест создания товара с заливкой нескольких изображений пройден успешно");
    }
}

