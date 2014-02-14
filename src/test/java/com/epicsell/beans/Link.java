package com.epicsell.beans;

/**
 * Created with IntelliJ IDEA.
 * User: Jane T.
 * Date: 17.04.13
 */
@SuppressWarnings("unused")
public class Link {
    public enum Target {
        URL("URL"), EMAIL("Email"), ANCHOR("Anchor", "Якорь");
        String target;
        String target1;

        Target(String target) {
            this.target = target;
        }

        Target(String target, String target1) {
            this.target = target;
            this.target1 = target1;
        }

        public String getTarget() {
            return this.target;
        }

        public String getTarget1() {
            return this.target1;
        }
    }

    public Target getTarget() {
        return target;
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean openInNewTab() {
        return newTab;
    }

    public void setNewTab(Boolean newTab) {
        this.newTab = newTab;
    }

    Target target;
    String url;
    String text;
    Boolean newTab;
}
