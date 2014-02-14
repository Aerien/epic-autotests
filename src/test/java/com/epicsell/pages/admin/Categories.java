package com.epicsell.pages.admin;

import com.epicsell.beans.Category;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: Jane T.
 * Date: 01.06.13
 */
public class Categories extends AdminBasePage {
    private static final By create_new_category_button = By.xpath("//div[contains(@class,'es-head')]/a");
    private static final By select_all_checkbox = By.id("select-all-cat");
    private static final By confirmation_dialog = By.xpath("//div[@class='modal-scrollable']/div");
    public static final By group_delete_button = By.xpath("//div[@id='command-buttons']//button[contains(@class, 'delete')]");
    //---categories table
    private static final By categories_table = By.id("StoreCategoryTree");
    private static final By categories_rows = By.xpath("//div[@id='StoreCategoryTree']/ul//ul//li");
    //---category edit form
    private static final By category_creation_form = By.id("StoreCategory");
    public static final By category_name_input = By.id("StoreCategory_name");
    private static final By category_key_words_input = By.id("StoreCategory_meta_keywords");
    private static final By category_description_input = By.id("StoreCategory_meta_description");
    private static final By category_status_checkbox = By.id("StoreCategory_active");
    private static final By create_category_button = By.id("submitStoreCategory");
    private static final By preview_link = By.xpath("//div[@class='modal-footer']/a");

    public Categories(WebDriver webDriver) {
        super(webDriver);
    }

