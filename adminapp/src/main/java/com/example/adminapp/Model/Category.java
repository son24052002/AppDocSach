package com.example.adminapp.Model;

public class Category {
    String id, categoryName;
    long timestamp;

    public Category() {
    }

    public Category(String id, String categoryName, long timestamp) {
        this.id = id;
        this.categoryName = categoryName;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
