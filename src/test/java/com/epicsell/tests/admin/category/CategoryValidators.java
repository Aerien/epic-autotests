package com.epicsell.tests.admin.category;

import com.epicsell.TestCore;
import com.epicsell.beans.Category;
import com.epicsell.beans.TestCategory;
import com.epicsell.pages.admin.AdminBasePage;
import com.epicsell.pages.admin.Categories;
import com.epicsell.pages.wizard.SiteLogin;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

@org.junit.experimental.categories.Category({TestCategory.Categories.class, TestCategory.Active.class})
public class CategoryValidators extends TestCore {
    private static Categories categories;
    Category category = new Category();

    @Before
    public void setUrl() {
        categories = (Categories) new SiteLogin(getWebDriver()).login(test_shop).openMenuItem(AdminBasePage.MenuItem.CATEGORIES);
        category.setName("test" + System.currentTimeMillis());
        category.setDescription("test");
        category.setKeywords("test");
        category.setActivity(true);
    }

    @Test
    public void emptyName() {
        category.setName("");
        categories.createCategory(category);
        Assert.assertEquals("Это поле необходимо заполнить.", categories.getNameValidationMessage());
    }
}
