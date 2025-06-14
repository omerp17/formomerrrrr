package com.example.formomerpeled.models;

import java.io.Serializable;
import java.util.Objects;

public class Dish  implements Serializable {
    protected String id;
    protected String name;
    protected String resId;
    protected double price;

    protected String details;

    protected int numberRate;
    protected  double rate;
    protected  double sumRate;

    public Dish(String id, String name, String resId, double price, String details, int numberRate, double rate, double sumRate) {
        this.id = id;
        this.name = name;
        this.resId = resId;
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

    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
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
    public void setNumberRate() {
        this.numberRate++;
    }

    public double getRate() {

        if(numberRate>0&& sumRate>0)
                    return sumRate/numberRate;
        return  0.0;
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
    public void setNewSumRate(double rate) {
        this.sumRate += rate;
    }



    @Override
    public String toString() {
        return "Dish{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", restaurant=" + resId +
                ", price=" + price +
                ", details='" + details + '\'' +
                ", numberRate=" + numberRate +
                ", rate=" + rate +
                ", sumRate=" + sumRate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dish dish = (Dish) o;
        return Objects.equals(id, dish.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
