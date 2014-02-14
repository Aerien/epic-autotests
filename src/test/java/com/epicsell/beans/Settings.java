package com.epicsell.beans;

/**
 * Created with IntelliJ IDEA.
 * User: Jane T.
 * Date: 06.05.13
 */
@SuppressWarnings("unused")
public class Settings {
    String name;
    String phone;
    String description;
    String email;
    String vk;
    String facebook;
    String twitter;
    String instagram;
    String gaCode;
    String ymCode;

    public String getCustomJSCode() {
        return customJSCode;
    }

    public void setCustomJSCode(String customJSCode) {
        this.customJSCode = customJSCode;
    }

    public String getYmCode() {
        return ymCode;
    }

    public void setYmCode(String ymCode) {
        this.ymCode = ymCode;
    }

    public String getGaCode() {
        return gaCode;
    }

    public void setGaCode(String gaCode) {
        this.gaCode = gaCode;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getVk() {
        return vk;
    }

    public void setVk(String vk) {
        this.vk = vk;
    }

    String customJSCode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
