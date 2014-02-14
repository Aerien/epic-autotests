package com.epicsell.tests.admin.pages;

import com.epicsell.TestCore;
import com.epicsell.beans.Link;
import com.epicsell.beans.Page;
import com.epicsell.beans.TestCategory;
import com.epicsell.drivers.ExtendedWebDriver;
import com.epicsell.links.AdminLinks;
import com.epicsell.pages.admin.AdminBasePage;
import com.epicsell.pages.admin.Journal;
import com.epicsell.pages.wizard.SiteLogin;
import com.epicsell.utils.TestUtils;
import org.apache.log4j.Logger;
import org.junit.*;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Created with IntelliJ IDEA.
 * User: Jane T.
 * Date: 14.04.13
 */
@Category(TestCategory.Ignored.class)
public class ContentFormatting extends TestCore {
    private static final Logger logger = Logger.getLogger(ContentFormatting.class);
    private static ExtendedWebDriver DRIVER;
    private static Page page;
    Journal journal;

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

    @Test
    public void createPageWithVolumeHTMLContent() throws Exception {
        page.setContent(TestUtils.readResourceFileContent("html_for_page_content"));
        try {
            journal.createPage(page, AdminBasePage.Mode.HTML).get(page.getPageLink(test_shop));
            DRIVER.switchTo().frame(DRIVER.findElement("//iframe"));
            Assert.assertFalse(DRIVER.isTextPresent("Ошибка обработки запроса."));
            String expected = page.getContent();
            expected = expected.replaceAll("\\s+", " ").trim();
            String actual = DRIVER.getInnerHtml(DRIVER.findElement(Journal.created_page_content));
            actual = actual.replaceAll("\\s+", " ").trim();
            Assert.assertEquals(expected, actual);
            logger.info("Тест валидатора при создании страницы объемным контентом с html содержимым");
        } finally {
            DRIVER.switchTo().defaultContent();
        }
    }

    @Test
    public void createPageWithBoldContent() throws Exception {
        page.setContent("test");
        try {
            journal.createPage(page, AdminBasePage.Mode.BOLD)
                    .open(page.getPageLink(test_shop))
                    .switchTo().frame(DRIVER.findElement("//iframe"));
            Assert.assertFalse(DRIVER.isTextPresent("Ошибка обработки запроса."));
            Assert.assertTrue(DRIVER.getInnerHtml(DRIVER.findElement(Journal.created_page_content))
                    .contains("<b>" + page.getContent() + "</b>"));
            logger.info("Тест режима bold при добавлении контента на страницу пройден успешно");
        } finally {
            DRIVER.switchTo().defaultContent();
        }
    }

    @Test
    public void createPageWithItalicContent() throws Exception {
        page.setContent("test");
        try {
            journal.createPage(page, AdminBasePage.Mode.ITALIC)
                    .get(page.getPageLink(test_shop));
            DRIVER.switchTo().frame(DRIVER.findElement("//iframe"));
            Assert.assertFalse(DRIVER.isTextPresent("Ошибка обработки запроса."));
            Assert.assertTrue(DRIVER.getInnerHtml(DRIVER.findElement(Journal.created_page_content))
                    .contains("<i>" + page.getContent() + "</i>"));
            logger.info("Тест режима bold при добавлении контента на страницу пройден успешно");
        } finally {
            DRIVER.switchTo().defaultContent();
        }
    }

    @Test
    public void createPageWithVideoContent() throws Exception {
        String youtubeLink = "http://www.youtube.com/embed/v4oN4DuR7YU";
        page.setContent("<iframe width=\"420\" height=\"315\" src=\"" + youtubeLink + "\" frameborder=\"0\" allowfullscreen></iframe>");
        try {
            journal.createPage(page, AdminBasePage.Mode.VIDEO)
                    .get(page.getPageLink(test_shop));
            DRIVER.switchTo().frame(DRIVER.findElement("//iframe"));
            Assert.assertFalse(DRIVER.isTextPresent("Ошибка обработки запроса."));
            Assert.assertTrue(DRIVER.findElements(By.xpath("//iframe[@src='" + youtubeLink + "']")).size() > 0);
            logger.info("Тест использования видео при добавлении контента на страницу пройден успешно");
        } finally {
            DRIVER.switchTo().defaultContent();
        }
    }

