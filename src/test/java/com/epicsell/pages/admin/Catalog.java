package com.epicsell.pages.admin;

import com.epicsell.beans.Product;
import com.epicsell.pages.site.ProductPage;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: Jane T.
 * Date: 07.05.13
 */
@SuppressWarnings("unused")
public class Catalog extends AdminBasePage {
    public static final By catalog_menu_link = By.xpath("//ul[@id][contains(@class,'toolbox__nav')]/li[contains(@class,'catalog')]/a");
    public static final By product_list_table = By.xpath("//div[@id='productsView']/table");
    public static final By product_list_table_header = By.xpath("//div[@id='productsView']/table/thead");
    public static final By check_all_input = By.xpath(".//th[1]/input");
    public static final By sort_by_name_link = By.xpath(".//th[2]/a");
    public static final By sort_by_price_link = By.xpath(".//th[3]/a");
    public static final By sort_by_state_link = By.xpath(".//th[4]/a");
    public static final By product_list_table_body = By.xpath("//div[@id='productsView']/table/tbody");
    public static final By check_col_input = By.xpath(".//td[1]/input");
    public static final By sku_col = By.xpath(".//td[@class='-sku']");
    public static final By name_col_link = By.xpath(".//td[@class='-title']//a");
    public static final By price_col = By.xpath(".//td[@class='-price']");
    public static final By state_col = By.xpath(".//td[@class='-tools _first']//a[@class='is_active_toggle']");
    public static final By edit_prod_button = By.xpath(".//td[@class='-tools _last']/a[1]");
    public static final By delete_prod_button = By.xpath(".//td[@class='-tools _last']/a[2]");
    public static final By product_list_table_footer = By.xpath("//div[@id='productsView']/table/tfoot");
    public static final By delete_selected_button = By.xpath(".//button[@id]");

    public static final By product_edit_form = By.id("StoreProduct");
    public static final By product_name_input = By.xpath("//form[@id='StoreProduct']//input[@id='StoreProduct_name']");
    public static final By product_article_input = By.xpath("//form[@id='StoreProduct']//input[@id='StoreProduct_sku']");
    public static final By product_short_description_input = By.xpath("//div[@class='redactor_box'][descendant::textarea[@id='StoreProduct_short_description']]//div[contains(@class,'redactor')]");
    public static final By product_full_description_input = By.xpath("//div[@class='redactor_box'][descendant::textarea[@id='StoreProduct_full_description']]//div[contains(@class,'redactor')]");
    public static final By product_price_input = By.xpath("//form[@id='StoreProduct']//input[@id='StoreProduct_price']");
    public static final By product_state_select = By.xpath("//form[@id='StoreProduct']//select[@id='StoreProduct_is_active']");
    public static final By product_category_input = By.xpath("//form[@id='StoreProduct']//input[@id='StoreProduct_categories']");
    public static final By product_upload_photo_input = By.xpath("//fieldset//input[@type='file']");
    public static final By product_images_list = By.xpath("//fieldset//ul");
    public static final By product_images = By.xpath("//li//img");
    public static final By product_show_on_site = By.xpath("//div[@class='modal-footer']/a[@id]");
    public static final By save_product_button = By.id("submitStoreProduct");
    public static final By close_product_edit_form_button = By.xpath("//div[@class='modal-footer']/button[2]");
    public static final By autosave_message = By.xpath("//div[text()='автосохранение...']");
    public static final By product_was_created_message = By.xpath("//div[text()='Вы создали новый товар']");
    public static final By product_was_updated_message = By.xpath("//div[text()='Изменения успешно сохранены']");
    public static final By product_line = By.xpath("//div[@id='productsView']//tbody/tr");
    public static final By product_list = By.xpath("//div[@id='productsView']");
    public static final By product_paging_next_page = By.xpath("//ul[@class='yiiPager']/li[@class='next']//a");

    public Catalog(WebDriver webDriver) {
        super(webDriver);
    }

