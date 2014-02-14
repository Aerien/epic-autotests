package com.epicsell.tests.admin.category;

import com.epicsell.TestCore;
import com.epicsell.beans.Category;
import com.epicsell.beans.TestCategory;
import com.epicsell.pages.admin.AdminBasePage;
import com.epicsell.pages.admin.Categories;
import com.epicsell.pages.wizard.SiteLogin;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;

/**
 * Created with IntelliJ IDEA.
 * User: TJ
 * Date: 06.06.13
 * Time: 17:57
 */
//@Ignore("drag&drop problem")
@org.junit.experimental.categories.Category({TestCategory.Categories.class, TestCategory.Active.class})
public class CategoriesEnablingDisabling extends TestCore {
    String disabled_color = "rgba(156, 156, 156, 1)";
    By tree_element = By.className("jstree-move");
    private static Categories categories;
    Category sub_category;
    Category sub_category1;
    Category category;

    @Before
    public void setUrl() {
        categories = (Categories) new SiteLogin(getWebDriver())
                .login(test_shop)
                .openMenuItem(AdminBasePage.MenuItem.CATEGORIES);
        category = new Category();
        category.setName("category" + System.currentTimeMillis());
        category.setActivity(true);
        sub_category = new Category();
        sub_category.setName("subcategory" + System.currentTimeMillis());
        sub_category.setActivity(true);
        sub_category1 = new Category();
        sub_category1.setName("subcategory1" + System.currentTimeMillis());
        sub_category1.setActivity(true);
        categories.createCategory(category).waitForPopup();
        categories.createCategory(sub_category).waitForPopup();
        categories.createCategory(sub_category1).waitForPopup();
        categories.moveCategoryAsSubCategory(category, sub_category);
        categories.moveCategoryAsSubCategory(category, sub_category1);
    }

    @Test
    public void parentCategory() {
        Category newInfo = new Category();
        newInfo.setActivity(false);
        categories.editCategory(category, newInfo).waitForPopup();
        Assert.assertTrue(categories.getCategoryElement(category).getAttribute("class").contains("disabled"));
        Assert.assertFalse(categories.getCategoryElement(sub_category).getAttribute("class").contains("disabled"));
        Assert.assertFalse(categories.getCategoryElement(sub_category1).getAttribute("class").contains("disabled"));
        Assert.assertEquals(disabled_color, categories.getCategoryElement(category).findElement(tree_element).getCssValue("color"));
        Assert.assertEquals(disabled_color, categories.getCategoryElement(sub_category).findElement(tree_element).getCssValue("color"));
        Assert.assertEquals(disabled_color, categories.getCategoryElement(sub_category1).findElement(tree_element).getCssValue("color"));

        newInfo.setActivity(true);
        categories.editCategory(category, newInfo).waitForPopup();
        Assert.assertFalse(categories.getCategoryElement(category).getAttribute("class").contains("disabled"));
        Assert.assertFalse(categories.getCategoryElement(sub_category).getAttribute("class").contains("disabled"));
        Assert.assertFalse(categories.getCategoryElement(sub_category1).getAttribute("class").contains("disabled"));
        Assert.assertNotEquals(disabled_color, categories.getCategoryElement(category).findElement(tree_element).getCssValue("color"));
        Assert.assertNotEquals(disabled_color, categories.getCategoryElement(sub_category).findElement(tree_element).getCssValue("color"));
        Assert.assertNotEquals(disabled_color, categories.getCategoryElement(sub_category1).findElement(tree_element).getCssValue("color"));
    }

    @Ignore("https://epicsell.tpondemand.com/RestUI/board.aspx?acid=D6FB33BAC43F0DCB478F9AFA28434F30#page=bug/458&appConfig=eyJhY2lkIjoiRDZGQjMzQkFDNDNGMERDQjQ3OEY5QUZBMjg0MzRGMzAifQ==" +
            "https://epicsell.tpondemand.com/RestUI/board.aspx?acid=D6FB33BAC43F0DCB478F9AFA28434F30#page=bug/478&appConfig=eyJhY2lkIjoiRDZGQjMzQkFDNDNGMERDQjQ3OEY5QUZBMjg0MzRGMzAifQ==")
    @Test
    public void parentCategoryAndOneChild() {
        categories.setCategoryCheckbox(category, true)
                .setCategoryCheckbox(sub_category, true)
                .setStatus(false)
                .waitCategoryStatus(category, false);
        Assert.assertTrue(categories.getCategoryElement(sub_category).getAttribute("class").contains("disabled"));
        Assert.assertFalse(categories.getCategoryElement(sub_category1).getAttribute("class").contains("disabled"));
        Assert.assertEquals(disabled_color, categories.getCategoryElement(category).findElement(tree_element).getCssValue("color"));
        Assert.assertEquals(disabled_color, categories.getCategoryElement(sub_category).findElement(tree_element).getCssValue("color"));
        Assert.assertEquals(disabled_color, categories.getCategoryElement(sub_category1).findElement(tree_element).getCssValue("color"));


        categories.setCategoryCheckbox(category, true)
                .setCategoryCheckbox(sub_category, true)
                .setStatus(true)
                .waitCategoryStatus(category, true);
        Assert.assertFalse(categories.getCategoryElement(sub_category).getAttribute("class").contains("disabled"));
        Assert.assertFalse(categories.getCategoryElement(sub_category1).getAttribute("class").contains("disabled"));
        Assert.assertNotEquals(disabled_color, categories.getCategoryElement(category).findElement(tree_element).getCssValue("color"));
        Assert.assertNotEquals(disabled_color, categories.getCategoryElement(sub_category).findElement(tree_element).getCssValue("color"));
        Assert.assertNotEquals(disabled_color, categories.getCategoryElement(sub_category1).findElement(tree_element).getCssValue("color"));
    }

    @Test
    public void twoChildCategories() {
        categories.setCategoryCheckbox(sub_category1, true)
                .setCategoryCheckbox(sub_category, true)
                .setStatus(false)
                .waitCategoryStatus(sub_category1, false);
        Assert.assertTrue(categories.getCategoryElement(sub_category).getAttribute("class").contains("disabled"));
        Assert.assertFalse(categories.getCategoryElement(category).getAttribute("class").contains("disabled"));
        Assert.assertNotEquals(disabled_color, categories.getCategoryElement(category).findElement(tree_element).getCssValue("color"));
        Assert.assertEquals(disabled_color, categories.getCategoryElement(sub_category).findElement(tree_element).getCssValue("color"));
        Assert.assertEquals(disabled_color, categories.getCategoryElement(sub_category1).findElement(tree_element).getCssValue("color"));

        categories.setCategoryCheckbox(sub_category1, true)
                .setCategoryCheckbox(sub_category, true)
                .setStatus(true)
                .waitCategoryStatus(sub_category1, true);
        Assert.assertFalse(categories.getCategoryElement(sub_category).getAttribute("class").contains("disabled"));
        Assert.assertFalse(categories.getCategoryElement(sub_category1).getAttribute("class").contains("disabled"));
        Assert.assertNotEquals(disabled_color, categories.getCategoryElement(category).findElement(tree_element).getCssValue("color"));
        Assert.assertNotEquals(disabled_color, categories.getCategoryElement(sub_category).findElement(tree_element).getCssValue("color"));
        Assert.assertNotEquals(disabled_color, categories.getCategoryElement(sub_category1).findElement(tree_element).getCssValue("color"));
    }
}
