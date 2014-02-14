package com.epicsell.tests.design_constructor;

import com.epicsell.TestCore;
import com.epicsell.beans.Link;
import com.epicsell.beans.Settings;
import com.epicsell.beans.TestCategory;
import com.epicsell.pages.admin.AdminBasePage;
import com.epicsell.pages.site.SiteBasePage;
import com.epicsell.pages.site.SiteMainPage;
import com.epicsell.pages.wizard.SiteLogin;
import com.epicsell.utils.TestUtils;
import org.junit.*;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Created with IntelliJ IDEA.
 * User: TJ
 * Date: 28.07.13
 * Time: 16:17
 */
@Category({TestCategory.DesignPanel.class, TestCategory.Active.class})
public class ShopInfo extends TestCore {
    SiteMainPage page;
    Settings settings = new Settings();

    @Before
    public void preCondition() {
        new SiteLogin(getWebDriver())
                .login(test_shop)
                .get(test_shop.getShopUrl());
        page = new SiteMainPage(getWebDriver())
                .openMenuItem(SiteMainPage.MenuItem.INFO);
        settings.setName("");
    }

    @Test
    public void phoneWithSymbols() {
        settings.setPhone("тел: 8 (916) 790-73-22");
        page.setShopSettings(settings).switchTo().frame(0);
        assertEquals(settings.getPhone(), page.findElement(SiteBasePage.phone).getText());
    }

    @Test
    public void phoneWithSpecialSymbols() {
        settings.setPhone("\"Ёё!№;%:?*()_+-=/\\@#$%^&amp;:'gіїЇІ'");
        page.setShopSettings(settings).switchTo().frame(0);
        assertEquals(settings.getPhone().replace("&amp;", "&"), page.findElement(SiteBasePage.phone).getText());
    }

    @Test
    public void nameWithSpecialSymbols() {
        settings.setName("\"Ёё!№;%:?*()_+-=/\\@#$%^&:'gіїЇІ' ");
        page.setShopSettings(settings)
                .switchTo().frame(0);
        assertEquals(settings.getName().trim(), page.findElement(SiteBasePage.site_name).getText());
    }

    @Test
    public void longName() {
        settings.setName("Превысокомногорассмотрительствующий интернет-магазин рентгеноэлектрокардиографического оборудования в Москве");
        page.setShopSettings(settings)
                .switchTo().frame(0);
        assertEquals(settings.getName().trim(), page.findElement(SiteBasePage.site_name).getText());
    }

    @Test
    public void simpleDescription() {
        settings.setDescription("simple description");
        page.setShopSettings(settings).switchTo().frame(0);
        assertEquals(settings.getDescription(), page.findElement(SiteBasePage.description).getText().trim());
    }

    @Test
    public void descriptionWithSpecialSymbols() {
        settings.setDescription("\"Ёё!№;%:?*()_+-=/\\@#$%^&amp;:'gіїЇІ' ");
        page.setShopSettings(settings).switchTo().frame(0);
        assertEquals(settings.getDescription().trim(), page.findElement(SiteBasePage.description).getText().trim());
    }

    @Test
    public void setShopSettingsWithVolumeHTMLContent() throws Exception {
        settings.setDescription(TestUtils.readResourceFileContent("html_for_page_content"));
        try {
            page.setShopSettings(settings, AdminBasePage.Mode.HTML)
                    .switchTo().frame(0);
            String expected = settings.getDescription();
            expected = expected.replaceAll("\\s+", " ").trim();
            String actual = page.getInnerHtml(page.findElement(SiteBasePage.description));
            actual = actual.replaceAll("\\s+", " ").trim();
            Assert.assertEquals("<div class=\"fs-label\">Редактировать<div></div></div>" + expected, actual);
            logger.info("Тест использования description с объемным контентом с html содержимым");
        } finally {
            page.switchTo().defaultContent();
        }
    }

    @Test
    public void setShopSettingsWithBoldContent() throws Exception {
        settings.setDescription("test");
        try {
            page.setShopSettings(settings, AdminBasePage.Mode.BOLD)
                    .switchTo().frame(0);

            Assert.assertTrue(page.getInnerHtml(page.findElement(SiteBasePage.description))
                    .contains("&nbsp;" + settings.getDescription()));
            logger.info("Тест режима bold при настройке description магазина");
        } finally {
            page.switchTo().defaultContent();
        }
    }

