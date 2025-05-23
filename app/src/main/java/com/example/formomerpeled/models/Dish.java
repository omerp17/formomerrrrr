package com.example.formomerpeled.models;

public class Dish {
    protected String id;
    protected String name;
    protected Restaurant restaurant;
    protected double price;

    protected String details;

    protected int numberRate;
    protected  double rate;
    protected  double sumRate;

    public Dish(String id, String name, Restaurant restaurant, double price,  String details) {
        this.id = id;
        this.name = name;
        this.restaurant = restaurant;
        this.price = price;

        this.details = details;
        this.numberRate = 0;
        this.rate = 0.0;
        this.sumRate = 0.0;
    }

    public Dish(String id, String name, Restaurant restaurant, double price, String details, int numberRate, double rate, double sumRate) {
        this.id = id;
        this.name = name;
        this.restaurant = restaurant;
        this.price = price;

        this.details = details;
        this.numberRate = numberRate;
        this.rate = rate;
        this.sumRate = sumRate;
    }

    public Dish() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }



    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getNumberRate() {
        return numberRate;
    }

    public void setNumberRate(int numberRate) {
        this.numberRate = numberRate;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getSumRate() {
        return sumRate;
    }

    public void setSumRate(double sumRate) {
        this.sumRate = sumRate;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", restaurant=" + restaurant +
                ", price=" + price +
                ", details='" + details + '\'' +
                ", numberRate=" + numberRate +
                ", rate=" + rate +
                ", sumRate=" + sumRate +
                '}';
    }
}