    public Categories createCategory(Category category) {
        waiter().until(ExpectedConditions.presenceOfElementLocated(create_new_category_button)).click();
        waiter().until(ExpectedConditions.visibilityOfElementLocated(category_creation_form));
        if (category.getName() != null) {
            WebElement input = findElement(category_name_input);
            input.sendKeys(category.getName());
        }

        if (category.getKeywords() != null) {
            WebElement input = findElement(category_key_words_input);
            input.sendKeys(category.getKeywords());
        }
        if (category.getDescription() != null) {
            WebElement input = findElement(category_description_input);
            input.sendKeys(category.getDescription());
        }
        if (category.getActivity() != null) {
            WebElement checkbox = findElement(category_status_checkbox);
            if (category.getActivity() ^ checkbox.isSelected()) {
                checkbox.click();
            }
        }
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            //
        }
        findElement(create_category_button).click();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            //
        }
        if (isElementPresent(create_category_button)) {
            findElement(create_category_button).click();
        }
        try {
            waiter().until(ExpectedConditions.invisibilityOfElementLocated(category_creation_form));
        } catch (TimeoutException te) {
            //
        }
        waitForPopup();
        return this;
    }

    public Categories createCategory(List<Category> categoriesList) {
        for (Category category : categoriesList) {
            createCategory(category);
        }
        return this;
    }

    public WebElement getCategoryElement(Category category) {
        try {
            WebElement categoryElement = findElement(By.xpath("//li[a[text()='" + category.getName() + "']]"));
            executeScript("arguments[0].scrollIntoView();", categoryElement);
            return categoryElement;
        } catch (NoSuchElementException nsee) {
            return null;
        }
    }

    public List<WebElement> getCategoryElements() {
        return findElements(By.xpath("//div[@id='StoreCategoryTree']//li[a]"));
    }

    public Categories waitCategoryStatus(final Category category, final Boolean enabled) {
        waiter().until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(org.openqa.selenium.WebDriver webDriver) {
                Boolean status = null;
                try {
                    status = !getCategoryElement(category).getAttribute("class").contains("disabled");
                } catch (StaleElementReferenceException s) {
                    //
                }
                return status != null ? status == enabled : null;
            }
        });
        return this;
    }

    public Categories setCategoryCheckbox(Category category, Boolean enabled) {
        WebElement cat = getCategoryElement(category);
        if (!cat.getAttribute("class").contains("unchecked") ^ enabled) {
            cat.findElement(By.xpath(".//ins[@class='jstree-checkbox']")).click();
        }
        return this;
    }

    public Categories editCategory(Category category, Category editedCategory) {
        waiter().until(ExpectedConditions.presenceOfElementLocated(categories_table));
        findElement(categories_table).findElement(By.xpath(".//li/a[text()='" + category.getName() + "']")).click();
        waiter().until(ExpectedConditions.visibilityOfElementLocated(category_creation_form));
        if (editedCategory.getName() != null) {
            WebElement input = findElement(category_name_input);
            input.clear();
            input.sendKeys(editedCategory.getName());
        }

        if (editedCategory.getKeywords() != null) {
            WebElement input = findElement(category_key_words_input);
            input.clear();
            input.sendKeys(editedCategory.getKeywords());
        }
        if (editedCategory.getDescription() != null) {
            WebElement input = findElement(category_description_input);
            input.clear();
            input.sendKeys(editedCategory.getDescription());
        }
        if (editedCategory.getActivity() != null) {
            WebElement checkbox = findElement(category_status_checkbox);
            if (editedCategory.getActivity() ^ checkbox.isSelected()) {
                checkbox.click();
            }
        }
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            //
        }
        findElement(create_category_button).click();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            //
        }
        if (isElementPresent(create_category_button)) {
            findElement(create_category_button).click();
        }
        waiter().until(ExpectedConditions.invisibilityOfElementLocated(category_creation_form));
        return this;
    }

    public Categories previewCategory(Category category) {
        waiter().until(ExpectedConditions.presenceOfElementLocated(categories_table));
        findElement(categories_table).findElement(By.xpath(".//li/a[text()='" + category.getName() + "']")).click();
        waiter().until(ExpectedConditions.visibilityOfElementLocated(category_creation_form));
        get(findElement(preview_link).getAttribute("href"));
        loader();
        return this;
    }

    public String getNameValidationMessage() {
        By name_error = By.xpath("//div[@class='controls'][descendant::input[@id='StoreCategory_name']]//div[@class='tooltip-inner']");
        return findElement(name_error).getText();
    }

    public Categories moveCategoryAsSubCategory(Category category, Category sub_category) {
        try {
            move(category, sub_category);
        } catch (TimeoutException te) {
            move(category, sub_category);
        }
        return this;
    }

    private void move(final Category category, Category sub_category) {
        WebElement category1;
        category1 = getCategoryElement(category);
        WebElement category2 = getCategoryElement(sub_category);
        Actions builder = new Actions(this);
        builder.clickAndHold(category2.findElement(By.tagName("a"))).perform();
        Dimension size = category1.getSize();
        WebElement c2 = category1.findElement(By.tagName("a")).findElement(By.xpath(".//ins[contains(@class,'checkbox')]"));
        builder.moveToElement(c2, -150, 150).perform();
        builder.moveToElement(c2, size.getWidth() * 2 / 3, size.getHeight() / 2).perform();
        builder.moveToElement(c2, size.getWidth() / 3, size.getHeight() / 2).perform();
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            //
        }
        builder.release().perform();
        By tree = By.xpath("//li[@id][a[contains(text(),'" + category.getName() +
                "')]]//li[@id][a[contains(text(),'" + sub_category.getName() + "')]]");
        waiter().until(ExpectedConditions.presenceOfElementLocated(tree));
    }

    public Categories setStatus(Boolean enabled) {
        By select_button = By.xpath("//div[@id='command-buttons']/div[2]/div/button");
        By status_list = By.xpath("//div[@id='command-buttons']/div[2]/div/ul");

        findElement(select_button).click();
        waiter().until(ExpectedConditions.visibilityOfElementLocated(status_list));
        if (enabled) {
            findElement(By.xpath("//a[@data-group-action='activate']")).click();
        } else {
            findElement(By.xpath("//a[@data-group-action='deactivate']")).click();
        }
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return this;
    }

    public Categories deleteCategory(Category category) {
        WebElement categoryRow = getCategoryElement(category);
        WebElement btn = categoryRow.findElement(By.xpath(".//i[@class='icon-trash']"));
        click(btn);
        waiter().until(ExpectedConditions.visibilityOfElementLocated(confirmation_dialog));
        try {
            TimeUnit.SECONDS.sleep(1L);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
        findElement(confirmation_dialog).findElement(By.xpath(".//a[2]")).click();
        waitForPopup();
        return this;
    }

    public Categories deleteGroupOfCategories(List<Category> categoriesList) {
        for (Category category : categoriesList) {
            setCategoryCheckbox(category, true);
        }
        waiter().until(ExpectedConditions.visibilityOfElementLocated(group_delete_button)).click();
        waiter().until(ExpectedConditions.visibilityOfElementLocated(confirmation_dialog));
        try {
            TimeUnit.SECONDS.sleep(1L);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
        findElement(confirmation_dialog).findElement(By.xpath(".//a[2]")).click();
        waitForPopup();
        return this;
    }

    public Categories deleteSelectedCategories() {
        waiter().until(ExpectedConditions.visibilityOfElementLocated(group_delete_button)).click();
        waiter().until(ExpectedConditions.visibilityOfElementLocated(confirmation_dialog));
        try {
            TimeUnit.SECONDS.sleep(1L);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
        findElement(confirmation_dialog).findElement(By.xpath(".//a[2]")).click();
        waitForPopup();
        return this;
    }

    public Categories changePlaces(Category category, Category category1) {
        WebElement main;
        WebElement sub;
        List<WebElement> siteCategoriesList = getCategoryElements();
        List<String> siteCategoriesNames = new ArrayList<String>();
        for (WebElement cat : siteCategoriesList) {
            siteCategoriesNames.add(cat.getText());
        }
        if (siteCategoriesNames.indexOf(category.getName()) > siteCategoriesNames.indexOf(category1.getName())) {
            main = getCategoryElement(category1);
            sub = getCategoryElement(category);
        } else {
            main = getCategoryElement(category);
            sub = getCategoryElement(category1);
        }
        Actions builder = new Actions(this);
        builder.clickAndHold(sub.findElement(By.tagName("a"))).perform();
        Dimension size = main.getSize();
        WebElement c2 = main;
        builder.moveToElement(c2, -150, 0).perform();
        builder.moveToElement(c2, size.getWidth() * 2 / 3, 0).perform();
        builder.moveToElement(c2, size.getWidth() / 3, 0).perform();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            //
        }
        builder.release().perform();
        return this;
    }

    public Categories selectAll() {
        WebElement allChbx = findElement(select_all_checkbox);
        if (!allChbx.getAttribute("class").contains("checked")) {
            allChbx.click();
        }
        return this;
    }

    public int getCategoriesCount() {
        return findElements(categories_rows).size();
    }
}
