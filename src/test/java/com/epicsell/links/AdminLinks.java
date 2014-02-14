package com.epicsell.links;

/**
 * Created with IntelliJ IDEA.
 * User: Jane T.
 * Date: 05.04.13
 */
public enum AdminLinks implements Link {
    ADMIN_ORDERS("/admin/orders"),
    ADMIN_SETTINGS("/admin/core/systemDefault"),
    ADMIN_AUTH("/admin/auth"),
    ADMIN_CATEGORIES("/admin/store/categories"),
    ADMIN_PAGES("/admin/pages"),
    PRODUCTS_IMPORT("/admin/csv/import"),
    PRODUCTS("/admin/store/products"),
    PAYMENT_METHODS("/admin/store/paymentMethod");
    String link;

    AdminLinks(String link) {
        this.link = link;
    }

    @Override
    public String $() {
        return link;
    }

}
