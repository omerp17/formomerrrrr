package com.example.formomerpeled.models;

import java.io.Serializable;

public class Restaurant  implements Serializable {
        private String id, uId;
        private String name;
        private String cuisineType;
        private String address;
        private String city;
        private String phoneNumber;
        private String glutenFreeMenuItems;
        private String domain;
        private String imageCode;
        private float rating;  // שדה לדירוג שנבחר


        public String getuId() {
                return uId;
        }

        public void setuId(String uId) {
                this.uId = uId;
        }

        public Restaurant() {
        }

        public Restaurant(String id, String uId, String name, String cuisineType, String address, String city, String phoneNumber, String glutenFreeMenuItems, String domain, String imageCode, float rating) {
                this.id = id;
                this.uId = uId;
                this.name = name;
                this.cuisineType = cuisineType;
                this.address = address;
                this.city = city;
                this.phoneNumber = phoneNumber;
                this.glutenFreeMenuItems = glutenFreeMenuItems;
                this.domain = domain;
                this.imageCode = imageCode;
                this.rating=rating;

        }

        // גטרים
        public String getId() {
                return id;
        }

        public String getName() {
                return name;
        }

        public String getCuisineType() {
                return cuisineType;
        }

        public String getAddress() {
                return address;
        }

        public String getCity() {
                return city;
        }

        public String getPhoneNumber() {
                return phoneNumber;
        }

        public String getGlutenFreeMenuItems() {
                return glutenFreeMenuItems;
        }

        public String getDomain() {
                return domain;
        }

        public String getImageCode() {
                return imageCode;
        }

        public float getRating() {
                return rating;
        }


        @Override
        public String toString() {
                return "Restaurant{" +
                        "id='" + id + '\'' +
                        ", name='" + name + '\'' +
                        ", cuisineType='" + cuisineType + '\'' +
                        ", address='" + address + '\'' +
                        ", city='" + city + '\'' +
                        ", phoneNumber='" + phoneNumber + '\'' +
                        ", glutenFreeMenuItems='" + glutenFreeMenuItems + '\'' +
                        ", domain='" + domain + '\'' +
                        ", imageCode='" + imageCode + '\'' +
                        ", rating=" + rating +
                        '}';
        }

        public void setRating(float rating) {
                this.rating = rating;


        }


}
