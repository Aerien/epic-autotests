package com.epicsell.beans;

import org.openqa.selenium.By;

/**
 * Created with IntelliJ IDEA.
 * User: Jane T.
 * Date: 14.04.13
 */
@SuppressWarnings("unused")
public class Page {
    String name;
    String content;
    Boolean activity;

    public Boolean getActivity() {
        return activity;
    }

    public void setActivity(Boolean activity) {
        this.activity = activity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPageLink(Shop shop) {
        String url = shop.getShopUrl() + "/page/" + name;
        return url.replace(" ", "-");
    }

    public By getAdminPageLocator() {
        return By.xpath("//div[@id='PageView']//tr[descendant::a[text()='" + name + "']]/td[2]");
    }
}
