package com.example.appdocsach;



import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabPageAdapter extends FragmentPagerAdapter {



    public TabPageAdapter(FragmentManager fm) {
        super(fm);
    }
    @Override
    public Fragment getItem(int position){
        switch (position){
            case 0:
                return new KhamPhaFragment();
            case 1:
                return new PhoBienFragment();
            default:
                return null;
        }
    }



    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Tab 1";
            case 1:
                return "Tab 2";
            // Thêm tiêu đề cho các tab khác tại đây
            default:
                return null;
        }
    }
}
