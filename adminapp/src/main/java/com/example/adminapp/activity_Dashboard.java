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

        setSingleEvent(grid_layout);
    }

    private void setSingleEvent(GridLayout gridLayout) {
        for (int i=0; i < grid_layout.getChildCount(); i ++){
            CardView cardView = (CardView) grid_layout.getChildAt(i);
            final  int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (finalI ==0){
                        Intent intent = new Intent(activity_Dashboard.this, ActivityOne.class);
                        startActivity(intent);

                    }
                    else {
                        Toast.makeText(activity_Dashboard.this, "No Item", Toast.LENGTH_SHORT).show();
                    }
//                    else if (finalI ==1){
//                        Intent intent = new Intent(activity_Dashboard.this, ActivityOne.class);
//                    }


                }
            });
        }
    }
}