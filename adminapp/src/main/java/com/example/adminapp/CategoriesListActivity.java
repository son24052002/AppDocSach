package com.example.adminapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.example.adminapp.Adapter.CategoryAdapter;
import com.example.adminapp.Model.Category;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CategoriesListActivity extends AppCompatActivity {

    private ArrayList<Category> categoryArrayList;
    private CategoryAdapter categoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_categories_list);

        loadCategories();
    }

    private void loadCategories() {
        categoryArrayList = new ArrayList<>();

        //lay tat ca category tu database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Categories");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //clear arraylist before adding data into it
                categoryArrayList.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    //get data
                    Category model = dataSnapshot.getValue(Category.class);

                    //add to arraylist
                    categoryArrayList.add(model);
                }
                //setup adapter
                categoryAdapter = new CategoryAdapter(CategoriesListActivity.this, categoryArrayList);

                //set adapter to recycleview
                RecyclerView recyclerView = findViewById(R.id.categoryRV);
                recyclerView.setAdapter(categoryAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}