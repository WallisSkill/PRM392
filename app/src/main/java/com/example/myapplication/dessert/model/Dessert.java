package com.example.myapplication.dessert.model;

public class Dessert {
    private int dessert_id;
    private String dessert_name;
    private String description;
    private double price;
    private String image;

    public Dessert(int dessert_id, String image, double price, String description, String dessert_name) {
        this.dessert_id = dessert_id;
        this.image = image;
        this.price = price;
        this.description = description;
        this.dessert_name = dessert_name;
    }

    public Dessert() {
    }

    public int getDessert_id() {
        return dessert_id;
    }

    public void setDessert_id(int dessert_id) {
        this.dessert_id = dessert_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDessert_name() {
        return dessert_name;
    }

    public void setDessert_name(String dessert_name) {
        this.dessert_name = dessert_name;
    }
}
