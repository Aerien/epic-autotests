package com.epicsell.tests.user_account;

import com.epicsell.TestCore;
import com.epicsell.beans.Shop;
import com.epicsell.beans.TestCategory;
import com.epicsell.pages.wizard.MainPage;
import com.epicsell.utils.GmailInbox;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * User: tabachnaya
 * Date: 04.02.13
 */
@Category({TestCategory.Registration.class, TestCategory.Active.class})
public class ShopCreation extends TestCore {
    private static final Logger logger = Logger.getLogger(ShopCreation.class);
    private static MainPage mainPage;
    private static Shop shop = new Shop();
    String valid_email = TestCore.testProperties.getProperty("email");
    String valid_password = TestCore.testProperties.getProperty("password");

    @Before
    public void setUrl() {
        mainPage = new MainPage(getWebDriver());
        shop.setCity("Kharkiv");
        shop.setCountry("Украина");
        shop.setClientName("team city");
        shop.setActivityType("Электроника");
    }

    @Ignore()
    @Test
    public void shops50forLoadTesting() throws Exception {
        for (int i = 0; i < 41; i++) {
            mainPage.navigate().to(TestCore.testProperties.getProperty("url"));
            shop.setEmail(valid_email);
            shop.generateShopName();
            shop.setPassword(valid_password);
            mainPage.createShop(shop)
                    .enterShop()
                    .setBaseClientInfo(shop);
            System.out.println(shop.getShopUrl());
        }
        logger.info("Тест создания интернет-магазина пройден успешно");
    }

    @Test
    public void registrationWithCorrectEmail() throws Exception {
        shop.setEmail(valid_email);
        shop.generateShopName();
        shop.setPassword(valid_password);
        GmailInbox inbox = new GmailInbox(shop);
        inbox.archiveInbox();
        assertTrue(mainPage.createShop(shop)
                .enterShop()
                .setBaseClientInfo(shop)
                .isMainSitePage(shop));
        assertEquals(shop.getShopUrl(), inbox.getAdminLinkAfterRegistration());
        logger.info("Тест создания интернет-магазина пройден успешно");
    }

    @Test
    public void registrationNameLengthValidator() throws Exception {
        shop.setEmail(valid_email);
        String name = "longlonglonglonglonglonglonglonglonglonglonglonglonglonglonglong" +
                "longlonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglon.test";
        shop.setShopName(name);
        shop.setPassword(valid_password);
        mainPage.fillShopRegistrationForm(shop);
//        assertEquals(61, mainPage.findElement(MainPage.alias_input).getAttribute("value").length());
        logger.info("Тест валидатора длины названия магазина пройден успешно");
    }

    @Test
    public void registrationEmptyNameValidator() throws Exception {
        shop.setEmail(valid_email);
        String name = " ";
        shop.setShopName(name);
        shop.setPassword(valid_password);
        mainPage.createShop(shop);
        assertEquals("Вы не указали название вашего магазина", getValidationMessages().get(0));
        logger.info("Тест валидатора обязательного поля названия магазина пройден успешно");
    }

    @Test
    public void registrationEmptyEmailValidator() throws Exception {
        shop.setEmail("");
        shop.generateShopName("");
        shop.setPassword(valid_password);
        mainPage.createShop(shop);
        assertEquals("Вы не указали E-mail", getValidationMessages().get(0));
        logger.info("Тест валидатора обязательного поля email пройден успешно");
    }

    @Test
    public void registrationIncorrectEmailValidator() throws Exception {
        shop.setEmail("test");
        shop.generateShopName("");
        shop.setPassword(valid_password);
        mainPage.createShop(shop);
        assertEquals("E-mail не является правильным E-Mail адресом.", getValidationMessages().get(0));
        logger.info("Тест валидатора email пройден успешно");
    }

    @Test
    public void registrationEmptyPasswordValidator() throws Exception {
        shop.setEmail(valid_email);
        shop.generateShopName("");
        shop.setPassword("");
        mainPage.createShop(shop);
        assertEquals("Вы не указали Пароль", getValidationMessages().get(0));
        logger.info("Тест валидатора обязательного поля пароля пройден успешно");
    }


    @Test
    public void registrationEnSymbolsValidator() throws Exception {
        shop.setEmail(valid_email);
        shop.setShopName("test,test.test");
        shop.setPassword(valid_password);
        mainPage.createShop(shop);
        assertEquals("Только английские буквы и цифры (только в формате *.test.epicsell.ru)", getValidationMessages().get(0));
        logger.info("Тест валидатора имени магазина с запятыми пройден успешно");
    }

    @Test
    public void registrationRUSymbolsValidator() throws Exception {
        shop.setEmail(valid_email);
        shop.setShopName("тест.test");
        shop.setPassword(valid_password);
        mainPage.createShop(shop);
        assertEquals("Только английские буквы и цифры (только в формате *.test.epicsell.ru)", getValidationMessages().get(0));
        logger.info("Тест валидатора имени магазина с русскими символами пройден успешно");
    }

    @Test
    public void registrationSpecialSymbolsValidator() throws Exception {
        shop.setEmail(valid_email);
        shop.generateShopName("t-");
        shop.setPassword(valid_password);
        assertTrue(mainPage.createShop(shop)
                .enterShop()
                .setBaseClientInfo(shop)
                .isMainSitePage(shop));
        logger.info("Тест валидатора имени магазина со знаком \"-\" пройден успешно");
    }

    @Test
    public void registrationSpecialSymbolValidator() throws Exception {
        shop.setEmail(valid_email);
        shop.generateShopName("t_");
        shop.setPassword(valid_password);
        assertTrue(mainPage.createShop(shop)
                .enterShop()
                .setBaseClientInfo(shop)
                .isMainSitePage(shop));
        logger.info("Тест валидатора имени магазина со знаком \"-\" пройден успешно");
    }

    private List<String> getValidationMessages() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            //
        }
        By messageElement = By.xpath("//div[@class='tooltip-inner']");
        List<WebElement> messages = mainPage.waiter()
                .until(ExpectedConditions.presenceOfAllElementsLocatedBy(messageElement));
        List<String> messageContents = new ArrayList<String>();
        for (WebElement message : messages) {
            messageContents.add(message.getText());
        }
        return messageContents;
    }
}
