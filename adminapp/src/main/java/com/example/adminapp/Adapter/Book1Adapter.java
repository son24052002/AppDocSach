package com.example.adminapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminapp.Model.Book;
import com.example.adminapp.R;
import com.github.barteksc.pdfviewer.PDFView;

import java.util.ArrayList;

public class Book1Adapter extends RecyclerView.Adapter<Book1Adapter.HolderBook1>{
    private Context context;
    private ArrayList<Book> bookArrayList;

    public Book1Adapter(Context context, ArrayList<Book> bookArrayList) {
        this.context = context;
        this.bookArrayList = bookArrayList;
    }

    @NonNull
    @Override
    public HolderBook1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_books, parent, false);
        return new HolderBook1(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderBook1 holder, int position) {
        Book model = bookArrayList.get(position);
        String bookTitle = model.getBookTitle();
        String bookDes = model.getBookDescription();

        holder.bookName.setText(bookTitle);
        holder.bookDes.setText(bookDes);
    }

    @Override
    public int getItemCount() {
        return bookArrayList.size();
    }

    class HolderBook1 extends RecyclerView.ViewHolder{

        TextView bookName;
        TextView bookDes;
        TextView Category;
        ImageButton btnMore;
        ImageView imgBook;
        public HolderBook1(@NonNull View itemView) {
            super(itemView);


            bookName = itemView.findViewById(R.id.bookName);
            bookDes = itemView.findViewById(R.id.bookDes);
            Category = itemView.findViewById(R.id.Category);
            btnMore = itemView.findViewById(R.id.btnMore);
            imgBook = itemView.findViewById(R.id.imgBook);
        }
    }

}
