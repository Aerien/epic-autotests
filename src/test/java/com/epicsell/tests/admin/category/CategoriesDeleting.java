package com.epicsell.tests.admin.category;

import com.epicsell.TestCore;
import com.epicsell.beans.Category;
import com.epicsell.beans.TestCategory;
import com.epicsell.pages.admin.AdminBasePage;
import com.epicsell.pages.admin.Categories;
import com.epicsell.pages.wizard.SiteLogin;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

/**
 * User: Aerien
 * Date: 02.06.13
 */
@org.junit.experimental.categories.Category({TestCategory.Categories.class, TestCategory.Active.class})
public class CategoriesDeleting extends TestCore {
    private static Categories categories;

    @Before
    public void setUrl() {
        categories = (Categories) new SiteLogin(getWebDriver()).login(test_shop).openMenuItem(AdminBasePage.MenuItem.CATEGORIES);
    }

    @Test
    public void deleteOneCategory() {
        final String name = "category" + System.currentTimeMillis();
        Category category = new Category();
        category.setName(name);

        categories.createCategory(category)
                .deleteCategory(category);

        assertNull("Категория не удалена", categories.getCategoryElement(category));
    }

    @Test
    public void deleteOneCategoryByGroupDeleting() {
        final String name = "category" + System.currentTimeMillis();
        Category category = new Category();
        category.setName(name);
        ArrayList<Category> categoriesList = new ArrayList<Category>();
        categoriesList.add(category);

        categories.createCategory(category)
                .deleteGroupOfCategories(categoriesList);

        for (Category i : categoriesList) {
            assertNull("Категория не удалена", categories.getCategoryElement(i));
        }
    }

    @Test
    public void deleteSeveralCategories() {
        final String name = "category" + System.currentTimeMillis();
        Category firstCategory = new Category();
        Category secondCategory = new Category();
        firstCategory.setName(name);
        secondCategory.setName(name + "x");
        ArrayList<Category> categoriesList = new ArrayList<Category>();
        categoriesList.add(firstCategory);
        categoriesList.add(secondCategory);

        categories.createCategory(categoriesList)
                .deleteGroupOfCategories(categoriesList);

        for (Category i : categoriesList) {
            assertNull("Категория не удалена", categories.getCategoryElement(i));
        }
    }

    //    @Ignore("drag&drop problem")
    @Test
    public void deleteOneSubCategory() {
        final String name = "category" + System.currentTimeMillis();
        Category parentCategory = new Category();
        Category subCategory = new Category();
        parentCategory.setName(name);
        subCategory.setName(name + "-sub");
        ArrayList<Category> categoriesList = new ArrayList<Category>();
        categoriesList.add(parentCategory);
        categoriesList.add(subCategory);

        categories.createCategory(categoriesList)
                .moveCategoryAsSubCategory(parentCategory, subCategory)
                .deleteCategory(subCategory);

        assertNull("Категория не удалена", categories.getCategoryElement(subCategory));
    }

    //    @Ignore("drag&drop problem")
    @Test
    public void deleteTwoSubCategory() {
        final String name = "category" + System.currentTimeMillis();
        Category parentCategory = new Category();
        Category firstSubCategory = new Category();
        Category secondSubCategory = new Category();
        parentCategory.setName(name);
        firstSubCategory.setName(name + "-sub1");
        secondSubCategory.setName(name + "-sub2");
        ArrayList<Category> categoriesList = new ArrayList<Category>();
        categoriesList.add(parentCategory);
        categoriesList.add(firstSubCategory);
        categoriesList.add(secondSubCategory);

        categories.createCategory(categoriesList)
                .moveCategoryAsSubCategory(parentCategory, firstSubCategory)
                .moveCategoryAsSubCategory(parentCategory, secondSubCategory)
                .deleteGroupOfCategories(categoriesList.subList(1, 3));

        assertNull("Категория не удалена", categories.getCategoryElement(firstSubCategory));
        assertNull("Категория не удалена", categories.getCategoryElement(secondSubCategory));
    }

    //    @Ignore("drag&drop problem")
    @Test
    public void deleteParentAndChildCategories() {
        final String name = "category" + System.currentTimeMillis();
        Category parentCategory = new Category();
        Category firstSubCategory = new Category();
        parentCategory.setName(name);
        firstSubCategory.setName(name + "-sub");
        ArrayList<Category> categoriesList = new ArrayList<Category>();
        categoriesList.add(parentCategory);
        categoriesList.add(firstSubCategory);

        categories.createCategory(categoriesList)
                .moveCategoryAsSubCategory(parentCategory, firstSubCategory)
                .deleteGroupOfCategories(categoriesList);

        assertNull("Категория не удалена", categories.getCategoryElement(firstSubCategory));
        assertNull("Категория не удалена", categories.getCategoryElement(parentCategory));
    }

    //    @Ignore("drag&drop problem")
    @Test
    public void deleteOneParentCategory() {
        final String name = "category" + System.currentTimeMillis();
        Category parentCategory = new Category();
        Category subCategory = new Category();
        parentCategory.setName(name);
        subCategory.setName(name + "-sub");
        ArrayList<Category> categoriesList = new ArrayList<Category>();
        categoriesList.add(parentCategory);
        categoriesList.add(subCategory);

        categories.createCategory(categoriesList)
                .moveCategoryAsSubCategory(parentCategory, subCategory)
                .deleteCategory(parentCategory);

        assertNull("Категория не удалена", categories.getCategoryElement(subCategory));
        assertNull("Категория не удалена", categories.getCategoryElement(parentCategory));
    }

    @Test
    public void deleteAll() {
        if (categories.getCategoriesCount() == 0) {
            final String name = "category" + System.currentTimeMillis();
            Category category = new Category();
            category.setName(name);
            categories.createCategory(category);
        }
        categories.selectAll()
                .deleteSelectedCategories();
        assertEquals(0L, categories.getCategoriesCount());
        categories.selectAll();
        assertFalse(categories.findElement(Categories.group_delete_button).isDisplayed());
    }
}