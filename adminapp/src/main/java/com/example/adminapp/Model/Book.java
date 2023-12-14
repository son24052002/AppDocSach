package com.example.adminapp.Model;

public class Book {
    String id, BookTitle, CategoryId, urlImg, urlPdf, BookDes;
    long timestamp;

    public Book() {
    }



    public Book(String id, String bookTitle, String categoryId, String urlImg, String urlPdf, long timestamp, String bookDes) {
        this.id = id;
        BookTitle = bookTitle;
        CategoryId = categoryId;
        this.urlImg = urlImg;
        this.urlPdf = urlPdf;
        this.timestamp = timestamp;
        this.BookDes = bookDes;
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

    public String getUrlImg() {
        return urlImg;
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }

    public String getUrlPdf() {
        return urlPdf;
    }

    public void setUrlPdf(String urlPdf) {
        this.urlPdf = urlPdf;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    public String getBookDes() {
        return BookDes;
    }

    public void setBookDes(String bookDes) {
        BookDes = bookDes;
    }
}
