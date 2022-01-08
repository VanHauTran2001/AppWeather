package com.example.appdubaothoitiet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.appdubaothoitiet.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity implements HomeFragment.ISendDataFragment {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        binding.viewPager.setAdapter(viewPagerAdapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);
    }
    //nhan du lieu tu HomeFragment va truyen sang NextdayFragment
    @Override
    public void sendData(String sendCity) {
        NextDayFragment nextDayFragment = (NextDayFragment) getSupportFragmentManager().findFragmentById(R.id.viewPager);
        nextDayFragment.receiveDataFragment(sendCity);
    }
}