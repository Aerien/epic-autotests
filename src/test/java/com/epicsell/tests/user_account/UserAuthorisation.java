package com.epicsell.tests.user_account;

import com.epicsell.TestCore;
import com.epicsell.beans.Shop;
import com.epicsell.beans.TestCategory;
import com.epicsell.drivers.ExtendedWebDriver;
import com.epicsell.links.AdminLinks;
import com.epicsell.pages.admin.AdminBasePage;
import com.epicsell.pages.site.SiteBasePage;
import com.epicsell.pages.wizard.MainPage;
import com.epicsell.pages.wizard.SiteLogin;
import org.junit.*;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Created with IntelliJ IDEA.
 * User: Jane T.
 * Date: 03.04.13
 */
@Category({TestCategory.Authorization.class, TestCategory.Active.class})
@RunWith(BlockJUnit4ClassRunner.class)
public class UserAuthorisation extends TestCore {
    private static Shop shop = new Shop();
    private static String valid_email = "epicsell.qa@gmail.com";
    private static String valid_password = "123456";
    private static SiteLogin login;

    @Before
    public void setUrl() {
        login = new SiteLogin(getWebDriver());
    }

    @BeforeClass
    public static void createShop() {
        TestCore.setUp();
        WebDriver webDriver = new FirefoxDriver();
        webDriver.get(TestCore.testProperties.getProperty("url"));
        webDriver.manage().window().maximize();
        TestCore.setWebDriver(new ExtendedWebDriver(webDriver));
        login = new SiteLogin(getWebDriver());
        shop.setEmail(valid_email);
        shop.generateShopName("");
        shop.setPassword(valid_password);
        shop.setCity("Kharkiv");
        shop.setCountry("Украина");
        shop.setClientName("team city");
        shop.setActivityType("Электроника");
        assertTrue(new MainPage(login).createShop(shop)
                .enterShop()
                .setBaseClientInfo(shop)
                .isMainSitePage(shop));
        new AdminBasePage(login).logout(shop.getShopName());
    }

    @Test
    public void authorizationWithValidEmail() {
        shop.setEmail(valid_email);
        shop.setPassword(valid_password);
        login.login(shop)
                .open(shop.getShopUrl())
                .waiter().until(ExpectedConditions.presenceOfAllElementsLocatedBy(SiteBasePage.toolbox_button));
    }

    @Test
    public void authorizationWithInvalidPassword() {
        shop.setEmail(valid_email);
        shop.setPassword("invalid_password");
        Assert.assertEquals(login.login(shop).waiter().until(new ExpectedCondition<String>() {
            @Override
            public String apply(WebDriver webDriver) {
                return login.findElement(SiteLogin.field_error).getAttribute("textContent");
            }
        }), "Неправильное имя пользователя или пароль.");
    }

    @Test
    public void adminWithoutAuthorisation() {
        login.navigate().to(shop.getShopUrl() + AdminLinks.ADMIN_ORDERS.$());
        assertEquals(shop.getShopUrl() + AdminLinks.ADMIN_AUTH.$(), login.getCurrentUrl());
    }

    @After
    public void browserShutDown() {
        try {
            new AdminBasePage(login).logout(shop.getShopName());
        } catch (Exception e) {
            //
        }
    }
}
