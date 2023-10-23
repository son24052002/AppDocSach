package com.example.appdocsach;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Message;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {
    private static final String DB_NAME = "mydatabase";
    private static final int DB_VERSION =2;

    public DBHandler( Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    private static final String CREATE_BOOK_TABLE = "CREATE TABLE Book ("+
            "BookId INTEGER PRIMARY KEY AUTOINCREMENT,"+
            "BookName TEXT, "+
            "Author TEXT,"+
            "Publication_year INTEGER,"+
            "Description TEXT,"+
            "CatId INTEGER,"+
            "ImageUrl TEXT,"+
            "FOREIGN KEY(CatId) REFERENCES Category(CatId)"+
            ");";


    private static final String CREATE_CATEGORY_TABLE = "CREATE TABLE Category (" +
            "CatId INTEGER PRIMARY KEY, " +
            "CatName TEXT, " +
            "Description TEXT"+
            ");";
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BOOK_TABLE);
        db.execSQL(CREATE_CATEGORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("DROP TABLE IF EXISTS Book");
        db.execSQL("DROP TABLE IF EXISTS Category");

        onCreate(db);
    }

    public void addBook(Book book){
        SQLiteDatabase database = getReadableDatabase();
        ContentValues value = new ContentValues();
        value.put("BookName", book.getBookName());
        value.put("Author",book.getAuthorName());
        value.put("Publication_year",book.getPublicYear());
        value.put("Description",book.getDes());
        value.put("CatId",book.getCatId());
        value.put("ImageUrl",book.getImageUrl());
        database.insert("Book",null,value);
    }

    public List<Book> getAll(){
        List<Book> bookList = new ArrayList<>();
        String getAll = "SELECT * FROM Book";
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(getAll, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            String BookName, ImageUrl, Author,Description;
            int id,Publication_year, CatId;
            id = cursor.getInt(0);
            BookName = cursor.getString(1);
            Author = cursor.getString(2);
            Publication_year = cursor.getInt(3);
            Description = cursor.getString(4);
            CatId = cursor.getInt(5);
            ImageUrl = cursor.getString(6);



            cursor.moveToNext();
        }
        cursor.close();
        return bookList;
    }
}