    @Test
    public void createPageWithImageContent() throws Exception {
        String pictureLink = "http://cs408527.vk.me/v408527679/379/v3FXL6kLloI.jpg";
        page.setContent(pictureLink);
        try {
            journal.createPage(page, AdminBasePage.Mode.IMAGE)
                    .get(page.getPageLink(test_shop));
            DRIVER.switchTo().frame(DRIVER.findElement("//iframe"));
            Assert.assertFalse(DRIVER.isTextPresent("Ошибка обработки запроса."));
            Assert.assertTrue(DRIVER.findElements(By.xpath("//img[@src='" + pictureLink + "']")).size() > 0);
            logger.info("Тест использования картинок при добавлении контента на страницу пройден успешно");
        } finally {
            DRIVER.switchTo().defaultContent();
        }
    }

    @Test
    public void createPageWithLinkContent() throws Exception {
        Link test_link = new Link();
        test_link.setNewTab(true);
        test_link.setUrl("http://cs408527.vk.me/v408527679/379/v3FXL6kLloI.jpg");
        test_link.setText("test link");
        test_link.setTarget(Link.Target.URL);
        try {
            journal.createPage(page, AdminBasePage.Mode.LINK, test_link)
                    .get(page.getPageLink(test_shop));
            DRIVER.switchTo().frame(DRIVER.findElement("//iframe"));
            Assert.assertFalse(DRIVER.isTextPresent("Ошибка обработки запроса."));
            WebElement linkElement = DRIVER.findElement(By.xpath("//a[@href='" + test_link.getUrl() + "']"));
            Assert.assertEquals("_blank", linkElement.getAttribute("target"));
            Assert.assertEquals(test_link.getText(), linkElement.getText());
            logger.info("Тест использования ссылок типа URL при добавлении контента на страницу пройден успешно");
        } finally {
            DRIVER.switchTo().defaultContent();
        }
    }


    @Test
    public void createPageWithEmailContent() throws Exception {
        Link test_link = new Link();
        test_link.setNewTab(true);
        test_link.setUrl(test_shop.getEmail());
        test_link.setText("test email");
        test_link.setTarget(Link.Target.EMAIL);
        try {
            journal.createPage(page, AdminBasePage.Mode.LINK, test_link)
                    .get(page.getPageLink(test_shop));
            DRIVER.switchTo().frame(DRIVER.findElement("//iframe"));
            Assert.assertFalse(DRIVER.isTextPresent("Ошибка обработки запроса."));
            WebElement linkElement = DRIVER.findElement(By.xpath("//a[contains(@href,'" + test_link.getUrl() + "')]"));
            Assert.assertEquals("mailto:" + test_link.getUrl(), linkElement.getAttribute("href"));
            Assert.assertEquals(test_link.getText(), linkElement.getText());
            logger.info("Тест использования ссылок типа EMAIL при добавлении контента на страницу пройден успешно");
        } finally {
            DRIVER.switchTo().defaultContent();
        }
    }

    @Test
    public void createPageWithAnchorContent() throws Exception {
        Link test_link = new Link();
        test_link.setNewTab(true);
        test_link.setUrl("test_anchor");
        test_link.setText("test anchor");
        test_link.setTarget(Link.Target.ANCHOR);
        try {
            journal.createPage(page, AdminBasePage.Mode.LINK, test_link)
                    .get(page.getPageLink(test_shop));
            DRIVER.switchTo().frame(DRIVER.findElement("//iframe"));
            Assert.assertFalse(DRIVER.isTextPresent("Ошибка обработки запроса."));
            WebElement linkElement = DRIVER.findElement(By.xpath("//a[contains(@href,'" + test_link.getUrl() + "')]"));
            Assert.assertEquals(test_shop.getShopUrl() + "/page/" + page.getName()
                    + "?iframe#" + test_link.getUrl(), linkElement.getAttribute("href"));
            Assert.assertEquals(test_link.getText(), linkElement.getText());
            logger.info("Тест использования форматирования типа ANCHOR при добавлении контента на страницу пройден успешно");
        } finally {
            DRIVER.switchTo().defaultContent();
        }
    }

