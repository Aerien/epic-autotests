package com.epicsell.tests.user_account;

import com.epicsell.TestCore;
import com.epicsell.beans.Shop;
import com.epicsell.beans.TestCategory;
import com.epicsell.drivers.ExtendedWebDriver;
import com.epicsell.links.AdminLinks;
import com.epicsell.pages.admin.AdminBasePage;
import com.epicsell.pages.site.SiteBasePage;
import com.epicsell.pages.wizard.*;
import com.epicsell.utils.GmailInbox;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Created with IntelliJ IDEA.
 * User: Jane T.
 * Date: 19.05.13
 */
@Category({TestCategory.ForgotPassword.class, TestCategory.Active.class})
public class ForgotPassword extends TestCore {
    private static final Logger logger = Logger.getLogger(ShopCreation.class);
    private static MainPage mainPage;
    private static Shop shop = new Shop();
    String valid_email = TestCore.testProperties.getProperty("email");
    String valid_password = TestCore.testProperties.getProperty("password");
    String newPassword = "123";
    GmailInbox inbox;

    @Before
    public void setUrl() {
        mainPage = new MainPage(getWebDriver());
        inbox = new GmailInbox(valid_email, valid_password);
    }

    @Test
    public void forgotPassword() throws Exception {
        shop.setCity("Kharkiv");
        shop.setCountry("Украина");
        shop.setClientName("team city");
        shop.setActivityType("Электроника");
        shop.setEmail(valid_email);
        shop.generateShopName();
        shop.setPassword(valid_password);
        mainPage.createShop(shop)
                .enterShop()
                .setBaseClientInfo(shop)
                .navigate().to(shop.getShopUrl() + AdminLinks.ADMIN_ORDERS.$());
        new AdminBasePage(mainPage).logout()
                .get(shop.getShopUrl() + AdminLinks.ADMIN_AUTH.$());
        inbox.archiveInbox();
        new SiteLogin(mainPage)
                .forgotPassword()
                .resetPassword(shop.getEmail())
                .get(inbox.getResetPasswordLink());
        new SetNewPassword(mainPage)
                .setPassword(newPassword)
                .login(shop.getEmail(), newPassword, shop)
                .open(shop.getShopUrl())
                .waiter().until(ExpectedConditions.presenceOfAllElementsLocatedBy(SiteBasePage.toolbox_button));
        logger.info("Тест смены пароля пройден успешно");
    }

    @Test
    public void forgotPasswordNotSamePasswords() throws Exception {
        inbox.archiveInbox();
        new SiteLogin(mainPage)
                .forgotPassword(test_shop3)
                .resetPassword(test_shop3.getEmail())
                .get(inbox.getResetPasswordLink());
        new SetNewPassword(mainPage)
                .setPassword(newPassword, test_shop3.getPassword())
                .waiter().until(new ExpectedCondition<ExtendedWebDriver>() {
            @Override
            public ExtendedWebDriver apply(org.openqa.selenium.WebDriver driver) {
                return mainPage.findElement(SetNewPassword.reply_pass_error).getAttribute("textContent")
                        .equals("Еще раз пароль должен быть повторен в точности.") ? mainPage : null;
            }
        });
        logger.info("Тест валидации на идентичность паролей при смене пройден успешно");
    }

    @Test
    public void forgotPasswordEmptyPasswords() throws Exception {
        inbox.archiveInbox();
        new SiteLogin(mainPage)
                .forgotPassword(test_shop3)
                .resetPassword(test_shop3.getEmail())
                .get(inbox.getResetPasswordLink());
        new SetNewPassword(mainPage)
                .setPassword("", "")
                .waiter().until(new ExpectedCondition<ExtendedWebDriver>() {
            @Override
            public ExtendedWebDriver apply(org.openqa.selenium.WebDriver driver) {
                return mainPage.findElement(SetNewPassword.pass_error).getAttribute("textContent")
                        .equals("Необходимо заполнить поле Новый пароль.") ? mainPage : null;
            }
        }).waiter().until(new ExpectedCondition<ExtendedWebDriver>() {
            @Override
            public ExtendedWebDriver apply(org.openqa.selenium.WebDriver driver) {
                return mainPage.findElement(SetNewPassword.reply_pass_error).getAttribute("textContent")
                        .equals("Необходимо заполнить поле Еще раз пароль.") ? mainPage : null;
            }
        });
        logger.info("Тест валидации на обязательное заполнение паролей при смене пройден успешно");
    }

    @Test
    public void emptyEmail() throws Exception {
        new SiteLogin(mainPage)
                .forgotPassword(test_shop3)
                .resetPassword("")
                .waiter().until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(org.openqa.selenium.WebDriver driver) {
                return mainPage.findElement(RemindPassword.error).getAttribute("textContent")
                        .equals("Ошибка. Пользователь не найден.");
            }
        });
        logger.info("Тест валидации на обязательное заполнение email при сбросе пароля пройден успешно");
    }

    @Test
    public void invalidEmail() throws Exception {
        new SiteLogin(mainPage)
                .forgotPassword(test_shop3)
                .resetPassword("wrong")
                .waiter().until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(org.openqa.selenium.WebDriver driver) {
                return mainPage.findElement(RemindPassword.error).getAttribute("textContent")
                        .equals("E-Mail не является правильным E-Mail адресом.");
            }
        });
        logger.info("Тест валидации на щбязательное заполнение email при сбросе пароля пройден успешно");
    }

    @Test
    public void forgotFromMainPage() throws Exception {
        shop.setCity("Kharkiv");
        shop.setCountry("Украина");
        shop.setClientName("team city");
        shop.setActivityType("Электроника");
        shop.setEmail(valid_email);
        shop.generateShopName();
        shop.setPassword(valid_password);
        inbox.archiveInbox();
        mainPage.createShop(shop)
                .enterShop()
                .setBaseClientInfo(shop)
                .navigate().to(shop.getShopUrl() + AdminLinks.ADMIN_ORDERS.$());
        new AdminBasePage(mainPage).logout()
                .get(testProperties.getProperty("url"));
        inbox.getAdminLinkAfterRegistration();
        inbox.archiveInbox();
        mainPage.clickLoginButton()
                .forgotPassword()
                .resetPassword(shop)
                .get(inbox.getResetPasswordLink());
        new SetNewPassword(mainPage)
                .setPassword(newPassword)
                .login(shop.getEmail(), newPassword, shop)
                .open(shop.getShopUrl())
                .waiter().until(ExpectedConditions.presenceOfAllElementsLocatedBy(SiteBasePage.toolbox_button));
        logger.info("Тест смены пароля с главной страницы пройден успешно");

    }

    @Test
    public void forgotFromMainPageWithEmptyEmail() throws Exception {
        mainPage.clickLoginButton()
                .forgotPassword()
                .resetPassword("")
                .waiter().until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(org.openqa.selenium.WebDriver driver) {
                return mainPage.findElement(MainRemindPassword.error).getAttribute("textContent")
                        .equals("Ошибка. Пользователь не найден.");
            }
        });
        logger.info("Тест валидатора при смене пароля с главной страницы пройден успешно");
    }

    @Test
    public void forgotFromMainPageWithWrongEmail() throws Exception {
        mainPage.clickLoginButton()
                .forgotPassword()
                .resetPassword("wrong")
                .waiter().until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(org.openqa.selenium.WebDriver driver) {
                return mainPage.findElement(MainRemindPassword.error).getAttribute("textContent")
                        .equals("E-Mail не является правильным E-Mail адресом.");
            }
        });
        logger.info("Тест валидатора при смене пароля с главной страницы пройден успешно");
    }
}
