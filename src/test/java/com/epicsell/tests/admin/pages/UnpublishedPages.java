package com.epicsell.tests.admin.pages;

import com.epicsell.TestCore;
import com.epicsell.beans.Page;
import com.epicsell.beans.TestCategory;
import com.epicsell.drivers.ExtendedWebDriver;
import com.epicsell.pages.admin.AdminBasePage;
import com.epicsell.pages.admin.Journal;
import com.epicsell.pages.wizard.SiteLogin;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * Created with IntelliJ IDEA.
 * User: Jane T.
 * Date: 21.04.13
 */
@Category(TestCategory.Ignored.class)
public class UnpublishedPages extends TestCore {
    private static final Logger logger = Logger.getLogger(UnpublishedPages.class);

    @Test
    public void disablePage() throws Exception {
        Page page = new Page();
        Journal journal = null;
        try {
            ExtendedWebDriver DRIVER = getWebDriver();
            page.setName("test" + System.currentTimeMillis());
            page.setActivity(true);
            page.setContent("test content");
            journal = ((Journal) new SiteLogin(DRIVER)
                    .login(test_shop)
                    .openMenuItem(AdminBasePage.MenuItem.PAGES))
                    .createPage(page);
            Assert.assertEquals("Опубликована", journal.openLastPage().findElement(page.getAdminPageLocator()).getText());
            Assert.assertEquals("Страница со статусом Опубликована не отображается в браузере",
                    String.valueOf(200), String.valueOf(DRIVER.getResponseCode(page.getPageLink(test_shop))));
            page.setActivity(false);
            journal.editPage(page);
            Assert.assertEquals("Черновик", DRIVER.findElement(page.getAdminPageLocator()).getText());
            Assert.assertEquals("Страница со статусом Черновик отображается в браузере",
                    String.valueOf(404), String.valueOf(DRIVER.getResponseCode(page.getPageLink(test_shop))));
            logger.info("Тест распубликации страницы пройден успешно");
        } finally {
            if (page.getName() != null && journal != null && page.getName() != null && !page.getName().isEmpty()) {
                try {
                    journal.deletePage(page);
                } catch (Exception e) {
                }
            }
        }
    }

}
