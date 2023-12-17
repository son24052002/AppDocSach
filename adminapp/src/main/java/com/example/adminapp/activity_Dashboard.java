package com.example.adminapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.Toast;

public class activity_Dashboard extends AppCompatActivity {


    GridLayout grid_layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        grid_layout = (GridLayout) findViewById(R.id.grid_layout);


        CardView cv1;
        CardView cv2;
        CardView cv3;
        CardView cv4;
        cv1 = findViewById(R.id.cv1);
        cv2 = findViewById(R.id.cv2);
        cv3 = findViewById(R.id.cv3);
        cv4 = findViewById(R.id.cv4);

        cv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_Dashboard.this, CategoriesListActivity.class);
                startActivity(intent);
            }
        });
        cv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_Dashboard.this, CategoryAddActivity.class);
                startActivity(intent);
            }
        });
        cv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_Dashboard.this, PdfListActivity.class);
                startActivity(intent);
            }
        });
        cv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_Dashboard.this, PdfAddActivity.class);
                startActivity(intent);
            }
        });
    }

//    private void setSingleEvent(GridLayout gridLayout) {
//        for (int i=0; i < grid_layout.getChildCount(); i ++){
//            CardView cardView = (CardView) grid_layout.getChildAt(i);
//            final  int finalI = i;
//            if(i==0){
//                cardView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        if (finalI ==0){
//                            Intent intent = new Intent(activity_Dashboard.this, PdfListActivity.class);
//                            startActivity(intent);
//
//                        }
//
//                        else if (finalI ==1){
//                            Intent intent = new Intent(activity_Dashboard.this, PdfAddActivity.class);
//                            startActivity(intent);
//                        }
//
//
//                    }
//                });
//            } else if (i==1) {
//                cardView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        if (finalI ==0){
//                            Intent intent = new Intent(activity_Dashboard.this, CategoriesListActivity.class);
//                            startActivity(intent);
//
//                        }
//
//                        else if (finalI ==1){
//                            Intent intent = new Intent(activity_Dashboard.this, CategoryAddActivity.class);
//                            startActivity(intent);
//                        }
//
//
//                    }
//                });
//            }
//
//        }
//    }
}