    @Test
    public void createPageWithHorizontalRuleContent() throws Exception {
        try {
            journal.createPage(page, AdminBasePage.Mode.HORIZONTAL_RULE)
                    .get(page.getPageLink(test_shop));
            DRIVER.switchTo().frame(DRIVER.findElement("//iframe"));
            Assert.assertFalse(DRIVER.isTextPresent("Ошибка обработки запроса."));
            Assert.assertTrue(DRIVER.findElement(Journal.created_page_content).findElements(By.tagName("hr")).size() == 1);
            logger.info("Тест использования форматирования типа HORIZONTAL_RULE при добавлении контента на страницу пройден успешно");
        } finally {
            DRIVER.switchTo().defaultContent();
        }
    }

    @Test
    public void createPageWithFontColorContent() throws Exception {
        String color = "#b8cce4";
        try {
            journal.createPage(page)
                    .openEditingForm(page.getName());
            new AdminBasePage(DRIVER).setTextContentStyle(AdminBasePage.Mode.FONT_COLOR, color);
            journal.savePage()
                    .get(page.getPageLink(test_shop));
            DRIVER.get(test_shop.getShopUrl() + "/page/" + page.getName());
            DRIVER.switchTo().frame(DRIVER.findElement("//iframe"));
            Assert.assertFalse(DRIVER.isTextPresent("Ошибка обработки запроса."));
            Assert.assertTrue(DRIVER.findElement(Journal.created_page_content)
                    .findElements(By.xpath(".//font[@color='" + color + "'][text()='" + page.getContent() + "']")).size() == 1);
            logger.info("Тест использования форматирования типа FONT_COLOR при добавлении контента на страницу пройден успешно");
        } finally {
            DRIVER.switchTo().defaultContent();
        }
    }

    @Test
    public void createPageWithBGColorContent() throws Exception {
        String color = "#b8cce4";
        String bg_color = "#ccc1d9";
        try {
            journal.createPage(page).openEditingForm(page.getName())
                    .setTextContentStyle(AdminBasePage.Mode.FONT_COLOR, color)
                    .setTextContentStyle(AdminBasePage.Mode.BG_COLOR, bg_color);
            journal.savePage()
                    .open(page.getPageLink(test_shop))
                    .switchTo().frame(DRIVER.findElement("//iframe"));
            Assert.assertFalse(DRIVER.isTextPresent("Ошибка обработки запроса."));
            WebElement span = DRIVER.findElement(Journal.created_page_content)
                    .findElement(By.xpath(".//span[text()='" + page.getContent() + "']"));
            Assert.assertEquals(Color.fromString(bg_color).asRgba(), span.findElement(By.xpath("./..")).getCssValue("background-color"));
            Assert.assertEquals(Color.fromString(color).asRgba(), span.getCssValue("color"));
            logger.info("Тест использования форматирования типа BG_COLOR при добавлении контента на страницу пройден успешно");
        } finally {
            DRIVER.switchTo().defaultContent();
        }
    }


    @Test
    public void createPageWithCodeContent() throws Exception {
        try {
            journal.createPage(page, AdminBasePage.Mode.FORMATTING, AdminBasePage.Formatting.CODE)
                    .open(page.getPageLink(test_shop))
                    .switchTo().frame(0);
            Assert.assertFalse(DRIVER.isTextPresent("Ошибка обработки запроса."));
            DRIVER.findElement(Journal.created_page_content)
                    .findElement(By.xpath(".//pre[contains(text(),'" + page.getContent() + "')]"));
            logger.info("Тест использования CODE контента при добавлении контента на страницу пройден успешно");
        } finally {
            DRIVER.switchTo().defaultContent();
        }
    }

