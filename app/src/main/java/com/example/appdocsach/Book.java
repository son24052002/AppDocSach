package com.example.appdocsach;

public class Book {
    private String BookName;
    private String AuthorName;
    private int PublicYear;
    private int CatId;
    private String Des;
    private String ImageUrl;

    public Book() {
    }

    public Book(String bookName, String authorName, int publicYear, int catId, String des, String imageUrl) {
        BookName = bookName;
        AuthorName = authorName;
        PublicYear = publicYear;
        CatId = catId;
        Des = des;
        ImageUrl = imageUrl;
    }

    public String getBookName() {
        return BookName;
    }

    public void setBookName(String bookName) {
        BookName = bookName;
    }

    public String getAuthorName() {
        return AuthorName;
    }

    public void setAuthorName(String authorName) {
        AuthorName = authorName;
    }

    public int getPublicYear() {
        return PublicYear;
    }

    public void setPublicYear(int publicYear) {
        PublicYear = publicYear;
    }

    public int getCatId() {
        return CatId;
    }

    public void setCatId(int catId) {
        CatId = catId;
    }

    public String getDes() {
        return Des;
    }

    public void setDes(String des) {
        Des = des;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }
}
