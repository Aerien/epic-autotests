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
public class CategoriesSeoTags extends TestCore {
    private static Categories categories;

    @Before
    public void setUrl() {
        categories = (Categories) new SiteLogin(getWebDriver()).login(test_shop).openMenuItem(AdminBasePage.MenuItem.CATEGORIES);
    }

    @Test
    public void createCategoryWithDescription() {
        final String name = "category" + System.currentTimeMillis();
        final String description = "description" + System.currentTimeMillis();
        Category category = new Category();
        category.setName(name);
        category.setDescription(description);
        category.setActivity(true);

        categories.createCategory(category)
                .previewCategory(category)
                .switchTo().frame(0);
        assertEquals(description, categories.findElement(By.xpath("//meta[@name='description']")).getAttribute("content"));
    }

    @Test
    public void createCategoryWithKeyWords() {
        final String expectedName = "category" + System.currentTimeMillis();
        final String keywords = "keyword1, keyword2, " + System.currentTimeMillis();
        Category category = new Category();
        category.setName(expectedName);
        category.setKeywords(keywords);
        category.setActivity(true);

        categories.createCategory(category)
                .previewCategory(category)
                .switchTo().frame(0);
        assertEquals(keywords, categories.findElement(By.xpath("//meta[@name='keywords']")).getAttribute("content"));
    }

    @Test
    public void editCategoryDescription() {
        final String name = "category" + System.currentTimeMillis();
        final String description = "description" + System.currentTimeMillis();
        final String expectedDescription = "edited descr" + System.currentTimeMillis();
        Category category = new Category();
        category.setName(name);
        category.setDescription(description);
        category.setActivity(true);
        Category editedCategory = new Category(category);
        editedCategory.setDescription(expectedDescription);

        ((Categories) categories.createCategory(category)
                .editCategory(category, editedCategory)
                .waitForPopup())
                .previewCategory(editedCategory)
                .switchTo().frame(0);
        assertEquals(expectedDescription, categories.findElement(By.xpath("//meta[@name='description']")).getAttribute("content"));
    }

    @Test
    public void editCategoryKeyWords() {
        final String expectedName = "category" + System.currentTimeMillis();
        final String keywords = "keyword1, keyword2, " + System.currentTimeMillis();
        final String expectedKeywords = "new keywords, " + System.currentTimeMillis();
        Category category = new Category();
        category.setName(expectedName);
        category.setKeywords(keywords);
        category.setActivity(true);
        Category editedCategory = new Category(category);
        editedCategory.setKeywords(expectedKeywords);

        ((Categories) categories.createCategory(category)
                .editCategory(category, editedCategory).waitForPopup())
                .previewCategory(editedCategory)
                .switchTo().frame(0);
        assertEquals(expectedKeywords, categories.findElement(By.xpath("//meta[@name='keywords']")).getAttribute("content"));
    }

    @Test
    public void clearCategoryDescription() {
        final String name = "category" + System.currentTimeMillis();
        final String description = "description" + System.currentTimeMillis();
        final String expectedDescription = "";
        Category category = new Category();
        category.setName(name);
        category.setDescription(description);
        category.setActivity(true);
        Category editedCategory = new Category(category);
        editedCategory.setDescription(expectedDescription);

        categories.createCategory(category)
                .editCategory(category, editedCategory)
                .previewCategory(editedCategory)
                .switchTo().frame(0);
        assertEquals(expectedDescription, categories.findElement(By.xpath("//meta[@name='description']")).getAttribute("content"));
    }

    @Test
    public void clearCategoryKeyWords() {
        final String expectedName = "category" + System.currentTimeMillis();
        final String keywords = "keyword1, keyword2, " + System.currentTimeMillis();
        final String expectedKeywords = "";
        Category category = new Category();
        category.setName(expectedName);
        category.setKeywords(keywords);
        category.setActivity(true);
        Category editedCategory = new Category(category);
        editedCategory.setKeywords(expectedKeywords);

        categories.createCategory(category)
                .editCategory(category, editedCategory)
                .previewCategory(editedCategory)
                .switchTo().frame(0);
        assertEquals(expectedKeywords, categories.findElement(By.xpath("//meta[@name='keywords']")).getAttribute("content"));
    }
}