    @Test
    public void createPageWithQuoteContent() throws Exception {
        try {

            journal.createPage(page, AdminBasePage.Mode.FORMATTING, AdminBasePage.Formatting.QUOTE)
                    .get(page.getPageLink(test_shop));
            DRIVER.switchTo().frame(DRIVER.findElement("//iframe"));
            Assert.assertFalse(DRIVER.isTextPresent("Ошибка обработки запроса."));
            DRIVER.findElement(Journal.created_page_content)
                    .findElement(By.xpath(".//blockquote[contains(text(),'" + page.getContent() + "')" +
                            " or descendant::node()[contains(text(),'" + page.getContent() + "')]]"));
            logger.info("Тест использования QUOTE контента при добавлении контента на страницу пройден успешно");
        } finally {
            DRIVER.switchTo().defaultContent();
        }
    }

    @Test
    public void createPageWithHeader2Content() throws Exception {
        try {

            journal.createPage(page, AdminBasePage.Mode.FORMATTING, AdminBasePage.Formatting.HEADER2)
                    .get(page.getPageLink(test_shop));
            DRIVER.switchTo().frame(DRIVER.findElement("//iframe"));
            Assert.assertFalse(DRIVER.isTextPresent("Ошибка обработки запроса."));
            DRIVER.findElement(Journal.created_page_content)
                    .findElement(By.xpath(".//h2[contains(text(),'" + page.getContent() + "')]"));
            logger.info("Тест использования HEADER2 контента при добавлении контента на страницу пройден успешно");
        } finally {
            DRIVER.switchTo().defaultContent();
        }
    }

    @Test
    public void createPageWithHeader3Content() throws Exception {
        try {

            journal.createPage(page, AdminBasePage.Mode.FORMATTING, AdminBasePage.Formatting.HEADER3)
                    .get(page.getPageLink(test_shop));
            DRIVER.switchTo().frame(DRIVER.findElement("//iframe"));
            Assert.assertFalse(DRIVER.isTextPresent("Ошибка обработки запроса."));
            DRIVER.findElement(Journal.created_page_content)
                    .findElement(By.xpath(".//h3[contains(text(),'" + page.getContent() + "')]"));
            logger.info("Тест использования HEADER3 контента при добавлении контента на страницу пройден успешно");
        } finally {
            DRIVER.switchTo().defaultContent();
        }
    }

    @Test
    public void createPageWithHeader4Content() throws Exception {
        try {

            journal.createPage(page, AdminBasePage.Mode.FORMATTING, AdminBasePage.Formatting.HEADER4)
                    .get(page.getPageLink(test_shop));
            DRIVER.switchTo().frame(DRIVER.findElement("//iframe"));
            Assert.assertFalse(DRIVER.isTextPresent("Ошибка обработки запроса."));
            DRIVER.findElement(Journal.created_page_content)
                    .findElement(By.xpath(".//h4[contains(text(),'" + page.getContent() + "')]"));
            logger.info("Тест использования HEADER4 контента при добавлении контента на страницу пройден успешно");
        } finally {
            DRIVER.switchTo().defaultContent();
        }
    }

    @Ignore("Issue #47")
    @Test
    public void createPageWithCenterAlignmentContent() throws Exception {
        AdminBasePage.Alignment alignment = AdminBasePage.Alignment.CENTER;
        try {

            journal.createPage(page, AdminBasePage.Mode.ALIGNMENT, AdminBasePage.Alignment.CENTER)
                    .get(page.getPageLink(test_shop));
            DRIVER.switchTo().frame(DRIVER.findElement("//iframe"));
            Assert.assertFalse(DRIVER.isTextPresent("Ошибка обработки запроса."));
            Assert.assertEquals(alignment.getAlign(), DRIVER.findElement(Journal.created_page_content)
                    .findElement(By.xpath(".//p[contains(text(),'" + page.getContent() + "')]")).getCssValue("text-align"));
            logger.info("Тест использования " + alignment + " контента при добавлении контента на страницу пройден успешно");
        } finally {
            DRIVER.switchTo().defaultContent();
        }
    }

