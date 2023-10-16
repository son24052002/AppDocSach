package com.example.appdocsach;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

        return myview;


    }


}