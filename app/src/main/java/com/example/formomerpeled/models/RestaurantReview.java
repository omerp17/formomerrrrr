package com.example.formomerpeled.models;

import java.sql.Date;
import java.time.LocalDateTime;

public class RestaurantReview {

    private String uId;
    private String feedback;

    // min 0 max 5
    private float userRating;

    private LocalDateTime localDateTime;

    public RestaurantReview() {
    }

    public RestaurantReview(String uId, String feedback, float userRating, LocalDateTime localDateTime) {
        this.uId = uId;
        this.feedback = feedback;
        this.userRating = userRating;
        this.localDateTime = localDateTime;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public float getUserRating() {
        return userRating;
    }

    public void setUserRating(float userRating) {
        this.userRating = userRating;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    @Override
    public String toString() {
        return "RestaurantReview{" +
                "uId='" + uId + '\'' +
                ", feedback='" + feedback + '\'' +
                ", userRating=" + userRating +
                ", localDateTime=" + localDateTime +
                '}';
    }
}
