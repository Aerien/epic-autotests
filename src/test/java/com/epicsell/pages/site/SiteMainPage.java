package com.epicsell.pages.site;

import com.epicsell.beans.Link;
import com.epicsell.beans.Settings;
import com.epicsell.beans.Shop;
import com.epicsell.pages.admin.AdminBasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: Jane T.
 * Date: 07.05.13
 */
public class SiteMainPage extends SiteBasePage {
    public static final By product_card = By.xpath("//ul[contains(@class,'products')]/li");
    public static final By product_name = By.xpath(".//div[contains(@class,'-name')]");

    public static final By name_input = By.xpath("//input[@data-settings='core.siteName']");
    public static final By phone_input = By.xpath("//input[@data-settings='core.sitePhone']");
    public static final By description_input = By.xpath("//div[@class='redactor_box']//div[contains(@class,'redactor_editor')]");
    public static final By email_input = By.xpath("//input[@data-settings='core.siteEmail']");
    public static final By vk_input = By.xpath("//input[@data-settings='core.socialVk']");
    public static final By facebook_input = By.xpath("//input[@data-settings='core.socialFb']");
    public static final By twitter_input = By.xpath("//input[@data-settings='core.socialTwitter']");
    public static final By instagram_input = By.xpath("//input[@data-settings='core.socialInstagram']");
    public static final By gaCode_input = By.xpath("//textarea[@data-settings='core.counterGA']");
    public static final By gaCode_link = By.xpath("//div[@data-settings-title='counterGA']");
    public static final By customCode_link = By.xpath("//div[@data-settings-title='customJS']");
    public static final By ymCode_link = By.xpath("//div[@data-settings-title='counterYM']");
    public static final By ymCode_input = By.xpath("//textarea[@data-settings='core.counterYM']");
    public static final By customCode_input = By.xpath("//textarea[@data-settings='core.customJS']");
    public static final By tooltip = By.xpath("//div[@class='tooltip-inner']");

    public enum MenuItem {
        SHOP_STRUCTURE(By.xpath("//div[@data-tb-menu='structure']")),
        COLORS(By.xpath("//div[@data-tb-menu='colors']")),
        FONTS(By.xpath("//div[@data-tb-menu='fonts']")),
        IMAGES(By.xpath("//div[@data-tb-menu='files']")),
        INFO(By.xpath("//div[@data-tb-menu='info']")),
        DOMAIN(By.xpath("//div[@class='tb-menu']/a")),
        BACK(By.xpath("//div[contains(@class,'-fade')]//div[@data-tb-menu='back']"));
        By locator;

        MenuItem(By locator) {
            this.locator = locator;
        }

        public By getLocator() {
            return this.locator;
        }
    }

    public SiteMainPage(WebDriver webDriver) {
        super(webDriver);
    }

    public ProductPage openProduct() {
        return openProduct(0);
    }

    public ProductPage openProduct(Integer i) {
        open(findElements(product_card).get(i).findElement(By.xpath(".//a")).getAttribute("href"));
        return new ProductPage(this);
    }

    public List<WebElement> getProductCards(String name) {
        return findElements(By.xpath("//a[descendant::h3[contains(text(),'" + name + "')]]"));
    }