    @Ignore("https://epicsell.tpondemand.com/RestUI/board.aspx?acid=D6FB33BAC43F0DCB478F9AFA28434F30#page=bug/658")
    @Test
    public void setShopSettingsWithItalicContent() throws Exception {
        settings.setDescription("test");
        try {
            page.setShopSettings(settings, AdminBasePage.Mode.ITALIC)
                    .switchTo().frame(0);

            Assert.assertTrue(page.getInnerHtml(page.findElement(SiteBasePage.description))
                    .contains("<i>" + settings.getDescription()));
            logger.info("Тест режима italic при настройке description магазина");
        } finally {
            page.switchTo().defaultContent();
        }
    }

    @Test
    public void setShopSettingsWithVideoContent() throws Exception {
        String youtubeLink = "http://www.youtube.com/embed/v4oN4DuR7YU";
        settings.setDescription("<iframe width=\"420\" height=\"315\" src=\"" + youtubeLink + "\" frameborder=\"0\" allowfullscreen></iframe>");
        try {
            page.setShopSettings(settings, AdminBasePage.Mode.VIDEO)
                    .switchTo().frame(0);

            Assert.assertTrue(page.findElements(By.xpath("//embed")).size() > 0);
            logger.info("Тест использования видео при настройке description магазина");
        } finally {
            page.switchTo().defaultContent();
        }
    }

    @Test
    public void setShopSettingsWithImageContent() throws Exception {
        String pictureLink = "http://cs408527.vk.me/v408527679/379/v3FXL6kLloI.jpg";
        settings.setDescription(pictureLink);
        try {
            page.setShopSettings(settings, AdminBasePage.Mode.IMAGE)
                    .switchTo().frame(0);

            Assert.assertTrue(page.findElements(By.xpath("//img[@src='" + pictureLink + "']")).size() > 0);
            logger.info("Тест использования картинок при настройке description магазина");
        } finally {
            page.switchTo().defaultContent();
        }
    }

    @Test
    public void setShopSettingsWithLinkContent() throws Exception {
        Link test_link = new Link();
        test_link.setNewTab(true);
        test_link.setUrl("http://cs408527.vk.me/v408527679/379/v3FXL6kLloI.jpg");
        test_link.setText("test link");
        test_link.setTarget(Link.Target.URL);
        settings.setDescription("");
        try {
            page.setShopSettings(settings, AdminBasePage.Mode.LINK, test_link)
                    .switchTo().frame(0);

            WebElement linkElement = page.findElement(By.xpath("//a[@href='" + test_link.getUrl() + "']"));
            Assert.assertEquals("_blank", linkElement.getAttribute("target"));
            Assert.assertEquals(test_link.getText(), linkElement.getText());
            logger.info("Тест использования ссылок типа URL при настройке description магазина");
        } finally {
            page.switchTo().defaultContent();
        }
    }

    @Test
    public void setShopSettingsWithEmailContent() throws Exception {
        Link test_link = new Link();
        test_link.setNewTab(true);
        test_link.setUrl(test_shop.getEmail());
        test_link.setText("test email");
        test_link.setTarget(Link.Target.EMAIL);
        settings.setDescription("");
        try {
            page.setShopSettings(settings, AdminBasePage.Mode.LINK, test_link)
                    .switchTo().frame(0);

            WebElement linkElement = page.findElement(By.xpath("//a[contains(@href,'" + test_link.getUrl() + "')]"));
            Assert.assertEquals("mailto:" + test_link.getUrl(), linkElement.getAttribute("href"));
            Assert.assertEquals(test_link.getText(), linkElement.getText());
            logger.info("Тест использования ссылок типа EMAIL при настройке description магазина");
        } finally {
            page.switchTo().defaultContent();
        }
    }

    @Test
    public void setShopSettingsWithAnchorContent() throws Exception {
        Link test_link = new Link();
        test_link.setNewTab(true);
        test_link.setUrl("test_anchor");
        test_link.setText("test anchor");
        test_link.setTarget(Link.Target.ANCHOR);
        settings.setDescription("");
        try {
            page.setShopSettings(settings, AdminBasePage.Mode.LINK, test_link)
                    .switchTo().frame(0);

            WebElement linkElement = page.findElement(By.xpath("//a[contains(@href,'" + test_link.getUrl() + "')]"));
            Assert.assertEquals(test_shop.getShopUrl()
                    + "/?iframe#" + test_link.getUrl(), linkElement.getAttribute("href"));
            Assert.assertEquals(test_link.getText(), linkElement.getText());
            logger.info("Тест использования ссылок типа ANCHOR при настройке description магазина");
        } finally {
            page.switchTo().defaultContent();
        }
    }

