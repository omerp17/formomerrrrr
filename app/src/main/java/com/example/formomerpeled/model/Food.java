package com.example.formomerpeled.model;

public class Food {
    String id, foodtype, city, status, img ,data;
    double amount;
    User ContactUser;

    public Food() {

    }

    public Food(String id, String foodtype, String city, String status, String img, String data, double amount, User contactUser) {
        this.id = id;
        this.foodtype = foodtype;
        this.city = city;
        this.status = status;
        this.img = img;
        this.data = data;
        this.amount = amount;
        ContactUser = contactUser;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFoodtype() {
        return foodtype;
    }

    public void setFoodtype(String foodtype) {
        this.foodtype = foodtype;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public User getContactUser() {
        return ContactUser;
    }

    public void setContactUser(User contactUser) {
        ContactUser = contactUser;
    }

    @Override
    public String toString() {
        return "Food{" +
                "id='" + id + '\'' +
                ", foodtype='" + foodtype + '\'' +
                ", city='" + city + '\'' +
                ", status='" + status + '\'' +
                ", img='" + img + '\'' +
                ", data='" + data + '\'' +
                ", amount=" + amount +
                ", ContactUser=" + ContactUser +
                '}';
    }
}
