package com.southman.southmanclient;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.HideReturnsTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jsibbold.zoomage.ZoomageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.southman.southmanclient.billPOJO.billBean;
import com.southman.southmanclient.currentPOJO.currentBean;
import com.southman.southmanclient.orderPOJO.Datum;
import com.southman.southmanclient.orderPOJO.orderBean;
import com.southman.southmanclient.vHistoryPOJO.vHistoryBean;

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
        tabs.addTab(tabs.newTab().setText("PAYMENT"));

        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());

        grid.setAdapter(adapter);

        tabs.setupWithViewPager(grid);

        tabs.getTabAt(0).setText("VOUCHER");
        tabs.getTabAt(1).setText("PAYMENT");

        grid.setOffscreenPageLimit(2);

    }

    class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            if (i == 0) {
                return new Bills();
            } else {
                return new scratch();
            }
        }

        @Override
        public int getCount() {
            return 2;
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

            Log.d("dddd", formattedDate);

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

                            Call<orderBean> call = cr.getOrders2(SharePreferenceUtils.getInstance().getString("id"), strDate);

                            call.enqueue(new Callback<orderBean>() {
                                @Override
                                public void onResponse(Call<orderBean> call, Response<orderBean> response1) {

                                    //Toast.makeText(History.this, String.valueOf(response1.body().getData().size()), Toast.LENGTH_SHORT).show();

                                    if (response1.body().getStatus().equals("1")) {
                                        linear.setVisibility(View.GONE);
                                    } else {
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

            Call<orderBean> call = cr.getOrders2(SharePreferenceUtils.getInstance().getString("id"), formattedDate);

            call.enqueue(new Callback<orderBean>() {
                @Override
                public void onResponse(Call<orderBean> call, Response<orderBean> response1) {

                    //Toast.makeText(History.this, String.valueOf(response1.body().getData().size()), Toast.LENGTH_SHORT).show();

                    if (response1.body().getStatus().equals("1")) {
                        linear.setVisibility(View.GONE);
                    } else {
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


        static class BillAdapter extends RecyclerView.Adapter<BillAdapter.ViewHolder>
        {
            Context context;
            List<Datum> list;

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
                        holder.price.setText("Price - Rs." + item.getPrice());

                        float pr1 = Float.parseFloat(item.getPrice());
                        float pa1 = Float.parseFloat(item.getCashValue());

                        holder.paid.setText("Collect from customer - Rs." + String.valueOf(pr1 - pa1));

                        holder.paid.setVisibility(View.VISIBLE);
                        holder.price.setVisibility(View.VISIBLE);

                        break;
                    case "scratch":
                        holder.type.setText("SCRATCH CARD - " + item.getId());

                        holder.price.setText("Discount - Rs." + item.getCashValue());

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

            Log.d("dddd", formattedDate);

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

                            Call<orderBean> call = cr.getOrders22(SharePreferenceUtils.getInstance().getString("id"), strDate);

                            call.enqueue(new Callback<orderBean>() {
                                @Override
                                public void onResponse(Call<orderBean> call, Response<orderBean> response1) {

                                    //Toast.makeText(History.this, String.valueOf(response1.body().getData().size()), Toast.LENGTH_SHORT).show();

                                    if (response1.body().getStatus().equals("1")) {
                                        linear.setVisibility(View.GONE);
                                    } else {
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

            Call<orderBean> call = cr.getOrders22(SharePreferenceUtils.getInstance().getString("id"), formattedDate);

            call.enqueue(new Callback<orderBean>() {
                @Override
                public void onResponse(Call<orderBean> call, Response<orderBean> response1) {

                    //Toast.makeText(History.this, String.valueOf(response1.body().getData().size()), Toast.LENGTH_SHORT).show();

                    if (response1.body().getStatus().equals("1")) {
                        linear.setVisibility(View.GONE);
                    } else {
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

        RecyclerView grid;
        GridLayoutManager manager;
        ProgressBar progress;
        List<com.southman.southmanclient.vHistoryPOJO.Datum> list;
        BillAdapter adapter;
        TextView date;
        LinearLayout linear;

        String dd;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.bill_layout, container, false);

            list = new ArrayList<>();

            linear = view.findViewById(R.id.linear);
            date = view.findViewById(R.id.date);
            grid = view.findViewById(R.id.grid);
            manager = new GridLayoutManager(getContext(), 1);
            progress = view.findViewById(R.id.progress);

            adapter = new BillAdapter(getActivity(), list);

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

                            dd = strDate;

                            progress.setVisibility(View.VISIBLE);

                            Bean b = (Bean) getActivity().getApplicationContext();

                            Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl(b.baseurl)
                                    .addConverterFactory(ScalarsConverterFactory.create())
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();

                            AllApiIneterface cr = retrofit.create(AllApiIneterface.class);


                            Call<vHistoryBean> call = cr.getOrders12(SharePreferenceUtils.getInstance().getString("id"), dd);

                            call.enqueue(new Callback<vHistoryBean>() {
                                @Override
                                public void onResponse(Call<vHistoryBean> call, Response<vHistoryBean> response) {

                                    if (response.body().getStatus().equals("1")) {
                                        adapter.setData(response.body().getData());
                                        linear.setVisibility(View.GONE);
                                    } else {
                                        adapter.setData(response.body().getData());
                                        linear.setVisibility(View.VISIBLE);
                                        //Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                    progress.setVisibility(View.GONE);

                                }

                                @Override
                                public void onFailure(Call<vHistoryBean> call, Throwable t) {
                                    progress.setVisibility(View.GONE);
                                }
                            });


                        }
                    });


                }
            });

            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = df.format(c);

            Log.d("dddd", formattedDate);

            date.setText("Date - " + formattedDate + " (click to change)");

            dd = formattedDate;

            singleReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    if (intent.getAction().equals("count")) {
                        onResume();
                    }

                }
            };

            LocalBroadcastManager.getInstance(getContext()).registerReceiver(singleReceiver,
                    new IntentFilter("count"));


            return view;
        }

        BroadcastReceiver singleReceiver;

        @Override
        public void onResume() {
            super.onResume();



            progress.setVisibility(View.VISIBLE);

            Bean b = (Bean) getActivity().getApplicationContext();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(b.baseurl)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            AllApiIneterface cr = retrofit.create(AllApiIneterface.class);


            Call<vHistoryBean> call = cr.getOrders12(SharePreferenceUtils.getInstance().getString("id"), dd);

            call.enqueue(new Callback<vHistoryBean>() {
                @Override
                public void onResponse(Call<vHistoryBean> call, Response<vHistoryBean> response) {

                    if (response.body().getStatus().equals("1")) {
                        adapter.setData(response.body().getData());
                        linear.setVisibility(View.GONE);
                    } else {
                        adapter.setData(response.body().getData());
                        linear.setVisibility(View.VISIBLE);
                        //Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    progress.setVisibility(View.GONE);

                }

                @Override
                public void onFailure(Call<vHistoryBean> call, Throwable t) {
                    progress.setVisibility(View.GONE);
                }
            });


        }

        class BillAdapter extends RecyclerView.Adapter<BillAdapter.ViewHolder> {
            Context context;
            List<com.southman.southmanclient.vHistoryPOJO.Datum> list = new ArrayList<>();

            public BillAdapter(Context context, List<com.southman.southmanclient.vHistoryPOJO.Datum> list) {
                this.context = context;
                this.list = list;
            }

            void setData(List<com.southman.southmanclient.vHistoryPOJO.Datum> list) {
                this.list = list;
                notifyDataSetChanged();
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.order_list_model1, viewGroup, false);
                return new ViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {

                holder.setIsRecyclable(false);

                final com.southman.southmanclient.vHistoryPOJO.Datum item = list.get(i);


                holder.status.setText(item.getCreated());


                try {
                    holder.price.setRating(Float.parseFloat(item.getRating()));
                }catch (Exception e)
                {
                    e.printStackTrace();
                }

                holder.type.setText("#" + item.getTxn());

                if (item.getStatus().equals("pending"))
                {
                    holder.paid.setTextColor(Color.BLUE);
                }
                else
                {
                    holder.paid.setTextColor(Color.parseColor("#E95959"));
                }

                if (item.getMode().equals("GPAY"))
                {
                    holder.code.setText(item.getClient() + " paid \u20B9 " + item.getAmount());
                }
                else
                {
                    if (item.getStatus().equals("pending"))
                    {
                        holder.code.setText(item.getClient() + " has requested to pay \u20B9 " + item.getAmount());
                    }
                    else
                    {
                        holder.code.setText(item.getClient() + " paid \u20B9 " + item.getAmount());
                    }

                }

                holder.type.setTextColor(Color.parseColor("#009688"));

                holder.date.setVisibility(View.GONE);
                //holder.price.setVisibility(View.VISIBLE);

                if (item.getMode().equals("GPAY"))
                {
                    holder.type.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_google_pay_mark_800_gray , 0 , 0 , 0);
                }
                else if (item.getMode().equals("CASH"))
                {
                    holder.type.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_money2 , 0 , 0 , 0);
                }
                else
                {
                    holder.type.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_free , 0 , 0 , 0);
                }


                holder.paid.setText(item.getStatus());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (item.getMode().equals("GPAY") || item.getMode().equals("FREE"))
                        {
                            Intent intent = new Intent(context , StatusActivity5.class);
                            intent.putExtra("id" , item.getId());
                            context.startActivity(intent);
                        }
                        else
                        {

                            if (item.getStatus().equals("pending"))
                            {

                                /*Intent intent = new Intent(context , CollectCash.class);
                                intent.putExtra("id" , item.getId());
                                intent.putExtra("cash" , item.getCash());
                                intent.putExtra("scratch" , item.getScratch());
                                intent.putExtra("pid" , item.getPid());
                                intent.putExtra("amount" , item.getAmount());
                                intent.putExtra("tid" , item.getTxn());
                                intent.putExtra("date" , item.getCreated());
                                intent.putExtra("user_id" , item.getUser_id());
                                context.startActivity(intent);*/

                            }
                            else
                            {
                                Intent intent = new Intent(context , StatusActivity5.class);
                                intent.putExtra("id" , item.getId());
                                context.startActivity(intent);
                            }

                        }



                    }
                });

            }

            @Override
            public int getItemCount() {
                return list.size();
            }

            class ViewHolder extends RecyclerView.ViewHolder {
                final TextView code;
                final TextView date;
                final TextView type;
                final TextView status;
                final RatingBar price;
                final TextView paid;

                ViewHolder(@NonNull View itemView) {
                    super(itemView);

                    code = itemView.findViewById(R.id.code);
                    date = itemView.findViewById(R.id.date);
                    type = itemView.findViewById(R.id.type);
                    status = itemView.findViewById(R.id.status);
                    price = itemView.findViewById(R.id.price);
                    paid = itemView.findViewById(R.id.paid);


                }
            }
        }


        @Override
        public void onDestroy() {
            super.onDestroy();

            LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(singleReceiver);

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

    static class BillAdapter extends RecyclerView.Adapter<BillAdapter.ViewHolder> {
        Context context;
        List<Datum> list = new ArrayList<>();

        public BillAdapter(Context context, List<Datum> list) {
            this.context = context;
            this.list = list;
        }

        void setData(List<Datum> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.redeem_list_mode, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int i) {

            final Datum item = list.get(i);

            holder.verify.setText(item.getStatus());

            holder.cancel.setVisibility(View.GONE);

            holder.generate.setVisibility(View.GONE);

            switch (item.getText()) {
                case "perks":
                    holder.order.setText("ORDER NO. - " + item.getId());
                    holder.order.setTextColor(Color.parseColor("#009688"));

                    //holder.price.setText("Benefits - " + item.getPrice() + " credits");

                    float pr = Float.parseFloat(item.getPrice());
                    float pa = Float.parseFloat(item.getCashValue());

                    //holder.paid.setText("Pending benefits - " + String.valueOf(pr - pa) + " credits");

                    break;
                case "cash":
                    if (item.getTableName().equals(""))
                    {
                        holder.order.setText("ORDER NO. - " + item.getId());
                    }
                    else
                    {
                        holder.order.setText("ORDER NO. - " + item.getId() + " (Table - " + item.getTableName() + ")");
                    }
                    holder.customer.setText(item.getUser());
                    holder.order.setTextColor(Color.parseColor("#689F38"));

                    holder.cash.setText(Html.fromHtml("Rs." + item.getCashRewards()));
                    holder.scratch.setText(Html.fromHtml("Rs." + item.getScratchAmount()));

//                    float pr1 = Float.parseFloat(item.getPrice());
                    //                  float pa1 = Float.parseFloat(item.getCashValue());

                    //                holder.paid.setText("Balance pay - Rs." + String.valueOf(pr1 - pa1));


                    if (item.getBillAmount().equals("")) {

                        holder.total.setText(Html.fromHtml("unverified"));
                        holder.collect.setText(Html.fromHtml("unverified"));

                    } else {

                        float c = Float.parseFloat(item.getCashRewards());
                        float s = Float.parseFloat(item.getScratchAmount());
                        float t = Float.parseFloat(item.getBillAmount());

                        holder.total.setText(Html.fromHtml("Rs." + item.getBillAmount()));
                        holder.collect.setText(Html.fromHtml("Rs." + String.valueOf(t - (c + s))));


                    }


                    break;
                case "scratch":
                    if (item.getTableName().equals(""))
                    {
                        holder.order.setText("ORDER NO. - " + item.getId());
                    }
                    else
                    {
                        holder.order.setText("ORDER NO. - " + item.getId() + " (Table - " + item.getTableName() + ")");
                    }
                    holder.customer.setText(item.getUser());
                    holder.order.setTextColor(Color.parseColor("#689F38"));

                    holder.cash.setText(Html.fromHtml("Rs." + item.getCashRewards()));
                    holder.scratch.setText(Html.fromHtml("Rs." + item.getScratchAmount()));

//                    float pr1 = Float.parseFloat(item.getPrice());
                    //                  float pa1 = Float.parseFloat(item.getCashValue());

                    //                holder.paid.setText("Balance pay - Rs." + String.valueOf(pr1 - pa1));

                    if (item.getBillAmount().equals("")) {

                        holder.total.setText(Html.fromHtml("unverified"));
                        holder.collect.setText(Html.fromHtml("unverified"));

                    } else {

                        float c = Float.parseFloat(item.getCashRewards());
                        float s = Float.parseFloat(item.getScratchAmount());
                        float t = Float.parseFloat(item.getBillAmount());

                        holder.total.setText(Html.fromHtml("Rs." + item.getBillAmount()));
                        holder.collect.setText(Html.fromHtml("Rs." + String.valueOf(t - (c + s))));


                    }
                    break;
            }


            final DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisk(true).cacheInMemory(true).resetViewBeforeLoading(false).build();


            final ImageLoader loader = ImageLoader.getInstance();

            loader.displayImage(item.getBill(), holder.bill, options);

            holder.bill.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(true);
                    dialog.setContentView(R.layout.zoom_dialog);
                    dialog.show();

                    ZoomageView zoom = dialog.findViewById(R.id.zoom);

                    loader.displayImage(item.getBill(), zoom, options);


                }
            });


        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView order, customer, cash, scratch, total, collect , generate;
            Button verify, cancel;
            ImageView bill;


            ViewHolder(@NonNull View itemView) {
                super(itemView);
                order = itemView.findViewById(R.id.order);
                customer = itemView.findViewById(R.id.user);
                cash = itemView.findViewById(R.id.cash);
                scratch = itemView.findViewById(R.id.scratch);
                total = itemView.findViewById(R.id.total);
                collect = itemView.findViewById(R.id.collect);
                verify = itemView.findViewById(R.id.verify);
                cancel = itemView.findViewById(R.id.cancel);
                bill = itemView.findViewById(R.id.bill);
                generate = itemView.findViewById(R.id.generate);
            }
        }
    }


}
