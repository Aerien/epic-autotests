package com.epicsell.tests.user_account;

import com.epicsell.TestCore;
import com.epicsell.beans.Shop;
import com.epicsell.beans.TestCategory;
import com.epicsell.pages.admin.AdminBasePage;
import com.epicsell.pages.wizard.MainLogin;
import com.epicsell.pages.wizard.MainPage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * Created with IntelliJ IDEA.
 * User: Jane T.
 * Date: 08.05.13
 */
@Category({TestCategory.Authorization.class, TestCategory.Active.class})
public class MainPageAuthorization extends TestCore {
    MainPage page;
    Shop shop;

    @Before
    public void init() {
        page = new MainPage(getWebDriver());
        shop = new Shop();
        shop.setEmail(test_shop.getEmail());
        shop.setPassword(test_shop.getPassword());
        shop.setShopName(test_shop.getShopName());
    }

    @Test
    public void validDataAuthorization() {
        page.clickLoginButton()
                .login(shop)
                .findElement(AdminBasePage.logout_menu_link);
    }

    @Test
    public void invalidEmailAuthorization() {
        shop.setEmail("invalid email");
        Assert.assertEquals("E-mail не является правильным E-Mail адресом.", page.clickLoginButton()
                .login(shop).findElement(MainLogin.errors).getAttribute("textContent").trim());
    }

    @Test
    public void incorrectEmailAuthorization() {
        shop.setEmail(shop.getEmail() + "u");
        Assert.assertEquals("Неправильный логин или пароль!", page.clickLoginButton()
                .login(shop).findElement(MainLogin.errors).getAttribute("textContent").trim());
    }

    @Test
    public void invalidPasswordAuthorization() {
        shop.setPassword("*");
        Assert.assertEquals("Неправильный логин или пароль!", page.clickLoginButton()
                .login(shop).findElement(MainLogin.errors).getAttribute("textContent").trim());
    }

//    @Test
//    public void invalidShopNameAuthorization() {
//        shop.setShopName("invalid_name");
//        Assert.assertEquals("Только английские буквы и цифры!", page.clickLoginButton()
//                .login(shop).findElement(MainLogin.errors).getAttribute("textContent").trim());
//    }

    @Test
    public void emptyPasswordAuthorization() {
        shop.setPassword("");
        Assert.assertEquals("Вы не указали Пароль", page.clickLoginButton()
                .login(shop).findElement(MainLogin.errors).getAttribute("textContent").trim());
    }

//    @Test
//    public void emptyShopNameAuthorization() {
//        shop.setShopName("");
//        Assert.assertEquals("Вы не указали название вашего магазина", page.clickLoginButton()
//                .login(shop).findElement(MainLogin.errors).getAttribute("textContent").trim());
//    }

    @Test
    public void emptyEmailAuthorization() {
        shop.setEmail("");
        Assert.assertEquals("Вы не указали E-mail", page.clickLoginButton()
                .login(shop).findElement(MainLogin.errors).getAttribute("textContent").trim());
    }

//    @Test
//    public void shortShopNameAuthorization() {
//        shop.setShopName("s");
//        Assert.assertEquals("Слишком короткое название магазина.", page.clickLoginButton()
//                .login(shop).findElement(MainLogin.errors).getAttribute("textContent").trim());
//    }
}
