package com.example.adminapp.Model;

public class Book {
    String id, BookTitle, CategoryId, imgUrl, url, BookDescription;
    long timestamp;

    public Book() {
    }



    public Book(String id, String bookTitle, String categoryId, String imgUrl, String urlPdf, long timestamp, String BookDescription) {
        this.id = id;
        this.BookTitle = bookTitle;
        this.CategoryId = categoryId;
        this.imgUrl = imgUrl;
        this.url = urlPdf;
        this.timestamp = timestamp;
        this.BookDescription = BookDescription;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBookDescription() {
        return BookDescription;
    }

    public void setBookDescription(String bookDescription) {
        BookDescription = bookDescription;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookTitle() {
        return BookTitle;
    }

    public void setBookTitle(String bookTitle) {
        BookTitle = bookTitle;
    }

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String categoryId) {
        CategoryId = categoryId;
    }





    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

}
