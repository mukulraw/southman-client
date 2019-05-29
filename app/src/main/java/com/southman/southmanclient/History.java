package com.southman.southmanclient;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.method.HideReturnsTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.southman.southmanclient.orderPOJO.Datum;
import com.southman.southmanclient.orderPOJO.orderBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class History extends AppCompatActivity {

    Toolbar toolbar;
    ProgressBar progress;
    ViewPager grid;
    GridLayoutManager manager;
    List<Datum> list;
    TabLayout tabs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        toolbar = findViewById(R.id.toolbar);
        grid = findViewById(R.id.grid);
        tabs = findViewById(R.id.tabs);
        progress = findViewById(R.id.progress);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationIcon(R.drawable.arrowleft);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        toolbar.setTitle("History");


        tabs.addTab(tabs.newTab().setText("VOUCHER"));
        tabs.addTab(tabs.newTab().setText("REDEEM"));
        tabs.addTab(tabs.newTab().setText("SCRATCH"));

        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());

        grid.setAdapter(adapter);

        tabs.setupWithViewPager(grid);

        tabs.getTabAt(0).setText("VOUCHER");
        tabs.getTabAt(1).setText("REDEEM");
        tabs.getTabAt(2).setText("SCRATCH");

        grid.setOffscreenPageLimit(2);

    }

    class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            if (i == 0) {
                return new voucer();
            } else if (i == 1) {
                return new redeem();
            } else {
                return new scratch();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    public static class voucer extends Fragment {

        ProgressBar progress;
        RecyclerView grid;
        GridLayoutManager manager;
        List<Datum> list;
        BillAdapter adapter;
        TextView date;
        LinearLayout linear;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.rh_layout, container, false);

            grid = view.findViewById(R.id.grid);
            date = view.findViewById(R.id.date);
            progress = view.findViewById(R.id.progress);

            linear = view.findViewById(R.id.linear);
            list = new ArrayList<>();


            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = df.format(c);

            Log.d("dddd" , formattedDate);

            date.setText("Date - " + formattedDate + " (click to change)");






            adapter = new BillAdapter(getActivity(), list);
            manager = new GridLayoutManager(getActivity(), 1);
            grid.setAdapter(adapter);
            grid.setLayoutManager(manager);


            date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    final Dialog dialog = new Dialog(getActivity());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(true);
                    dialog.setContentView(R.layout.date_dialog);
                    dialog.show();


                    final DatePicker picker = dialog.findViewById(R.id.date);
                    Button ok = dialog.findViewById(R.id.ok);

                    long now = System.currentTimeMillis() - 1000;
                    picker.setMaxDate(now);

                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            int year = picker.getYear();
                            int month = picker.getMonth();
                            int day = picker.getDayOfMonth();

                            Calendar calendar = Calendar.getInstance();
                            calendar.set(year, month, day);

                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                            String strDate = format.format(calendar.getTime());

                            dialog.dismiss();

                            date.setText("Date - " + strDate + " (click to change)");



                            progress.setVisibility(View.VISIBLE);

                            Bean b = (Bean) getActivity().getApplicationContext();

                            Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl(b.baseurl)
                                    .addConverterFactory(ScalarsConverterFactory.create())
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();

                            AllApiIneterface cr = retrofit.create(AllApiIneterface.class);


                            progress.setVisibility(View.VISIBLE);

                            Call<orderBean> call = cr.getOrders2(SharePreferenceUtils.getInstance().getString("id") , strDate);

                            call.enqueue(new Callback<orderBean>() {
                                @Override
                                public void onResponse(Call<orderBean> call, Response<orderBean> response1) {

                                    //Toast.makeText(History.this, String.valueOf(response1.body().getData().size()), Toast.LENGTH_SHORT).show();

                                    if (response1.body().getStatus().equals("1"))
                                    {
                                        linear.setVisibility(View.GONE);
                                    }
                                    else

                                    {
                                        linear.setVisibility(View.VISIBLE);
                                    }

                                    adapter.setData(response1.body().getData());

                                    progress.setVisibility(View.GONE);
                                }

                                @Override
                                public void onFailure(Call<orderBean> call, Throwable t) {
                                    progress.setVisibility(View.GONE);
                                    Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();
                                }
                            });



                        }
                    });



                }
            });

            progress.setVisibility(View.VISIBLE);

            Bean b = (Bean) getActivity().getApplicationContext();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(b.baseurl)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            AllApiIneterface cr = retrofit.create(AllApiIneterface.class);


            progress.setVisibility(View.VISIBLE);

            Call<orderBean> call = cr.getOrders2(SharePreferenceUtils.getInstance().getString("id") , formattedDate);

            call.enqueue(new Callback<orderBean>() {
                @Override
                public void onResponse(Call<orderBean> call, Response<orderBean> response1) {

                    //Toast.makeText(History.this, String.valueOf(response1.body().getData().size()), Toast.LENGTH_SHORT).show();

                    if (response1.body().getStatus().equals("1"))
                    {
                        linear.setVisibility(View.GONE);
                    }
                    else

                    {
                        linear.setVisibility(View.VISIBLE);
                    }

                    adapter.setData(response1.body().getData());

                    progress.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<orderBean> call, Throwable t) {
                    progress.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();
                }
            });


            return view;
        }





    }

    public static class redeem extends Fragment {

        ProgressBar progress;
        RecyclerView grid;
        GridLayoutManager manager;
        List<Datum> list;
        BillAdapter adapter;
        TextView date;
        LinearLayout linear;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.rh_layout, container, false);

            grid = view.findViewById(R.id.grid);
            date = view.findViewById(R.id.date);
            progress = view.findViewById(R.id.progress);

            linear = view.findViewById(R.id.linear);
            list = new ArrayList<>();


            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = df.format(c);

            Log.d("dddd" , formattedDate);

            date.setText("Date - " + formattedDate + " (click to change)");






            adapter = new BillAdapter(getActivity(), list);
            manager = new GridLayoutManager(getActivity(), 1);
            grid.setAdapter(adapter);
            grid.setLayoutManager(manager);


            date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    final Dialog dialog = new Dialog(getActivity());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(true);
                    dialog.setContentView(R.layout.date_dialog);
                    dialog.show();


                    final DatePicker picker = dialog.findViewById(R.id.date);
                    Button ok = dialog.findViewById(R.id.ok);

                    long now = System.currentTimeMillis() - 1000;
                    picker.setMaxDate(now);

                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            int year = picker.getYear();
                            int month = picker.getMonth();
                            int day = picker.getDayOfMonth();

                            Calendar calendar = Calendar.getInstance();
                            calendar.set(year, month, day);

                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                            String strDate = format.format(calendar.getTime());

                            dialog.dismiss();

                            date.setText("Date - " + strDate + " (click to change)");



                            progress.setVisibility(View.VISIBLE);

                            Bean b = (Bean) getActivity().getApplicationContext();

                            Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl(b.baseurl)
                                    .addConverterFactory(ScalarsConverterFactory.create())
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();

                            AllApiIneterface cr = retrofit.create(AllApiIneterface.class);


                            progress.setVisibility(View.VISIBLE);

                            Call<orderBean> call = cr.getOrders22(SharePreferenceUtils.getInstance().getString("id") , strDate);

                            call.enqueue(new Callback<orderBean>() {
                                @Override
                                public void onResponse(Call<orderBean> call, Response<orderBean> response1) {

                                    //Toast.makeText(History.this, String.valueOf(response1.body().getData().size()), Toast.LENGTH_SHORT).show();

                                    if (response1.body().getStatus().equals("1"))
                                    {
                                        linear.setVisibility(View.GONE);
                                    }
                                    else

                                    {
                                        linear.setVisibility(View.VISIBLE);
                                    }

                                    adapter.setData(response1.body().getData());

                                    progress.setVisibility(View.GONE);
                                }

                                @Override
                                public void onFailure(Call<orderBean> call, Throwable t) {
                                    progress.setVisibility(View.GONE);
                                    Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();
                                }
                            });



                        }
                    });



                }
            });

            progress.setVisibility(View.VISIBLE);

            Bean b = (Bean) getActivity().getApplicationContext();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(b.baseurl)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            AllApiIneterface cr = retrofit.create(AllApiIneterface.class);


            progress.setVisibility(View.VISIBLE);

            Call<orderBean> call = cr.getOrders22(SharePreferenceUtils.getInstance().getString("id") , formattedDate);

            call.enqueue(new Callback<orderBean>() {
                @Override
                public void onResponse(Call<orderBean> call, Response<orderBean> response1) {

                    //Toast.makeText(History.this, String.valueOf(response1.body().getData().size()), Toast.LENGTH_SHORT).show();

                    if (response1.body().getStatus().equals("1"))
                    {
                        linear.setVisibility(View.GONE);
                    }
                    else

                    {
                        linear.setVisibility(View.VISIBLE);
                    }

                    adapter.setData(response1.body().getData());

                    progress.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<orderBean> call, Throwable t) {
                    progress.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();
                }
            });


            return view;
        }





    }

    public static class scratch extends Fragment {

        ProgressBar progress;
        RecyclerView grid;
        GridLayoutManager manager;
        List<Datum> list;
        BillAdapter adapter;
        TextView date;
        LinearLayout linear;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.rh_layout, container, false);

            grid = view.findViewById(R.id.grid);
            date = view.findViewById(R.id.date);
            progress = view.findViewById(R.id.progress);

            linear = view.findViewById(R.id.linear);
            list = new ArrayList<>();


            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = df.format(c);

            Log.d("dddd" , formattedDate);

            date.setText("Date - " + formattedDate + " (click to change)");






            adapter = new BillAdapter(getActivity(), list);
            manager = new GridLayoutManager(getActivity(), 1);
            grid.setAdapter(adapter);
            grid.setLayoutManager(manager);


            date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    final Dialog dialog = new Dialog(getActivity());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(true);
                    dialog.setContentView(R.layout.date_dialog);
                    dialog.show();


                    final DatePicker picker = dialog.findViewById(R.id.date);
                    Button ok = dialog.findViewById(R.id.ok);

                    long now = System.currentTimeMillis() - 1000;
                    picker.setMaxDate(now);

                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            int year = picker.getYear();
                            int month = picker.getMonth();
                            int day = picker.getDayOfMonth();

                            Calendar calendar = Calendar.getInstance();
                            calendar.set(year, month, day);

                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                            String strDate = format.format(calendar.getTime());

                            dialog.dismiss();

                            date.setText("Date - " + strDate + " (click to change)");



                            progress.setVisibility(View.VISIBLE);

                            Bean b = (Bean) getActivity().getApplicationContext();

                            Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl(b.baseurl)
                                    .addConverterFactory(ScalarsConverterFactory.create())
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();

                            AllApiIneterface cr = retrofit.create(AllApiIneterface.class);


                            progress.setVisibility(View.VISIBLE);

                            Call<orderBean> call = cr.getOrders23(SharePreferenceUtils.getInstance().getString("id") , strDate);

                            call.enqueue(new Callback<orderBean>() {
                                @Override
                                public void onResponse(Call<orderBean> call, Response<orderBean> response1) {

                                    //Toast.makeText(History.this, String.valueOf(response1.body().getData().size()), Toast.LENGTH_SHORT).show();

                                    if (response1.body().getStatus().equals("1"))
                                    {
                                        linear.setVisibility(View.GONE);
                                    }
                                    else

                                    {
                                        linear.setVisibility(View.VISIBLE);
                                    }

                                    adapter.setData(response1.body().getData());

                                    progress.setVisibility(View.GONE);
                                }

                                @Override
                                public void onFailure(Call<orderBean> call, Throwable t) {
                                    progress.setVisibility(View.GONE);
                                    Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();
                                }
                            });



                        }
                    });



                }
            });

            progress.setVisibility(View.VISIBLE);

            Bean b = (Bean) getActivity().getApplicationContext();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(b.baseurl)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            AllApiIneterface cr = retrofit.create(AllApiIneterface.class);


            progress.setVisibility(View.VISIBLE);

            Call<orderBean> call = cr.getOrders23(SharePreferenceUtils.getInstance().getString("id") , formattedDate);

            call.enqueue(new Callback<orderBean>() {
                @Override
                public void onResponse(Call<orderBean> call, Response<orderBean> response1) {

                    //Toast.makeText(History.this, String.valueOf(response1.body().getData().size()), Toast.LENGTH_SHORT).show();

                    if (response1.body().getStatus().equals("1"))
                    {
                        linear.setVisibility(View.GONE);
                    }
                    else

                    {
                        linear.setVisibility(View.VISIBLE);
                    }

                    adapter.setData(response1.body().getData());

                    progress.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<orderBean> call, Throwable t) {
                    progress.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();
                }
            });


            return view;
        }





    }

    /*@Override
    public void onResume() {
        super.onResume();


        progress.setVisibility(View.VISIBLE);

        Bean b = (Bean) getApplicationContext();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.baseurl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AllApiIneterface cr = retrofit.create(AllApiIneterface.class);


        Call<orderBean> call = cr.getOrders2(SharePreferenceUtils.getInstance().getString("id"));

        call.enqueue(new Callback<orderBean>() {
            @Override
            public void onResponse(Call<orderBean> call, Response<orderBean> response) {

                if (response.body().getStatus().equals("1"))
                {
                    adapter.setData(response.body().getData());
                }
                else
                {
                    Toast.makeText(History.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }

                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<orderBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });


    }*/

    static class BillAdapter extends RecyclerView.Adapter<BillAdapter.ViewHolder>
    {
        Context context;
        List<Datum> list = new ArrayList<>();

        public BillAdapter(Context context , List<Datum> list)
        {
            this.context = context;
            this.list = list;
        }

        void setData(List<Datum> list)
        {
            this.list = list;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.order_list_model , viewGroup , false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int i) {

            final Datum item = list.get(i);

            holder.code.setText("Item - " + item.getCode());
            holder.date.setText(item.getCreated());

            holder.user.setText(item.getUser());

            switch (item.getText()) {
                case "perks":
                    holder.type.setText("VOUCHER STORE - " + item.getId());
                    holder.type.setTextColor(Color.parseColor("#009688"));

                    holder.price.setText("Benefits - " + item.getPrice() + " credits");

                    float pr = Float.parseFloat(item.getPrice());
                    float pa = Float.parseFloat(item.getCashValue());

                    holder.paid.setText("Pending benefits - " + String.valueOf(pr - pa) + " credits");

                    holder.paid.setVisibility(View.VISIBLE);
                    holder.price.setVisibility(View.VISIBLE);

                    break;
                case "cash":
                    holder.type.setText("REDEEM STORE - " + item.getId());
                    holder.type.setTextColor(Color.parseColor("#689F38"));
                    holder.price.setText("Price - " + item.getPrice() + " Rs.");

                    float pr1 = Float.parseFloat(item.getPrice());
                    float pa1 = Float.parseFloat(item.getCashValue());

                    holder.paid.setText("Collect from customer - " + String.valueOf(pr1 - pa1) + " Rs.");

                    holder.paid.setVisibility(View.VISIBLE);
                    holder.price.setVisibility(View.VISIBLE);

                    break;
                case "scratch":
                    holder.type.setText("SCRATCH CARD - " + item.getId());

                    holder.price.setText("Discount - " + item.getCashValue() + " Rs.");

                    holder.type.setTextColor(Color.parseColor("#F9A825"));
                    holder.paid.setVisibility(View.GONE);
                    //holder.price.setVisibility(View.GONE);

                    break;
            }


            holder.complete.setText(item.getStatus());

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder
        {
            TextView code, date, type , user , price , paid;
            Button complete;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                code = itemView.findViewById(R.id.code);
                date = itemView.findViewById(R.id.date);
                type = itemView.findViewById(R.id.type);
                user = itemView.findViewById(R.id.user);
                price = itemView.findViewById(R.id.price);
                paid = itemView.findViewById(R.id.paid);
                complete = itemView.findViewById(R.id.complete);

            }
        }
    }



}