    @Test
    public void setShopSettingsWithHorizontalRuleContent() throws Exception {
        settings.setDescription("test");
        try {
            page.setShopSettings(settings, AdminBasePage.Mode.HORIZONTAL_RULE)
                    .switchTo().frame(0);
            Assert.assertTrue(page.findElement(SiteBasePage.description).findElements(By.tagName("hr")).size() == 1);
            logger.info("Тест использования ссылок типа HORIZONTAL_RULE при настройке description магазина");
        } finally {
            page.switchTo().defaultContent();
        }
    }

    @Test
    public void setShopSettingsWithFontColorContent() throws Exception {
        String color = "#b8cce4";
        settings.setDescription("test");
        try {
            page.setShopSettings(settings);
            WebElement content_input = page.waiter().until(ExpectedConditions.presenceOfElementLocated(SiteMainPage.description_input));
            new AdminBasePage(page)
                    .setTextContentStyle(content_input, AdminBasePage.Mode.FONT_COLOR, color)
                    .findElement(SiteMainPage.name_input).click();
            page.loader();
            page.switchTo().frame(0);
            Assert.assertEquals(page.findElement(SiteBasePage.description)
                    .findElement(By.xpath(".//span[contains(text(),'" + settings.getDescription() + "')]"))
                    .getCssValue("color"), Color.fromString(color).asRgba());
            logger.info("Тест использования ссылок типа FONT_COLOR при настройке description магазина");
        } finally {
            page.switchTo().defaultContent();
        }
    }

    @Test
    public void setShopSettingsWithBGColorContent() throws Exception {
        String color = "#b8cce4";
        String bg_color = "#ccc1d9";
        settings.setDescription("test");
        try {
            page.setShopSettings(settings);
            WebElement content_input = page.waiter().until(ExpectedConditions.presenceOfElementLocated(SiteMainPage.description_input));
            new AdminBasePage(page).setTextContentStyle(content_input, AdminBasePage.Mode.FONT_COLOR, color)
                    .setTextContentStyle(content_input, AdminBasePage.Mode.BG_COLOR, bg_color)
                    .findElement(SiteMainPage.name_input).click();
            page.loader().switchTo().frame(0);
            WebElement span = page.findElement(SiteBasePage.description)
                    .findElement(By.xpath(".//span[contains(text(),'" + settings.getDescription() + "')]"));
            Assert.assertEquals(Color.fromString(bg_color).asRgba(), span.findElement(By.xpath("./..")).getCssValue("background-color"));
            Assert.assertEquals(Color.fromString(color).asRgba(), span.getCssValue("color"));
            logger.info("Тест использования ссылок типа BG_COLOR при настройке description магазина");
        } finally {
            page.switchTo().defaultContent();
        }
    }

    @Test
    public void setShopSettingsWithCodeContent() throws Exception {
        settings.setDescription("test");
        try {
            page.setShopSettings(settings, AdminBasePage.Mode.FORMATTING, AdminBasePage.Formatting.CODE)
                    .switchTo().frame(0);
            page.findElement(SiteBasePage.description)
                    .findElement(By.xpath(".//pre[contains(text(),'" + settings.getDescription() + "')]"));
            logger.info("Тест использования CODE контента при настройке description магазина");
        } finally {
            page.switchTo().defaultContent();
        }
    }

    @Test
    public void setShopSettingsWithQuoteContent() throws Exception {
        settings.setDescription("test");
        try {
            page.setShopSettings(settings, AdminBasePage.Mode.FORMATTING, AdminBasePage.Formatting.QUOTE)
                    .switchTo().frame(0);
            page.findElement(SiteBasePage.description)
                    .findElement(By.xpath(".//blockquote[contains(text(),'" + settings.getDescription() + "')" +
                            " or descendant::node()[contains(text(),'" + settings.getDescription() + "')]]"));
            logger.info("Тест использования QUOTE контента при настройке description магазина");
        } finally {
            page.switchTo().defaultContent();
        }
    }

    @Test
    public void setShopSettingsWithHeader2Content() throws Exception {
        settings.setDescription("test");
        try {
            page.setShopSettings(settings, AdminBasePage.Mode.FORMATTING, AdminBasePage.Formatting.HEADER2)
                    .switchTo().frame(0);
            page.findElement(SiteBasePage.description)
                    .findElement(By.xpath("./h2[contains(text(),'" + settings.getDescription() + "')]"));
            logger.info("Тест использования HEADER2 контента при настройке description магазина");
        } finally {
            page.switchTo().defaultContent();
        }
    }

