package com.epicsell.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Jane T.
 * Date: 08.05.13
 */
@SuppressWarnings("unused")
public class Order {
    String clientName;
    String email;
    String phone;
    String address;
    String comment;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    String date;
    Boolean payed;
    Delivery delivery;
    Status status;
    List<Product> products;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    String number;

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Boolean getPayed() {
        return payed;
    }

    public void setPayed(Boolean payed) {
        this.payed = payed;
    }

    public Delivery getDelivery() {
        return delivery;
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public void addProduct(Product product) {
        if (this.products == null) {
            this.products = new ArrayList<Product>();
        }
        this.products.add(product);
    }

    public enum Status {
        NEW("Новый"),
        ACCEPTED("Принят"),
        DONE("Выполнен"),
        DELETED("Удален");

        String status;

        Status(String status) {
            this.status = status;
        }

        public String getStatus() {
            return this.status;
        }
    }

    public static Order getValidOrder() {
        Order order = new Order();
        order.setClientName("clientName" + System.currentTimeMillis());
        order.setEmail("email" + System.currentTimeMillis() + "@email.com");
        order.setPhone("1231231231");
        order.setAddress("address");
        order.setComment("comment");
        order.setPayed(false);
        Delivery delivery = new Delivery();
        delivery.setName("Курьерская доставка по Москве");
        order.setDelivery(delivery);
        order.setStatus(Order.Status.NEW);
        Product product = new Product();
        product.setName("Чиз-кейк Орех-микс");
        order.addProduct(product);
        return order;
    }
}
