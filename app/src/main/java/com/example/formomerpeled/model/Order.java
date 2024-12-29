package com.example.formomerpeled.model;

public class Order {
    String id , date;
    User user;
    Food food;

    public Order() {

    }

    public Order(String id, String date, User user, Food food) {
        this.id = id;
        this.date = date;
        this.user = user;
        this.food = food;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", date='" + date + '\'' +
                ", user=" + user +
                ", food=" + food +
                '}';
    }
}
