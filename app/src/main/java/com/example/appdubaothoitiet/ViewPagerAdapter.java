package com.example.appdubaothoitiet;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0 :
                return new HomeFragment();
            case 1:

                return new NextDayFragment();
            default:
                return new HomeFragment();

        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position){
            case 0:
                title = "HÔM NAY";
                break;
            case 1:
                title = "NGÀY TIẾP THEO";
                break;
        }
        return title;
    }
}
