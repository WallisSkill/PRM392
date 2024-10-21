package com.example.myapplication.drink.model;

public class Drink {
    private int drink_id;
    private String drink_name;
    private String description;
    private double price;
    private String image;

    public Drink() {
    }

    public Drink(int drink_id, String drink_name, String description, double price, String image) {
        this.drink_id = drink_id;
        this.drink_name = drink_name;
        this.description = description;
        this.price = price;
        this.image = image;
    }

    public int getDrink_id() {
        return drink_id;
    }

    public void setDrink_id(int drink_id) {
        this.drink_id = drink_id;
    }

    public String getDrink_name() {
        return drink_name;
    }

    public void setDrink_name(String drink_name) {
        this.drink_name = drink_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
