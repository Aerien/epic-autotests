package com.epicsell.beans;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Jane T.
 * Date: 07.05.13
 */
@SuppressWarnings("unused")
public class Delivery {
    String name;
    Boolean activity;
    BigDecimal price;
    BigDecimal free_price;
    String description;
    List<String> payments;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getActivity() {
        return activity;
    }

    public void setActivity(Boolean activity) {
        this.activity = activity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getFreePrice() {
        return free_price;
    }

    public void setFreePrice(BigDecimal free_price) {
        this.free_price = free_price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getPayments() {
        return payments;
    }

    public void setPayments(List<String> payments) {
        this.payments = payments;
    }

    public void addPayment(String payment) {
        if (this.payments == null) {
            this.payments = new ArrayList<String>();
        }
        this.payments.add(payment);
    }
}
