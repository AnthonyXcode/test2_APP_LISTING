package com.anthony.example.test2_app_listing;

/**
 * Created by anthony on 1/2/2017.
 */

public class ListingItem {
    private int number;
    private String iconLink;
    private String name;
    private String type;
    private String id;
    private double averageUserRating;
    private int userRatingCount;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getIconLink() {
        return iconLink;
    }

    public void setIconLink(String iconLink) {
        this.iconLink = iconLink;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getAverageUserRating() {
        return averageUserRating;
    }
    public void setAverageUserRating(double averageUserRating) {
        this.averageUserRating = averageUserRating;
    }

    public int getUserRatingCount() {
        return userRatingCount;
    }
    public void setUserRatingCount(int userRatingCount) {
        this.userRatingCount = userRatingCount;
    }
}
