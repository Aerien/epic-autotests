package com.epicsell.tests.admin.pages;

import com.epicsell.TestCore;
import com.epicsell.beans.TestCategory;
import com.epicsell.drivers.ExtendedWebDriver;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Jane T.
 * Date: 21.04.13
 */
@Category(TestCategory.Ignored.class)
public class VerifyPublishedPage extends TestCore {
    private static final Logger logger = Logger.getLogger(VerifyPublishedPage.class);

    @Test
    public void verifyPagesAvailability() throws Exception {
        ExtendedWebDriver DRIVER = getWebDriver();
        DRIVER.get(test_shop.getShopUrl());
        List<WebElement> links = DRIVER.findElements(By.xpath("//a[starts-with(@href,'/page')]"));
        Collections.shuffle(links);
        if (links.size() > 20) {
            links = links.subList(0, 20);
        }
        for (WebElement link : links) {
            String linkUrl = link.getAttribute("href");
            Assert.assertEquals("200", String.valueOf(DRIVER.getResponseCode(linkUrl)));
        }
        logger.info("Тест доступности всех страниц, на которые есть ссылки на главной странице пройден успешно");
    }
}
