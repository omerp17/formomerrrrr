package com.example.formomerpeled.model;

import java.util.List;

public class RestaurantDatabase {
    String city;
    String cuisineType;
    List<String> glutenFreeMenuItems;
    Restaurant restaurant;
    public List<Restaurant> searchByLocation( String city) {
        this.city=city;
        // חיפוש מסעדות לפי מיקום
    }

    public List<Restaurant> searchByCuisine(String cuisineType) {
        this.cuisineType=cuisineType;
        // חיפוש לפי סוג אוכל
    }

    public List<Restaurant> searchGlutenFree() {
        this.searchGlutenFree=searchGlutenFree;
        // חיפוש מסעדות עם אוכל ללא גלוטן
    }

    public void addRestaurant(Restaurant restaurant) {
        this.restaurant=restaurant;
        // הוספת מסעדה לרשימה
    }
}
