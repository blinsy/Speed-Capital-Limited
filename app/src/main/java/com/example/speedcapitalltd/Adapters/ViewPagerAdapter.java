package com.example.speedcapitalltd.Adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {


    private  final List<Fragment> lstfragment = new ArrayList<>();
    private final List<String> lstTittles = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return lstfragment.get(position);
    }

    @Override
    public int getCount() {
        return lstTittles.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return lstTittles.get(position);
    }

    public  void  AddFragment (Fragment fragment ,String title){

        lstfragment.add(fragment);
        lstTittles.add(title);
    }
}
