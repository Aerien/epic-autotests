package com.epicsell.tests.admin.category;

import com.epicsell.TestCore;
import com.epicsell.beans.Category;
import com.epicsell.beans.TestCategory;
import com.epicsell.pages.admin.AdminBasePage;
import com.epicsell.pages.admin.Categories;
import com.epicsell.pages.wizard.SiteLogin;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;

/**
 * User: Aerien
 * Date: 02.06.13
 */
@org.junit.experimental.categories.Category({TestCategory.Categories.class, TestCategory.Active.class})
public class CategoriesActivity extends TestCore {
    private static Categories categories;

    @Before
    public void setUrl() {
        categories = (Categories) new SiteLogin(getWebDriver()).login(test_shop).openMenuItem(AdminBasePage.MenuItem.CATEGORIES);
    }

    @Test
    public void createActiveCategory() {
        final String name = "category" + System.currentTimeMillis();
        Category category = new Category();
        category.setName(name);
        category.setActivity(true);

        categories.createCategory(category)
                .previewCategory(category)
                .switchTo().frame(0);
        assertTrue(categories.isElementPresent(By.xpath("//ul[@id='categories']/li/a[text()='" + name + "']")));
    }

    @Test
    public void createDisabledCategory() {
        final String name = "category" + System.currentTimeMillis();
        Category category = new Category();
        category.setName(name);
        category.setActivity(false);

        categories.createCategory(category)
                .previewCategory(category)
                .switchTo().frame(0);
        assertFalse(categories.isElementPresent(By.xpath("//ul[@id='categories']/li/a[text()='" + name + "']")));
    }

    @Test
    public void editCategoryActivityYN() {
        final String name = "category" + System.currentTimeMillis();
        Category category = new Category();
        category.setName(name);
        category.setActivity(true);
        Category editedCategory = new Category(category);
        editedCategory.setActivity(false);

        categories.createCategory(category)
                .editCategory(category, editedCategory)
                .previewCategory(editedCategory)
                .switchTo().frame(0);
        assertFalse(categories.isElementPresent(By.xpath("//ul[@id='categories']/li/a[text()='" + name + "']")));
    }

    @Test
    public void editCategoryActivityNY() {
        final String name = "category" + System.currentTimeMillis();
        Category category = new Category();
        category.setName(name);
        category.setActivity(false);
        Category editedCategory = new Category(category);
        editedCategory.setActivity(true);

        categories.createCategory(category)
                .editCategory(category, editedCategory)
                .previewCategory(editedCategory)
                .switchTo().frame(0);
        assertTrue(categories.isElementPresent(By.xpath("//ul[@id='categories']/li/a[text()='" + name + "']")));
    }
}