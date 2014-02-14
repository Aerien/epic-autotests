package com.epicsell.tests.admin.pages;

import com.epicsell.TestCore;
import com.epicsell.beans.Page;
import com.epicsell.beans.TestCategory;
import com.epicsell.drivers.ExtendedWebDriver;
import com.epicsell.links.AdminLinks;
import com.epicsell.pages.admin.AdminBasePage;
import com.epicsell.pages.admin.Journal;
import com.epicsell.pages.wizard.SiteLogin;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.support.Color;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Jane T.
 * Date: 14.04.13
 */
@Category(TestCategory.Ignored.class)
public class PageCreation extends TestCore {
    private static final Logger logger = Logger.getLogger(PageCreation.class);
    private static ExtendedWebDriver DRIVER;
    private static Journal journal;
    private static Page page;

    @Before
    public void resetPageSettings() {
        DRIVER = getWebDriver();
        new SiteLogin(DRIVER).login(test_shop);
        page = new Page();
        page.setName("test" + System.currentTimeMillis());
        page.setActivity(true);
        page.setContent("test content");
        DRIVER.get(test_shop.getShopUrl() + AdminLinks.ADMIN_PAGES.$());
        journal = new Journal(DRIVER);
    }

    private List<String> getAllShops() throws Exception {
        String fileName = "C:\\Users\\TJ\\Desktop\\shops.txt";
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        List<String> shops = new ArrayList<String>();
        String line = "";
        while ((line = reader.readLine()) != null) {
            shops.add(line);
        }
        return shops;
    }

    @Test
    public void pagesCreationForLoadTesting() throws Exception {
        String valid_email = TestCore.testProperties.getProperty("email");
        String valid_password = TestCore.testProperties.getProperty("password");
        List<String> shops = getAllShops();
        for (String shop : shops) {
            journal.navigate().to(shop + "/admin");
            journal = ((Journal) new SiteLogin(journal).login(valid_email, valid_password).openMenuItem(AdminBasePage.MenuItem.PAGES));
            for (int i = 0; i < 10; i++) {
                Page page1 = new Page();
                page1.setName("p" + System.currentTimeMillis());
                page1.setActivity(true);
                journal.createPage(page1).waitForPopup();
                System.out.println(shop + "/page/" + page1.getName());
            }
            System.out.print("-----------------------------------------------------------------------------------");
        }
        logger.info("Тест создания статической страницы с корерктным названием пройден успешно");
    }

    @Test
    public void createPageWithValidName() {
        try {
            journal.createPage(page);
            DRIVER.get(page.getPageLink(test_shop));
            DRIVER.switchTo().frame(DRIVER.findElement("//iframe"));
            Assert.assertFalse(DRIVER.isTextPresent("Ошибка обработки запроса."));
            Assert.assertEquals(page.getContent(), DRIVER.findElement(Journal.created_page_content).getText());
            Assert.assertEquals(page.getName().toUpperCase(), DRIVER.findElement("//h1").getText());
            logger.info("Тест создания статической страницы с корерктным названием пройден успешно");
        } finally {
            DRIVER.switchTo().defaultContent();
        }
    }

    @Test
    public void createPageWithLongName() {
        page.setName("testtesttesttesttesttest testtesttesttesttesttesttest testtesttesttes " +
                "testtesttesttesttesttesttest testtesttesttesttesttesttest testtesttesttes " +
                "testtesttesttesttesttesttest testtesttesttesttesttesttest testtesttesttes " +
                "testtesttesttesttesttest" + System.currentTimeMillis());
        try {
            journal.createPage(page);
            DRIVER.get(page.getPageLink(test_shop));
            DRIVER.switchTo().frame(DRIVER.findElement("//iframe"));
            Assert.assertFalse(DRIVER.isTextPresent("Ошибка обработки запроса."));
            Assert.assertEquals(page.getContent(), DRIVER.findElement(Journal.created_page_content).getAttribute("textContent").trim());
            logger.info("Тест создания статической страницы с очень длинным названием пройден успешно");
        } finally {
            DRIVER.switchTo().defaultContent();
        }
    }


    @Test
    public void createPageWithEmptyName() {
        page.setName("");
        try {
            journal.createPage(page);
            Assert.assertEquals(Color.fromString("#B94A48").asRgba(),
                    DRIVER.findElement(Journal.page_name_input).getCssValue("color"));
            logger.info("Тест валидации при создании статической страницы с пустым названием пройден успешно");
        } finally {
            DRIVER.switchTo().defaultContent();
        }
    }

    @Test
    public void createPageWithNameMoreThan255Symbols() {
        page.setName("testtesttesttesttesttest testtesttesttesttesttesttest testtesttesttes " +
                "testtesttesttesttesttesttest testtesttesttesttesttesttest testtesttesttes " +
                "testtesttesttesttesttesttest testtesttesttesttesttesttest testtesttesttes " +
                "testtesttesttesttesttest" + System.currentTimeMillis() + "test");
        try {
            journal.createPage(page);
            DRIVER.get(test_shop.getShopUrl() + "/page/" + page.getName().substring(0, 255));
            DRIVER.switchTo().frame(DRIVER.findElement("//iframe"));
            Assert.assertFalse(DRIVER.isTextPresent("Ошибка обработки запроса."));
            Assert.assertEquals(page.getContent(), DRIVER.findElement(Journal.created_page_content).getAttribute("textContent").trim());
            logger.info("Тест валидатора при создании страницы с названием больше 255 символов пройден успешно");
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
