package com.epicsell.tests.admin.pages;

import com.epicsell.TestCore;
import com.epicsell.beans.Page;
import com.epicsell.beans.TestCategory;
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
 * Date: 19.05.13
 */
@Category(TestCategory.Ignored.class)
public class PageDeleting extends TestCore {
    private static final Logger logger = Logger.getLogger(UnpublishedPages.class);

    @Test
    public void deletePage() throws Exception {
        Page page = new Page();
        page.setName("test" + System.currentTimeMillis());
        page.setActivity(true);
        page.setContent("test content");
        Journal journal = ((Journal) new SiteLogin(getWebDriver())
                .login(test_shop)
                .openMenuItem(AdminBasePage.MenuItem.PAGES))
                .createPage(page);
        Assert.assertEquals("Опубликована", journal.openLastPage().findElement(page.getAdminPageLocator()).getText());
        Assert.assertEquals("Страница со статусом Опубликована не отображается в браузере",
                String.valueOf(200), String.valueOf(journal.getResponseCode(page.getPageLink(test_shop))));
        journal.deletePage(page);
        Assert.assertEquals("Удаленная страница отображается в браузере",
                String.valueOf(404), String.valueOf(journal.getResponseCode(page.getPageLink(test_shop))));
        logger.info("Тест удаления страницы пройден успешно");
    }
}
