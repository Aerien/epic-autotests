package com.epicsell.pages.admin;

import com.epicsell.TestCore;
import com.epicsell.beans.Link;
import com.epicsell.beans.Page;
import com.epicsell.links.AdminLinks;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: Jane T.
 * Date: 07.05.13
 */
@SuppressWarnings("unused")
public class Journal extends AdminBasePage {
    public static final By create_page = By.xpath("//a[@id='yw0']");
    public static final By page_creation_form = By.xpath("//form[@id='Page']");
    public static final By page_name_input = By.xpath("//input[@name='Page[title]']");
    public static final By content_text_area_html_mode = By.xpath("//textarea[@id='Page_full_description' or @data-settings='core.siteDescription']");
    public static final By page_status_hidden_select = By.xpath("//select[@id='Page_status']");
    public static final By create_page_button = By.xpath("//button[@id='yw1']");
    public static final By cancel_without_changes_button = By.xpath("//button[@id='yw2']");
    public static final By created_page_content = By.xpath("//div[@class='es-page']");

    public Journal(WebDriver webDriver) {
        super(webDriver);
    }

    public Journal createPage(Page page) {
        return createPage(page, null, null, null, null);
    }

    public Journal createPage(Page page, AdminBasePage.Mode mode) {
        return createPage(page, null, null, mode, null);
    }

    public Journal createPage(Page page, AdminBasePage.Mode mode, Link link) {
        return createPage(page, null, null, mode, link);
    }

    public Journal createPage(Page page, AdminBasePage.Mode mode, AdminBasePage.Formatting formatting) {
        return createPage(page, formatting, null, mode, null);
    }

    public Journal createPage(Page page, AdminBasePage.Mode mode, AdminBasePage.Alignment alignment) {
        return createPage(page, null, alignment, mode, null);
    }

    public Journal createPage(Page page,
                              AdminBasePage.Formatting formatting,
                              AdminBasePage.Alignment alignment,
                              AdminBasePage.Mode mode,
                              Link link) {
        if (!getCurrentUrl().contains(AdminLinks.ADMIN_PAGES.$())) {
            open(TestCore.test_shop.getShopUrl() + AdminLinks.ADMIN_PAGES.$());
        }
        waiter().until(ExpectedConditions.presenceOfElementLocated(create_page)).click();
        waiter().until(ExpectedConditions.visibilityOfElementLocated(page_creation_form));
        if (page.getName() != null) {
            WebElement input = findElement(page_name_input);
            input.sendKeys(page.getName());
            if (page.getName().length() > 255) {
                Assert.assertEquals(255, input.getAttribute("value").length());
            }
        }
        WebElement contentInput = findElement(AdminBasePage.content_text_area);
        if (mode != null) {
            if (link != null) {
                editContent(contentInput, page.getContent(), mode, link);
            } else if (formatting != null) {
                editContent(contentInput, page.getContent(), mode, formatting);
            } else if (alignment != null) {
                editContent(contentInput, page.getContent(), mode, alignment);
            } else {
                editContent(contentInput, page.getContent(), mode);
            }
        } else {
            contentInput.sendKeys(page.getContent());
        }
        if (page.getActivity() != null) {
            String value = page.getActivity() ? "published" : "draft";
            new Select(findElement(page_status_hidden_select)).selectByValue(value);
        }
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            //
        }
        findElement(create_page_button).click();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            //
        }
        return this;
    }

    public Journal editPage(Page page) {
        if (!getCurrentUrl().contains(AdminLinks.ADMIN_PAGES.$())) {
            get(TestCore.test_shop.getShopUrl() + AdminLinks.ADMIN_PAGES.$());
        }
        findElement(By.xpath("//a[text()='" + page.getName() + "']")).click();
        waiter().until(ExpectedConditions.visibilityOfElementLocated(page_creation_form));
        if (page.getName() != null) {
            WebElement input = findElement(page_name_input);
            input.sendKeys(Keys.CONTROL, "a");
            input.sendKeys(Keys.BACK_SPACE);
            input.sendKeys(page.getName());
            if (page.getName().length() > 255) {
                Assert.assertEquals(255, input.getAttribute("value").length());
            }
        }
        if (page.getContent() != null) {
            WebElement contentInput = findElement(AdminBasePage.content_text_area);
            contentInput.sendKeys(Keys.CONTROL, "a");
            contentInput.sendKeys(Keys.BACK_SPACE);
            contentInput.sendKeys(page.getContent());
        }
        if (page.getActivity() != null) {
            By change_status_link = By.xpath("//div[@id='s2id_Page_status']/a");
            By change_status_input = By.xpath("//div[@class='select2-search']//input");
            findElement(change_status_link).click();
            WebElement status_input = waiter().until(ExpectedConditions.presenceOfElementLocated(change_status_input));
            status_input.sendKeys(page.getActivity() ? "оп" : "че");
            status_input.sendKeys(Keys.RETURN);
        }
        findElement(create_page_button).click();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            //
        }
        return this;
    }

    public Journal savePage() {
        findElement(Journal.create_page_button).click();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            //
        }
        return this;
    }

    public Journal deletePage(Page page) {
        WebElement pageElement = findElement(By.xpath("//tr[descendant::a[contains(text(),'" + page.getName() + "')]]"));
        getMouse().mouseMove(((Locatable) pageElement).getCoordinates());
        pageElement.findElement(By.xpath(".//a[@class='delete']")).click();
        waitForAlert(1).accept();
        waiter().until(ExpectedConditions.stalenessOf(pageElement));
        return this;
    }
}