    @Test
    public void setShopSettingsWithHeader3Content() throws Exception {
        settings.setDescription("test");
        try {
            page.setShopSettings(settings, AdminBasePage.Mode.FORMATTING, AdminBasePage.Formatting.HEADER3)
                    .switchTo().frame(0)
                    .findElement(SiteBasePage.description)
                    .findElement(By.xpath("./h3[contains(text(),'" + settings.getDescription() + "')]"));
            logger.info("Тест использования HEADER3 контента при настройке description магазина");
        } finally {
            page.switchTo().defaultContent();
        }
    }

    @Test
    public void setShopSettingsWithHeader4Content() throws Exception {
        try {
            page.setShopSettings(settings, AdminBasePage.Mode.FORMATTING, AdminBasePage.Formatting.HEADER4)
                    .switchTo().frame(0);

            page.findElement(SiteBasePage.description)
                    .findElement(By.xpath("./h4[contains(text(),'" + settings.getDescription() + "')]"));
            logger.info("Тест использования HEADER4 контента при настройке description магазина");
        } finally {
            page.switchTo().defaultContent();
        }
    }

    @Ignore("Issue #47")
    @Test
    public void setShopSettingsWithCenterAlignmentContent() throws Exception {
        AdminBasePage.Alignment alignment = AdminBasePage.Alignment.CENTER;
        try {
            page.setShopSettings(settings, AdminBasePage.Mode.ALIGNMENT, AdminBasePage.Alignment.CENTER)
                    .switchTo().frame(0);

            Assert.assertEquals(alignment.getAlign(), page.findElement(SiteBasePage.description)
                    .findElement(By.xpath(".//p[contains(text(),'" + settings.getDescription() + "')]")).getCssValue("text-align"));
            logger.info("Тест использования " + alignment + " контента при настройке description магазина");
        } finally {
            page.switchTo().defaultContent();
        }
    }

    @Ignore("Issue #47")
    @Test
    public void setShopSettingsWithRightAlignmentContent() throws Exception {
        AdminBasePage.Alignment alignment = AdminBasePage.Alignment.RIGHT;
        try {
            page.setShopSettings(settings, AdminBasePage.Mode.ALIGNMENT, AdminBasePage.Alignment.CENTER)
                    .switchTo().frame(0);

            Assert.assertEquals(alignment.getAlign(), page.findElement(SiteBasePage.description)
                    .findElement(By.xpath(".//p[contains(text(),'" + settings.getDescription() + "')]")).getCssValue("text-align"));
            logger.info("Тест использования " + alignment + " контента при настройке description магазина");
        } finally {
            page.switchTo().defaultContent();
        }
    }

    @Ignore("Issue #47")
    @Test
    public void setShopSettingsWithLeftAlignmentContent() throws Exception {
        AdminBasePage.Alignment alignment = AdminBasePage.Alignment.LEFT;
        try {
            page.setShopSettings(settings, AdminBasePage.Mode.ALIGNMENT, AdminBasePage.Alignment.CENTER)
                    .switchTo().frame(0);

            Assert.assertEquals(alignment.getAlign(), page.findElement(SiteBasePage.description)
                    .findElement(By.xpath(".//p[contains(text(),'" + settings.getDescription() + "')]")).getCssValue("text-align"));
            logger.info("Тест использования " + alignment + " контента при настройке description магазина");
        } finally {
            page.switchTo().defaultContent();
        }
    }

    @Ignore("Issue #47")
    @Test
    public void setShopSettingsWithTextJustifiedContent() throws Exception {
        AdminBasePage.Alignment alignment = AdminBasePage.Alignment.JUSTIFY;
        try {
            page.setShopSettings(settings, AdminBasePage.Mode.ALIGNMENT, AdminBasePage.Alignment.CENTER)
                    .switchTo().frame(0);
            Assert.assertEquals(alignment.getAlign(), page.findElement(SiteBasePage.description)
                    .findElement(By.xpath(".//p[contains(text(),'" + settings.getDescription() + "')]")).getCssValue("text-align"));
            logger.info("Тест использования " + alignment + " контента при настройке description магазина");
        } finally {
            page.switchTo().defaultContent();
        }
    }

    @After
    public void back() {
        page.switchTo().defaultContent();
    }

}
