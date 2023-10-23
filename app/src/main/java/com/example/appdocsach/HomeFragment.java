package com.example.appdocsach;

import android.os.Bundle;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

public class HomeFragment extends Fragment {




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myview = inflater.inflate(R.layout.fragment_home, container, false);

        TabLayout tabLayout = myview.findViewById(R.id.tabLayout);
        ViewPager viewPager = myview.findViewById(R.id.viewPager);

        // Tạo và thiết lập adapter cho ViewPager
        TabPageAdapter adapter = new TabPageAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);

        // Kết nối TabLayout và ViewPager
        tabLayout.setupWithViewPager(viewPager);


        //navigation

        Toolbar toolbar = myview.findViewById(R.id.toolbar);
        DrawerLayout drawerLayout = myview.findViewById(R.id.drawerLayout);
        NavigationView navigationView = myview.findViewById(R.id.navigationView);
        //ListView listView = myview.findViewById(R.id.listview);

        //actionToolBar();

        toolbar.setNavigationIcon(R.drawable.baseline_menu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });


        return myview;


    }

//    private void actionToolBar() {
//        toolbar
//        toolbar.setNavigationIcon(R.drawable.baseline_menu);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                drawerLayout.openDrawer(GravityCompat.START);
//            }
//        });
//    }


}