    public Boolean isMainSitePage(final Shop shop) {
        return waiter().until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                return webDriver.getCurrentUrl().contains(shop.getShopUrl());
            }
        });
    }

    public List<String> getCategoriesList() {
        List<WebElement> siteCategoriesList = findElement(By.id("categories")).findElements(By.xpath(".//li/a"));
        List<String> siteCategoriesNames = new ArrayList<String>();
        for (WebElement cat : siteCategoriesList) {
            siteCategoriesNames.add(cat.getText());
        }
        return siteCategoriesNames;
    }

    public SiteMainPage openMenuItem(MenuItem info) {
        if (isElementPresent(MenuItem.BACK.getLocator())) {
            findElement(MenuItem.BACK.getLocator()).click();
        }
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            //
        }
        findElement(info.getLocator()).findElement(By.xpath("./span")).click();
        waiter().until(ExpectedConditions.presenceOfElementLocated(MenuItem.BACK.getLocator()));
        return this;
    }

    public SiteMainPage setShopSettings(com.epicsell.beans.Settings settings) {
        return setShopSettings(settings, null, null, null, null);
    }

    public SiteMainPage setShopSettings(com.epicsell.beans.Settings settings, AdminBasePage.Mode mode) {
        return setShopSettings(settings, null, null, mode, null);
    }

    public SiteMainPage setShopSettings(com.epicsell.beans.Settings settings, AdminBasePage.Mode mode, Link link) {
        return setShopSettings(settings, null, null, mode, link);
    }

    public SiteMainPage setShopSettings(com.epicsell.beans.Settings settings, AdminBasePage.Mode mode, AdminBasePage.Formatting formatting) {
        return setShopSettings(settings, formatting, null, mode, null);
    }

    public SiteMainPage setShopSettings(com.epicsell.beans.Settings settings, AdminBasePage.Mode mode, AdminBasePage.Alignment alignment) {
        return setShopSettings(settings, null, alignment, mode, null);
    }

    public SiteMainPage setShopSettings(Settings settings,
                                        AdminBasePage.Formatting formatting,
                                        AdminBasePage.Alignment alignment,
                                        AdminBasePage.Mode mode,
                                        Link link) {
        waiter().until(ExpectedConditions.visibilityOfElementLocated(name_input));
        if (settings != null) {
            if (settings.getName() != null) {
                WebElement input = findElement(name_input);
                input.sendKeys(Keys.CONTROL, "a");
                input.sendKeys(Keys.BACK_SPACE);
                input.sendKeys(settings.getName());
                findElement(phone_input).click();
                loader();
            }
            if (settings.getPhone() != null) {
                WebElement input = findElement(phone_input);
                input.clear();
                input.sendKeys(settings.getPhone());
                findElement(name_input).click();
                loader();
            }
            if (settings.getDescription() != null) {
                WebElement contentInput = findElement(description_input);
                contentInput.sendKeys(Keys.CONTROL, "a");
                contentInput.sendKeys(" ");
                if (mode != null) {
                    if (link != null) {
                        editContent(contentInput, settings.getDescription(), mode, link);
                    } else if (formatting != null) {
                        editContent(contentInput, settings.getDescription(), mode, formatting);
                        try {
                            TimeUnit.SECONDS.sleep(1);
                        } catch (InterruptedException e) {
                            //
                        }
                        contentInput.click();
                    } else if (alignment != null) {
                        editContent(contentInput, settings.getDescription(), mode, alignment);
                    } else {
                        editContent(contentInput, settings.getDescription(), mode);
                    }
                } else {
                    contentInput.sendKeys(settings.getDescription());
                }
                findElement(name_input).click();
                loader();
            }
            if (settings.getEmail() != null) {
                WebElement input = findElement(email_input);
                input.clear();
                input.sendKeys(settings.getEmail());
                findElement(name_input).click();
                loader();
            }
            if (settings.getVk() != null) {
                WebElement input = findElement(vk_input);
                input.clear();
                input.sendKeys(settings.getVk());
                findElement(name_input).click();
                loader();
            }
            if (settings.getFacebook() != null) {
                WebElement input = findElement(facebook_input);
                input.clear();
                input.sendKeys(settings.getFacebook());
                findElement(name_input).click();
                loader();
            }
            if (settings.getTwitter() != null) {
                WebElement input = findElement(twitter_input);
                input.clear();
                input.sendKeys(settings.getTwitter());
                findElement(name_input).click();
                loader();
            }
            if (settings.getInstagram() != null) {
                WebElement input = findElement(instagram_input);
                input.clear();
                input.sendKeys(settings.getInstagram());
                findElement(name_input).click();
                loader();
            }
            if (settings.getGaCode() != null) {
                findElement(gaCode_link).click();
                WebElement input = findElement(gaCode_input);
                input.clear();
                input.sendKeys(settings.getGaCode());
                findElement(name_input).click();
                loader();
            }
            if (settings.getYmCode() != null) {
                findElement(ymCode_link).click();
                WebElement input = findElement(ymCode_input);
                input.clear();
                input.sendKeys(settings.getYmCode());
                findElement(name_input).click();
                loader();
            }
            if (settings.getCustomJSCode() != null) {
                findElement(customCode_link).click();
                WebElement input = findElement(customCode_input);
                input.clear();
                input.sendKeys(settings.getYmCode());
                findElement(name_input).click();
                loader();
            }
        }
        loader();
        return this;
    }
}
