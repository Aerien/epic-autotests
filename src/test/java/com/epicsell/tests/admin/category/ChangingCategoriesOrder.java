package com.epicsell.tests.admin.category;

import com.epicsell.TestCore;
import com.epicsell.beans.Category;
import com.epicsell.beans.TestCategory;
import com.epicsell.links.AdminLinks;
import com.epicsell.pages.admin.AdminBasePage;
import com.epicsell.pages.admin.Categories;
import com.epicsell.pages.site.SiteMainPage;
import com.epicsell.pages.wizard.SiteLogin;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: TJ
 * Date: 07.06.13
 * Time: 9:47
 */
@Ignore("problems with drag&drop")
@org.junit.experimental.categories.Category({TestCategory.Categories.class, TestCategory.Active.class})
public class ChangingCategoriesOrder extends TestCore {
    private static Categories categories;

    @Before
    public void setUrl() {
        categories = (Categories) new SiteLogin(getWebDriver()).login(test_shop).openMenuItem(AdminBasePage.MenuItem.CATEGORIES);
    }

    @Test
    public void changeOrder() {
        SiteMainPage siteMainPage = new SiteMainPage(categories);
        Category category = new Category();
        category.setName("category" + System.currentTimeMillis());
        category.setActivity(true);
        Category category1 = new Category();
        category1.setName("subcategory" + System.currentTimeMillis());
        category1.setActivity(true);

        categories.createCategory(category).waitForPopup();
        categories.createCategory(category1).waitForPopup();

        categories.navigate().to(test_shop.getShopUrl());
        categories.switchTo().frame(0);

        List<String> siteCategoriesNames = siteMainPage.getCategoriesList();
        Assert.assertTrue(siteCategoriesNames.indexOf(category.getName()) < siteCategoriesNames.indexOf(category1.getName()));

        categories.navigate().to(test_shop.getShopUrl() + AdminLinks.ADMIN_CATEGORIES.$());
        categories.changePlaces(category, category1);

        categories.navigate().to(test_shop.getShopUrl());
        categories.switchTo().frame(0);
        siteCategoriesNames = siteMainPage.getCategoriesList();
        Assert.assertTrue(siteCategoriesNames.indexOf(category.getName()) > siteCategoriesNames.indexOf(category1.getName()));
    }

}
