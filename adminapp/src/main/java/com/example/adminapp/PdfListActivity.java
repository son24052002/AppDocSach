package com.example.adminapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ImageButton;

import com.example.adminapp.Adapter.BookAdapter;
import com.example.adminapp.Model.Book;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PdfListActivity extends AppCompatActivity {

    private ArrayList<Book> bookArrayList;
    private BookAdapter bookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_list);
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        loadListBook();
    }

    private void loadListBook() {
        bookArrayList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Books");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bookArrayList.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    //getdata
                    Book model = dataSnapshot.getValue(Book.class);
                    //add data
                    bookArrayList.add(model);
                }
                //setup adapter
                bookAdapter = new BookAdapter(PdfListActivity.this, bookArrayList);
                //set adapter to recycleview
                RecyclerView recyclerView = findViewById(R.id.bookRv);
                recyclerView.setAdapter(bookAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}