package com.epicsell.tests.admin.pages;

import com.epicsell.TestCore;
import com.epicsell.beans.Page;
import com.epicsell.beans.TestCategory;
import com.epicsell.drivers.ExtendedWebDriver;
import com.epicsell.links.AdminLinks;
import com.epicsell.pages.admin.Journal;
import com.epicsell.pages.wizard.SiteLogin;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.HashSet;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Jane T.
 * Date: 21.04.13
 */
@Category(TestCategory.Ignored.class)
public class CreatePagesWithTheSameName extends TestCore {
    private static final Logger logger = Logger.getLogger(ContentFormatting.class);
    private static Journal journal;
    private static ExtendedWebDriver DRIVER;
    private static Page page;

    @Before
    public void resetPageSettings() {
        DRIVER = getWebDriver();
        new SiteLogin(DRIVER).login(test_shop);
        page = new Page();
        page.setName("test" + System.currentTimeMillis());
        page.setActivity(true);
        page.setContent("test content");
    }

    @Test
    public void createPagesWithSameName() throws Exception {
        try {
            DRIVER.get(test_shop.getShopUrl() + AdminLinks.ADMIN_PAGES.$());
            journal = new Journal(DRIVER).createPage(page).createPage(page);
            DRIVER.get(test_shop.getShopUrl());
            DRIVER.switchTo().frame(DRIVER.findElement("//iframe"));
            Assert.assertFalse(DRIVER.isTextPresent("Ошибка обработки запроса."));
            List<WebElement> links = DRIVER.findElements(By.xpath("//a[contains(@href,'" + page.getPageLink(test_shop) + "')]"));
            HashSet<String> linksValues = new HashSet<String>();
            for (WebElement link : links) {
                Assert.assertEquals(page.getName(), link.getText());
                String url = link.getAttribute("href");
                Assert.assertTrue(url.contains(page.getPageLink(test_shop)));
                linksValues.add(url);
            }
            Assert.assertEquals(links.size(), linksValues.size());
            logger.info("Тест валидатора при создании страницы объемным контентом с html содержимым");
        } finally {
            DRIVER.switchTo().defaultContent();
        }
    }

    @After
    public void deletePage() {
        if (page != null && journal != null && page.getName() != null && !page.getName().isEmpty()) {
            try {
                journal.deletePage(page);
            } catch (Exception e) {
            }
        }
    }
}
