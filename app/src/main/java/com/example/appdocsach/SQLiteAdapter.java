package com.example.appdocsach;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class SQLiteAdapter extends BaseAdapter {
    List<Book> bookList;

    public SQLiteAdapter(List<Book> bookList){
        this.bookList = bookList;
    }
    @Override
    public int getCount() {
        return this.bookList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_book, viewGroup, false);
        ImageView imageView = view.findViewById(R.id.img_book);
        TextView tvBookName = view.findViewById(R.id.book_title);

        Book book = bookList.get(i);
        tvBookName.setText(book.getBookName());
        return view;
    }
}
