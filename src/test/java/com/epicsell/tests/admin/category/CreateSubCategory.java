package com.epicsell.tests.admin.category;

import com.epicsell.TestCore;
import com.epicsell.beans.Category;
import com.epicsell.beans.TestCategory;
import com.epicsell.pages.admin.AdminBasePage;
import com.epicsell.pages.admin.Categories;
import com.epicsell.pages.wizard.SiteLogin;
import org.junit.Before;
import org.junit.Test;

@org.junit.experimental.categories.Category({TestCategory.Categories.class, TestCategory.Active.class})
public class CreateSubCategory extends TestCore {
    private static Categories categories;

    @Before
    public void setUrl() {
        categories = (Categories) new SiteLogin(getWebDriver()).login(test_shop).openMenuItem(AdminBasePage.MenuItem.CATEGORIES);
    }

    //    @Ignore("drag&drop problem")
    @Test
    public void testCreateSubCategory() {
        Category category = new Category();
        category.setName("category" + System.currentTimeMillis());
        category.setActivity(true);
        Category sub_category = new Category();
        sub_category.setName("subcategory" + System.currentTimeMillis());
        sub_category.setActivity(true);

        categories.createCategory(category).waitForPopup();
        categories.createCategory(sub_category).waitForPopup();
        categories.moveCategoryAsSubCategory(category, sub_category);
    }
}