    @Ignore("Issue #47")
    @Test
    public void createPageWithRightAlignmentContent() throws Exception {
        AdminBasePage.Alignment alignment = AdminBasePage.Alignment.RIGHT;
        try {

            journal.createPage(page, AdminBasePage.Mode.ALIGNMENT, AdminBasePage.Alignment.CENTER)
                    .get(page.getPageLink(test_shop));
            DRIVER.switchTo().frame(DRIVER.findElement("//iframe"));
            Assert.assertFalse(DRIVER.isTextPresent("Ошибка обработки запроса."));
            Assert.assertEquals(alignment.getAlign(), DRIVER.findElement(Journal.created_page_content)
                    .findElement(By.xpath(".//p[contains(text(),'" + page.getContent() + "')]")).getCssValue("text-align"));
            logger.info("Тест использования " + alignment + " контента при добавлении контента на страницу пройден успешно");
        } finally {
            DRIVER.switchTo().defaultContent();
        }
    }

    @Ignore("Issue #47")
    @Test
    public void createPageWithLeftAlignmentContent() throws Exception {
        AdminBasePage.Alignment alignment = AdminBasePage.Alignment.LEFT;
        try {

            journal.createPage(page, AdminBasePage.Mode.ALIGNMENT, AdminBasePage.Alignment.CENTER)
                    .get(page.getPageLink(test_shop));
            DRIVER.switchTo().frame(DRIVER.findElement("//iframe"));
            Assert.assertFalse(DRIVER.isTextPresent("Ошибка обработки запроса."));
            Assert.assertEquals(alignment.getAlign(), DRIVER.findElement(Journal.created_page_content)
                    .findElement(By.xpath(".//p[contains(text(),'" + page.getContent() + "')]")).getCssValue("text-align"));
            logger.info("Тест использования " + alignment + " контента при добавлении контента на страницу пройден успешно");
        } finally {
            DRIVER.switchTo().defaultContent();
        }
    }

    @Ignore("Issue #47")
    @Test
    public void createPageWithTextJustifiedContent() throws Exception {
        AdminBasePage.Alignment alignment = AdminBasePage.Alignment.JUSTIFY;
        try {

            journal.createPage(page, AdminBasePage.Mode.ALIGNMENT, AdminBasePage.Alignment.CENTER)
                    .get(page.getPageLink(test_shop));
            DRIVER.switchTo().frame(DRIVER.findElement("//iframe"));
            Assert.assertFalse(DRIVER.isTextPresent("Ошибка обработки запроса."));
            Assert.assertEquals(alignment.getAlign(), DRIVER.findElement(Journal.created_page_content)
                    .findElement(By.xpath(".//p[contains(text(),'" + page.getContent() + "')]")).getCssValue("text-align"));
            logger.info("Тест использования " + alignment + " контента при добавлении контента на страницу пройден успешно");
        } finally {
            DRIVER.switchTo().defaultContent();
        }
    }

    @Test
    public void fullScreenContentMode() throws Exception {

        DRIVER.findElement(Journal.create_page).click();
        DRIVER.waiter().until(ExpectedConditions.visibilityOfElementLocated(Journal.page_creation_form));
        DRIVER.findElement(AdminBasePage.full_screen_mode_link).click();
        DRIVER.waiter().until(ExpectedConditions.presenceOfElementLocated(AdminBasePage.full_screen_mode_block));
        DRIVER.findElement(AdminBasePage.full_screen_mode_link).click();
        DRIVER.findElement(Journal.create_page_button).click();
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
