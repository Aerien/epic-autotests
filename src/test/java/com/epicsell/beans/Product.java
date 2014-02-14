package com.epicsell.beans;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Jane T.
 * Date: 06.04.13
 */
@SuppressWarnings("unused")
public class Product {
    String category; //can be the list split by ,
    String name;
    BigDecimal price;
    String url;
    String article;
    Boolean is_active;
    String image;
    Integer quantity;
    String short_description;
    String full_description;
    String meta_title;
    String meta_keywords;
    String meta_description;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public Boolean isActive() {
        return is_active;
    }

    public void setActivity(Boolean is_active) {
        this.is_active = is_active;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getShortDescription() {
        return short_description;
    }

    public void setShortDescription(String short_description) {
        this.short_description = short_description;
    }

    public String getFullDescription() {
        return full_description;
    }

    public void setFullDescription(String full_description) {
        this.full_description = full_description;
    }

    public String getMetaTitle() {
        return meta_title;
    }

    public void setMetaTitle(String meta_title) {
        this.meta_title = meta_title;
    }

    public String getMetaKeywords() {
        return meta_keywords;
    }

    public void setMetaKeywords(String meta_keywords) {
        this.meta_keywords = meta_keywords;
    }

    public String getMetaDescription() {
        return meta_description;
    }

    public void setMetaDescription(String meta_description) {
        this.meta_description = meta_description;
    }

    public enum Header {
        CATEGORY(""),
        NAME("Необходимо заполнить поле Название."),
        PRICE("Необходимо заполнить поле Цена.", "Цена должен быть числом."),
        URL(""),
        ARTICLE(""),
        ACTIVITY("", "Статус должно быть 1 или 0."),
        IMAGE(""),
        QTY("", "Остаток должен быть целым числом."),
        SHORT_DESCRIPTION(""),
        FULL_DESCRIPTION(""),
        META_TITLE(""),
        META_KEYWORDS(""),
        META_DESCRIPTION("");

        String emptyValidationMessage;
        String invalidValidationMessage;

        Header(String message) {
            this.emptyValidationMessage = message;
        }

        Header(String message, String message2) {
            this.emptyValidationMessage = message;
            this.invalidValidationMessage = message2;
        }

        public String getMessage() {
            return emptyValidationMessage;
        }

        public String getMessageForInvalidValue() {
            return invalidValidationMessage;
        }
    }

    public static ArrayList<Product> getProducts(Integer count) {
        ArrayList<Product> products = new ArrayList<Product>();
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            Product product = new Product();
            String unique = String.valueOf(System.currentTimeMillis());
            unique = unique.substring(unique.length() - 2, unique.length());
            product.setCategory("category" + unique);
            product.setName(Thread.currentThread().getStackTrace()[2].getMethodName() + System.currentTimeMillis());
            product.setPrice(new BigDecimal("105.6"));
            product.setUrl("url" + System.currentTimeMillis());
            product.setArticle("article" + System.currentTimeMillis() + i);
            product.setActivity(random.nextBoolean());
            product.setImage("image" + System.currentTimeMillis());
            product.setQuantity(random.nextInt(99));
            product.setShortDescription("short_description" + System.currentTimeMillis());
            product.setFullDescription("full_description" + System.currentTimeMillis());
            product.setMetaTitle("meta_title" + System.currentTimeMillis());
            product.setMetaKeywords("meta_keywords" + System.currentTimeMillis());
            product.setMetaDescription("meta_description" + System.currentTimeMillis());
            products.add(product);
        }
        return products;
    }

    public static ArrayList<Product> getProductsWithInvalidInfo(Integer count, List<Header> headers) {
        ArrayList<Product> products = getProducts(count);
        Random random = new Random();
        for (Product product : products) {
            if (headers.contains(Header.CATEGORY)) {
                product.setCategory("category" + System.currentTimeMillis());
            }
            if (headers.contains(Header.CATEGORY)) {
                product.setName(Thread.currentThread().getStackTrace()[2].getMethodName() + " " + System.currentTimeMillis());
            }
            if (headers.contains(Header.CATEGORY)) {
                product.setPrice(new BigDecimal("105.6"));
            }
            if (headers.contains(Header.CATEGORY)) {
                product.setUrl("url" + System.currentTimeMillis());
            }
            if (headers.contains(Header.CATEGORY)) {
                product.setArticle("article" + System.currentTimeMillis());
            }
            if (headers.contains(Header.CATEGORY)) {
                product.setActivity(random.nextBoolean());
            }
            if (headers.contains(Header.CATEGORY)) {
                product.setImage("image" + System.currentTimeMillis());
            }
            if (headers.contains(Header.CATEGORY)) {
                product.setQuantity(random.nextInt(99));
            }
            if (headers.contains(Header.CATEGORY)) {
                product.setShortDescription("short_description" + System.currentTimeMillis());
            }
            if (headers.contains(Header.CATEGORY)) {
                product.setFullDescription("full_description" + System.currentTimeMillis());
            }
            if (headers.contains(Header.CATEGORY)) {
                product.setMetaTitle("meta_title" + System.currentTimeMillis());
            }
            if (headers.contains(Header.CATEGORY)) {
                product.setMetaKeywords("meta_keywords" + System.currentTimeMillis());
            }
            if (headers.contains(Header.CATEGORY)) {
                product.setMetaDescription("meta_description" + System.currentTimeMillis());
            }
//            if (headers.contains(Header.CATEGORY)) {
//
//            }
        }
        return products;
    }

    public static ArrayList<Product> getProductsWithEmptyInfo(Integer count, List<Header> headers) {
        ArrayList<Product> products = getProducts(count);
        Random random = new Random();
        for (Product product : products) {
            if (headers.contains(Header.CATEGORY)) {
                product.setCategory("");
            }
            if (headers.contains(Header.NAME)) {
                product.setName("");
            }
            if (headers.contains(Header.PRICE)) {
                product.setPrice(null);
            }
            if (headers.contains(Header.URL)) {
                product.setUrl("");
            }
            if (headers.contains(Header.ARTICLE)) {
                product.setArticle("");
            }
            if (headers.contains(Header.ACTIVITY)) {
                product.setActivity(null);
            }
            if (headers.contains(Header.IMAGE)) {
                product.setImage("");
            }
            if (headers.contains(Header.QTY)) {
                product.setQuantity(null);
            }
            if (headers.contains(Header.CATEGORY)) {
                product.setShortDescription("");
            }
            if (headers.contains(Header.CATEGORY)) {
                product.setFullDescription("");
            }
            if (headers.contains(Header.CATEGORY)) {
                product.setMetaTitle("");
            }
            if (headers.contains(Header.CATEGORY)) {
                product.setMetaKeywords("");
            }
            if (headers.contains(Header.CATEGORY)) {
                product.setMetaDescription("");
            }
        }
        return products;
    }
}
