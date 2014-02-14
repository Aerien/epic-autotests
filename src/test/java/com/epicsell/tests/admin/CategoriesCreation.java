package com.epicsell.tests.admin;

import com.epicsell.TestCore;
import com.epicsell.beans.Category;
import com.epicsell.pages.admin.AdminBasePage;
import com.epicsell.pages.admin.Categories;
import com.epicsell.pages.wizard.SiteLogin;
import org.junit.Ignore;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Jane T.
 * Date: 01.06.13
 */
public class CategoriesCreation extends TestCore {
    private List<String> getAllShops() throws Exception {
        String fileName = "C:\\Users\\TJ\\Desktop\\shops.txt";
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        List<String> shops = new ArrayList<String>();
        String line = "";
        while ((line = reader.readLine()) != null) {
            shops.add(line);
        }
        return shops;
    }

    @Ignore
    @Test
    public void pagesCreationForLoadTesting() throws Exception {
        String valid_email = TestCore.testProperties.getProperty("email");
        String valid_password = TestCore.testProperties.getProperty("password");
        List<String> shops = getAllShops();
        for (String shop : shops) {
            Categories categories = new Categories(getWebDriver());
            categories.navigate().to(shop + "/admin");
            categories = ((Categories) new SiteLogin(categories).login(valid_email, valid_password).openMenuItem(AdminBasePage.MenuItem.CATEGORIES));
            for (int i = 0; i < 10; i++) {
                Category category1 = new Category();
                category1.setName("cat" + System.currentTimeMillis());
                category1.setActivity(true);
                categories.createCategory(category1).waitForPopup();
                System.out.println(shop + "/category/" + category1.getName());
            }
            System.out.println("-----------------------------------------------------------------------------------");
        }
        logger.info("Тест создания статической страницы с корерктным названием пройден успешно");
    }

}
