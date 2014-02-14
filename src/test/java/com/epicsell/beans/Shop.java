package com.epicsell.beans;

/**
 * Created with IntelliJ IDEA.
 * User: Jane T.
 * Date: 30.03.13
 */
@SuppressWarnings("unused")
public class Shop {
    String email;
    String password;
    String shop_name;
    String client_name;
    String city;
    String country;
    String activityType;

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getClientName() {
        return client_name;
    }

    public void setClientName(String client_name) {
        this.client_name = client_name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getShopName() {
        return shop_name;
    }

    public String getShopUrl() {
        return "http://" + this.getShopName() + ".epicsell.ru";
    }

    public String getShopShortUrl() {
        return this.getShopName() + ".epicsell.ru";
    }

    public void setShopName(String shop_name) {
        this.shop_name = shop_name;
    }

    public void generateShopName(String shop_name) {
        String name = shop_name + System.currentTimeMillis();
        if (name.length() > 56) {
            name = name.substring(0, 55);
        }
        this.shop_name = name + ".test";
    }

    public void generateShopName() {
        this.shop_name = System.currentTimeMillis() + ".test";
    }
}
