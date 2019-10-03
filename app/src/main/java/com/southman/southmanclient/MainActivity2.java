package com.southman.southmanclient;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.io.IOException;

public class MainActivity2 extends AppCompatActivity {

    Toolbar toolbar;

    ViewPager pager;

    SmartTabLayout tabLayout;

    PagerAdapter adapter;

    ImageView logout, rupee;

    TextView cname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        cname = findViewById(R.id.location);

        toolbar = findViewById(R.id.toolbar);
        rupee = findViewById(R.id.rupee);

        logout = findViewById(R.id.logout);

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

        adapter = new PagerAdapter(getSupportFragmentManager(), 2);
        pager.setAdapter(adapter);
        tabLayout.setViewPager(pager);

        //pager.setOffscreenPageLimit(3);

        rupee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity2.this, Clients.class);
                startActivity(intent);

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(MainActivity2.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.logout_dialog);
                dialog.show();

                Button ookk = dialog.findViewById(R.id.button2);
                Button canc = dialog.findViewById(R.id.button4);

                canc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


                ookk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    FirebaseInstanceId.getInstance().deleteInstanceId();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();


                        SharePreferenceUtils.getInstance().deletePref();

                        Intent intent = new Intent(MainActivity2.this, Splash.class);
                        startActivity(intent);

                        finishAffinity();

                    }
                });


            }
        });

        tabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {

                Log.d("pageaa", String.valueOf(i));


            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });


    }

    public class PagerAdapter extends FragmentStatePagerAdapter {

        String[] titles = {
                "Voucher Store",
                "Redeem Store",
                "Current Profit"
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

                return new Bills2();
            }
            else if (i == 1)
            {
                return new Bills22();
            }
            else
            {
                return new profit();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

}
