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
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.Color;

/**
 * User: Aerien
 * Date: 02.06.13
 */
@org.junit.experimental.categories.Category({TestCategory.Categories.class, TestCategory.Active.class})
public class CategoriesNames extends TestCore {
    private static Categories categories;

    @Before
    public void setUrl() {
        categories = (Categories) new SiteLogin(getWebDriver()).login(test_shop).openMenuItem(AdminBasePage.MenuItem.CATEGORIES);
    }

    @Test
    public void createCategoryWithSimpleName() {
        final String expectedName = "category" + System.currentTimeMillis();
        Category category = new Category();
        category.setName(expectedName);
        category.setActivity(true);

        categories.createCategory(category);
        categories.previewCategory(category);
        assertTrue(categories.getCurrentUrl().contains(expectedName));
        categories.switchTo().frame(0);
        assertEquals(expectedName.toUpperCase(), categories.findElement(By.tagName("h1")).getText());
    }

    @Test
    public void specialSymbolsInTheName() {
        final String expectedName = System.currentTimeMillis() + "!!!";
        Category category = new Category();
        category.setName(expectedName);
        category.setActivity(true);

        categories.createCategory(category);
        categories.previewCategory(category);
        categories.switchTo().frame(0);
        assertEquals(expectedName.toUpperCase(), categories.findElement(By.tagName("h1")).getText());
    }

    @Test
    public void createCategoryWithDuplicatedName() {
        final String expectedName = "category" + System.currentTimeMillis();
        Category category = new Category();
        category.setName(expectedName);
        category.setActivity(true);

        categories.createCategory(category)
                .waitForPopup();
        try {
            categories.createCategory(category);
        } catch (TimeoutException te) {
        }
        assertEquals(Color.fromString("#B94A48").asRgba(), categories.findElement(Categories.category_name_input).getCssValue("color"));
    }
}