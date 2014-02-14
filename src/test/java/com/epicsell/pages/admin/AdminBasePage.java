package com.epicsell.pages.admin;

import com.epicsell.drivers.ExtendedWebDriver;
import com.epicsell.links.AdminLinks;
import com.epicsell.pages.site.SiteMainPage;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: Jane T.
 * Date: 07.05.13
 */
public class AdminBasePage extends ExtendedWebDriver {
    public ExtendedWebDriver waitForPopup() {
        try {
            waiter().until(ExpectedConditions.visibilityOfElementLocated(popup)).click();
            waiter().until(ExpectedConditions.invisibilityOfElementLocated(popup));
        } catch (TimeoutException te) {
            //
        }
        return this;
    }

    public AdminBasePage(WebDriver webDriver) {
        super(webDriver);
    }

    public enum Mode {
        HTML, BOLD, ITALIC, VIDEO, IMAGE, LINK, HORIZONTAL_RULE, FONT_COLOR, FORMATTING, BG_COLOR, ALIGNMENT
    }

    public enum Formatting {
        PARAGRAPH("Paragraph", "Обычный текст"),
        QUOTE("Quote", "Цитата"),
        CODE("Code", "Код"),
        HEADER2("Header 2", "Заголовок 2"),
        HEADER3("Header 3", "Заголовок 3"),
        HEADER4("Header 4", "Заголовок 4");
        String linkText;
        String linkText1;

        Formatting(String linkText, String linkText1) {
            this.linkText = linkText;
            this.linkText1 = linkText1;
        }

        public By getLink() {
            return By.xpath(".//a[text()='" + linkText + "' or text()='" + linkText1 + "']");
        }
    }

    public enum Alignment {
        LEFT("Align text to the left", "left"),
        CENTER("Center text", "center"),
        RIGHT("Align text to the right", "right"),
        JUSTIFY("Justify text", "justify");
        String linkText;

        public String getAlign() {
            return align;
        }

        String align;

        Alignment(String linkText, String align) {
            this.linkText = linkText;
            this.align = align;
        }

        public By getLink() {
            return By.xpath(".//a[text()='" + linkText + "']");
        }
    }

    public SiteMainPage logout(String site) {
        if (!getCurrentUrl().contains(AdminLinks.ADMIN_AUTH.$())) {
            navigate().to("http://" + site + ".epicsell.ru" + AdminLinks.ADMIN_ORDERS.$());
            logout();
        }
        return new SiteMainPage(this);
    }

    public SiteMainPage logout() {
        By logout_link = By.xpath("//li[contains(@class,'logout')]//a");
        waiter().until(ExpectedConditions.visibilityOfElementLocated(logout_link)).click();
        waiter().until(new ExpectedCondition<Object>() {
            @Override
            public Object apply(org.openqa.selenium.WebDriver driver) {
                return !getCurrentUrl().contains("/admin");
            }
        });
        return new SiteMainPage(this);
    }

    public enum MenuItem {
        ORDERS, CATALOG, CATEGORIES, PAGES, SETTINGS
    }

    public AdminBasePage openMenuItem(MenuItem item) {
        switch (item) {
            case ORDERS:
                waiter().until(ExpectedConditions.presenceOfElementLocated(orders_menu_item)).click();
                return new Orders(this);
            case CATALOG:
                waiter().until(ExpectedConditions.presenceOfElementLocated(catalog_menu_item)).click();
                return new Catalog(this);
            case CATEGORIES:
                waiter().until(ExpectedConditions.presenceOfElementLocated(categories_menu_item)).click();
                return new Categories(this);
            case PAGES:
                waiter().until(ExpectedConditions.presenceOfElementLocated(pages_menu_item)).click();
                return new Journal(this);
            case SETTINGS:
                waiter().until(ExpectedConditions.presenceOfElementLocated(settings_menu_item)).click();
                return new Settings(this);
            default:
                return null;
        }
    }

    public AdminBasePage openEditingForm(String contentName) {
        By content = By.xpath("//a[text()='" + contentName + "']");
        if (!isElementPresent(content)) {
            openLastPage();
        }
        findElement(content).click();
        waiter().until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//form[@id]")));
        return this;
    }

    public AdminBasePage openLastPage() {
        while (!findElement(By.xpath("//ul[@id='yw1']/li[contains(@class,'next')]"))
                .getAttribute("class").contains("disabled")) {
            findElement(By.xpath("//ul[@id='yw1']/li[last()-1]//a")).click();
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                //
            }
        }
        return this;
    }

    public AdminBasePage setTextContentStyle(AdminBasePage.Mode mode) {
        WebElement contentInput = findElement(content_text_area);
        contentInput.sendKeys(Keys.CONTROL, "a");
        switch (mode) {
            case BOLD:
                findElement(bold_mode_link).click();
                break;
            case ITALIC:
                findElement(italic_mode_link).click();
                break;
        }
        return this;
    }

    public AdminBasePage setTextContentStyle(AdminBasePage.Mode mode, String color) {
        return setTextContentStyle(findElement(content_text_area), mode, color);
    }

    public AdminBasePage setTextContentStyle(WebElement contentInput, AdminBasePage.Mode mode, String color) {
        findElement(full_screen_mode_link).click();
        loader();
        loader();
        contentInput = findElement(By.className("redactor_editor"));
        contentInput.click();
        contentInput.sendKeys(Keys.CONTROL, "a");
        switch (mode) {
            case BG_COLOR:
                if (color == null) {
                    Assert.fail("Color must not be null");
                }
                findElement(bg_color_mode_link).click();
                loader();
                WebElement color_palete = waiter().until(ExpectedConditions.visibilityOfElementLocated(drop_down_menu));
                color_palete.findElement(By.xpath(".//a[@rel='" + color + "']")).click();
                break;
            case FONT_COLOR:
                if (color == null) {
                    Assert.fail("Color must not be null");
                }
                findElement(font_color_mode_link).click();
                loader();
                WebElement palete = waiter().until(ExpectedConditions.visibilityOfElementLocated(drop_down_menu));
                palete.findElement(By.xpath(".//a[@rel='" + color + "']")).click();
                break;
        }
        loader();
        findElement(full_screen_mode_link).click();
        loader();
        return this;
    }
}
