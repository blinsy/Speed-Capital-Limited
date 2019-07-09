package com.example.speedcapitalltd.Fragments;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.speedcapitalltd.Activities.MainActivity;
import com.example.speedcapitalltd.Adapters.ViewPagerAdapter;
import com.example.speedcapitalltd.R;

public class FragmentActivity extends AppCompatActivity {
private TabLayout tabLayout;
private ViewPager viewPager;
private ViewPagerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
getSupportActionBar().hide();
        tabLayout=findViewById(R.id.tablayout);
        viewPager= findViewById(R.id.viewPager);
        adapter= new ViewPagerAdapter(getSupportFragmentManager());
//Add fragments here

        adapter.AddFragment(new FragmentAll(),"All");
        adapter.AddFragment(new FragmentComplete(),"Complete \n Payments");
        adapter.AddFragment(new FragmentIncomplete(),"Pending ".concat("\n").concat("Payments"));

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    public void back(View view) {
        Intent intent= new Intent(FragmentActivity.this, MainActivity.class);
        startActivity(intent);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        boolean homeSelected=false;
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
            //Bungee.shrink(InvoiceStatusActivity.this);
            homeSelected= true;
        }

        return homeSelected;
    }



}
