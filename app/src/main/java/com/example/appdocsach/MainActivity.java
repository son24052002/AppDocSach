package com.example.appdocsach;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.appdocsach.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    //FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DBHandler handler = new DBHandler(MainActivity.this);

        //Book book = new Book("Bồ Câu Bay Đi Tìm Bà","Walter Macken",2021,1,"Biết bao cảm hứng và tưởng tượng hẳn sẽ được khơi lên từ đây, câu chuyện về anh em nhà họ Dove - hai chú bồ câu bé nhỏ, gan dạ - bỏ nhà vượt biển, tìm kiếm một ngôi làng không biết tên, ở một đất nước không quen thuộc, để đến với bà ngoại - biểu tượng của tình yêu thương mà chúng luôn khao khát.","drawable/bocaubayditimba.jpg");

        //handler.addBook(book);
        bottomNavigationView = findViewById(R.id.bottom_NavigationView);
        //frameLayout = findViewById(R.id.frame_layout);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame_layout, new HomeFragment());
        fragmentTransaction.commit();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if(item.getItemId()==R.id.home){

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.frame_layout, new HomeFragment());
                    fragmentTransaction.commit();

                } else if (item.getItemId()==R.id.profile) {

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.frame_layout, new ProfileFragment());
                    fragmentTransaction.commit();

                } else if (item.getItemId()==R.id.setting) {

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.frame_layout, new SettingFragment());
                    fragmentTransaction.commit();
                }

                return true;
            }
        });



    }
    public void change(View login_view){
        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
        startActivity(intent);
    }

}