    public Catalog createSimpleProduct(Product product) {
        waiter().until(ExpectedConditions.presenceOfElementLocated(catalog_menu_link)).click();
        waiter().until(ExpectedConditions.titleContains("Товары"));
        findElement(AdminBasePage.create_bean_button).click();
        waiter().until(ExpectedConditions.visibilityOfElementLocated(product_edit_form));
        fillProductEditForm(product);
        findElement(save_product_button).click();
        if (isElementPresent(close_product_edit_form_button)) {
            try {
                findElement(close_product_edit_form_button).click();
            } catch (Exception nse) {
                //
            }
        }
        waiter().until(ExpectedConditions.invisibilityOfElementLocated(product_edit_form));
        try {
            waiter().until(ExpectedConditions.visibilityOfElementLocated(product_was_updated_message)).click();
        } catch (TimeoutException te) {
            //
        }
//        wait.until(ExpectedConditions.invisibilityOfElementLocated(product_was_updated_message));
        return this;
    }

    public Catalog deleteSimpleProduct(String productName) {
        List<WebElement> npLink;
        WebElement product = null;
        do {
            List<WebElement> products = findElements(By.xpath("//a[text()='" + productName + "']"));
            if (products.size() > 0) {
                product = products.get(0).findElement(By.xpath("./ancestor::tr"));
            }
            if (product == null) {
                if (isElementPresent(product_paging_next_page)) {
                    npLink = findElements(product_paging_next_page);
                    npLink.get(0).click();
                    try {
                        new WebDriverWait(webDriver, 1).until(new ExpectedCondition<Boolean>() {
                            @Override
                            public Boolean apply(WebDriver webDriver) {
                                return findElement(product_list).getAttribute("class").contains("loading");
                            }
                        });
                    } catch (TimeoutException te) {
                        //
                    }
                    waiter().until(new ExpectedCondition<Boolean>() {
                        @Override
                        public Boolean apply(WebDriver webDriver) {
                            return !findElement(product_list).getAttribute("class").contains("loading");
                        }
                    });
                }
            }
        }
        while (product == null && isElementPresent(product_paging_next_page));
        Assert.assertNotNull("Продукт с именем " + productName + " не найден", product);
        new Actions(webDriver).moveToElement(product).release().perform();
        waiter().until(ExpectedConditions.visibilityOf(product.findElement(By.xpath(".//a[@class='delete']")))).click();
        Alert alert = waiter().until(ExpectedConditions.alertIsPresent());
        Assert.assertEquals("Вы уверены, что хотите удалить данный элемент?", alert.getText());
        alert.accept();
        waiter().until(ExpectedConditions.stalenessOf(product));
        return this;
    }

