package com.epicsell.beans;

/**
 * Created with IntelliJ IDEA.
 * User: Jane T.
 * Date: 01.06.13
 */
public class Category {
    String name;
    String keywords;
    String description;
    Boolean activity;

    public Category() {
    }

    public Category(Category category) {
        this.name = category.getName();
        this.keywords = category.getKeywords();
        this.description = category.getDescription();
        this.activity = category.getActivity();
    }

    public Boolean getActivity() {
        return activity;
    }

    public void setActivity(Boolean activity) {
        this.activity = activity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
