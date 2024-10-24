package com.example.myapplication.order.model;


import java.util.Date;

public class Order {
    private int order_id;
    private int customer_id;
    private String order_date;
    private double total_amount;
    private int status;

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(double total_amount) {
        this.total_amount = total_amount;
    }

    public Order(int customer_id, String order_date, int order_id, int status, double total_amount) {
        this.customer_id = customer_id;
        this.order_date = order_date;
        this.order_id = order_id;
        this.status = status;
        this.total_amount = total_amount;
    }
}