    public Catalog fillProductEditForm(Product product) {
        WebDriverWait wait = new WebDriverWait(webDriver, 4);
        WebElement prod_name = findElement(product_name_input);
        WebElement prod_price = findElement(product_price_input);
        WebElement article = findElement(product_article_input);
        WebElement short_description = findElement(product_short_description_input);
        WebElement full_description = findElement(product_full_description_input);
        WebElement prod_state = findElement(product_state_select);
        WebElement prod_category = findElement(product_category_input);

        if (product.getName() != null && !product.getName().equals(prod_name.getAttribute("value"))) {
            prod_name.clear();
            prod_name.sendKeys(product.getName());
            blurField(prod_name);
        }
        if (product.getPrice() != null && !product.getPrice().toString().equals(prod_price.getAttribute("value"))) {
            prod_price.sendKeys(Keys.CONTROL + "a");
            prod_price.sendKeys(product.getPrice().toString());
            blurField(prod_price);
        }
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(product_was_created_message));
        } catch (TimeoutException te) {
            //
        }
        wait.until(ExpectedConditions.invisibilityOfElementLocated(product_was_created_message));
        if (product.getArticle() != null && !product.getArticle().equals(article.getAttribute("value"))) {
            article.clear();
            article.sendKeys(product.getArticle());
            blurField(article);
        }
        if (product.getShortDescription() != null && !product.getShortDescription().equals(short_description.getAttribute("value"))) {
            short_description.sendKeys(Keys.CONTROL, "a");
            short_description.sendKeys(Keys.BACK_SPACE);
            short_description.sendKeys(product.getShortDescription());
        }
        if (product.getFullDescription() != null && !product.getFullDescription().equals(full_description.getAttribute("value"))) {
            full_description.sendKeys(Keys.CONTROL, "a");
            full_description.sendKeys(Keys.BACK_SPACE);
            full_description.sendKeys(product.getFullDescription());
        }
        if (product.isActive() != null) {
            String act_locator = "//div[@id='s2id_StoreProduct_is_active']//a";
            WebElement activity = findElement(By.xpath(act_locator));
            activity.click();
            WebElement activity_input = waiter().until(ExpectedConditions.visibilityOfElementLocated
                    (By.xpath("//div[contains(@class,'with-searchbox')]//input")));
            if (product.isActive()) {
                activity_input.sendKeys("Активен");
            } else {
                activity_input.sendKeys("Не активен");
            }
            activity_input.sendKeys(Keys.RETURN);
        }
        if (product.getCategory() != null && !product.getCategory().equals(prod_category.getAttribute("value"))) {
            String currentStyle = prod_category.getAttribute("style");
            String currentType = prod_category.getAttribute("type");
            setAttributeToWebElement(prod_category, "style", currentStyle.replace("display: none;", ""));
            setAttributeToWebElement(prod_category, "type", currentType.replace("hidden", "text"));
            prod_category.sendKeys(Keys.CONTROL, "a");
            prod_category.sendKeys(Keys.BACK_SPACE);
            prod_category.sendKeys(product.getCategory());
            blurField(prod_category);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                //
            }
            setAttributeToWebElement(prod_category, "style", currentStyle);
            setAttributeToWebElement(prod_category, "type", currentType);
        }
        if (product.getImage() != null) {
            for (String filePath : product.getImage().split(",")) {
                List<WebElement> imagesList = findElements(product_images);
                final int imagesCount = imagesList.size();
                WebElement input = findElement(product_upload_photo_input);
                executeScript("arguments[0].scrollIntoView();", input);
                input.sendKeys(filePath);
                wait.until(new ExpectedCondition<Boolean>() {
                    @Override
                    public Boolean apply(WebDriver driver) {
                        return driver.findElements(product_images).size() > imagesCount;
                    }
                });
            }
        }
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            //
        }
        return this;
    }

    public Catalog editSimpleProduct(String productName, Product editedProduct) {
        findElement(catalog_menu_link).click();
        waiter().until(ExpectedConditions.titleContains("Товары"));
        List<WebElement> npLink;
        WebElement productLine = null;

        do {
            List<WebElement> products = findElements(By.xpath("//a[text()='" + productName + "']"));
            if (products.size() > 0) {
                productLine = products.get(0).findElement(By.xpath("./ancestor::tr"));
                break;
            }
            if ((npLink = findElements(product_paging_next_page)).size() != 0) {
                npLink.get(0).click();
                try {
                    new WebDriverWait(webDriver, 1).until(new ExpectedCondition<Boolean>() {
                        @Override
                        public Boolean apply(WebDriver webDriver) {
                            return findElement(product_list).getAttribute("class").contains("loading");
                        }
                    });
                } catch (TimeoutException te) {
                    //
                }
                waiter().until(new ExpectedCondition<Boolean>() {
                    @Override
                    public Boolean apply(WebDriver webDriver) {
                        return !findElement(product_list).getAttribute("class").contains("loading");
                    }
                });
            }
        } while (npLink.size() != 0);

        if (productLine != null) {
            findElement(By.linkText(productName)).click();
            waiter().until(ExpectedConditions.visibilityOfElementLocated(product_edit_form));
            waiter().until(ExpectedConditions.visibilityOfElementLocated(product_name_input));
            fillProductEditForm(editedProduct);
            findElement(save_product_button).click();
            if (isElementPresent(close_product_edit_form_button)) {
                try {
                    findElement(close_product_edit_form_button).click();
                } catch (Exception nse) {
                    //
                }
            }
            waiter().until(ExpectedConditions.invisibilityOfElementLocated(product_edit_form));
            try {
                waiter().until(ExpectedConditions.visibilityOfElementLocated(product_was_updated_message));
            } catch (TimeoutException te) {
                //
            }
//            waiter().until(ExpectedConditions.invisibilityOfElementLocated(product_was_updated_message));
        } else {
            Assert.fail("Продукт с именем " + editedProduct.getName() + " не найден");
        }
        return this;
    }

    public Catalog deleteSeveralProducts(ArrayList<Product> products) {
        List<String> names = new ArrayList<String>();
        for (Product product : products) {
            names.add(product.getName());
        }
        return deleteSeveralProducts(names);
    }

    public Catalog deleteSeveralProducts(List<String> products) {
        for (String name : products) {
            List<WebElement> checkboxes = findElements(By.xpath("//a[text()='" + name + "']/ancestor::tr/td[1]//input"));
            for (WebElement checkbox : checkboxes) {
                if (!checkbox.isSelected()) checkbox.click();
            }
        }
        By deleteProducts = By.xpath("//button[@id='yw2']");
        findElement(deleteProducts).click();
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            //
        }
        return this;
    }

    public ProductPage showProductOnSite(Product product) {
        findElement(catalog_menu_link).click();
        waiter().until(ExpectedConditions.titleContains("Товары"));
        List<WebElement> npLink;
        WebElement productLine = null;
        do {
            List<WebElement> products = findElements(By.linkText(product.getName()));
            if (products.size() > 0) {
                productLine = products.get(0).findElement(By.xpath("./ancestor::tr"));
                break;
            }
            if ((npLink = findElements(product_paging_next_page)).size() != 0) {
                npLink.get(0).click();
                try {
                    new WebDriverWait(webDriver, 1).until(new ExpectedCondition<Boolean>() {
                        @Override
                        public Boolean apply(WebDriver webDriver) {
                            return findElement(product_list).getAttribute("class").contains("loading");
                        }
                    });
                } catch (TimeoutException te) {
                    //
                }
                waiter().until(new ExpectedCondition<Boolean>() {
                    @Override
                    public Boolean apply(WebDriver webDriver) {
                        return !findElement(product_list).getAttribute("class").contains("loading");
                    }
                });
            }
        } while (npLink.size() != 0);

        if (productLine != null) {
            productLine.findElement(By.linkText(product.getName())).click();
            waiter().until(ExpectedConditions.visibilityOfElementLocated(product_edit_form));
            waiter().until(ExpectedConditions.visibilityOfElementLocated(product_name_input));

            get(findElement(product_show_on_site).getAttribute("href"));
            loader();
        } else {
            java.util.logging.Logger.getLogger("EditProduct").info("Продукт с именем " + product.getName() + " не найден");
        }
        return new ProductPage(this);
    }


    public void deleteProductsMethod() {
        By selectAll = By.xpath("//div[contains(@class,'-bot')]//a[contains(@class,'select-all')]");
        By delete = By.xpath("//button[@id='yw2'][not(contains(@class,'disabled'))]");
        while (isElementPresent(selectAll)) {
            findElement(selectAll).click();
            waiter().until(ExpectedConditions.presenceOfElementLocated(delete)).click();
            try {
                waiter().until(new ExpectedCondition<Object>() {
                    @Override
                    public Object apply(org.openqa.selenium.WebDriver driver) {
                        Boolean loading = false;
                        try {
                            loading = findElement(By.id("productsView"))
                                    .getAttribute("class").contains("grid-view-loading");
                        } catch (StaleElementReferenceException stl) {
                            //
                        }
                        return loading;
                    }
                });
            } catch (TimeoutException e) {
                //
            }
            waiter().until(new ExpectedCondition<Object>() {
                @Override
                public Object apply(org.openqa.selenium.WebDriver driver) {
                    Boolean contains = true;
                    try {
                        contains = findElement(By.id("productsView")).getAttribute("class").contains("grid-view-loading");
                    } catch (Exception e) {
                        //
                    }
                    return !contains;
                }
            });
            waitForPopup();
        }
    }
}
