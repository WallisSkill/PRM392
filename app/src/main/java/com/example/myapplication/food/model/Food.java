package com.example.myapplication.food.model;

public class Food {
    private int food_id;
    private String food_name;
    private String description;
    private double price;
    private String image;

    public Food(String description, int food_id, String food_name, String image, double price) {
        this.description = description;
        this.food_id = food_id;
        this.food_name = food_name;
        this.image = image;
        this.price = price;
    }

    public Food() {

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getFood_id() {
        return food_id;
    }

    public void setFood_id(int food_id) {
        this.food_id = food_id;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
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
}
