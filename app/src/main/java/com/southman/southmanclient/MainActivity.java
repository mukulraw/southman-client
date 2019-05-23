package com.southman.southmanclient;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;

    ViewPager pager;

    SmartTabLayout tabLayout;

    PagerAdapter adapter;

    ImageView history, logout;

    TextView cname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cname = findViewById(R.id.location);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        /*ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.open, R.string.close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
*/
        pager = findViewById(R.id.pager);
        tabLayout = findViewById(R.id.tablayout);

        String lname = SharePreferenceUtils.getInstance().getString("name");

        cname.setText(lname);

        adapter = new PagerAdapter(getSupportFragmentManager(), 3);
        pager.setAdapter(adapter);
        tabLayout.setViewPager(pager);

    }

    public class PagerAdapter extends FragmentStatePagerAdapter {

        String[] titles = {
                "Pending Orders",
                "New Bills",
                "Scratch Coupons"
        };

        public PagerAdapter(FragmentManager fm, int list) {
            super(fm);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int i) {

            if (i == 0) {

                return new Orders();
            } else if (i == 1) {
                return new Bills();
            } else if (i == 2) {

                return new Scratch();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

}
