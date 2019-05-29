package com.southman.southmanclient;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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

import com.southman.southmanclient.transPOJO.Datum;
import com.southman.southmanclient.transPOJO.transBean;

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

public class TransHistory extends AppCompatActivity {

    RecyclerView grid;
    GridLayoutManager manager;
    ProgressBar progress;
    List<Datum> list;
    BillAdapter adapter;
    Toolbar toolbar;
    String id;
    LinearLayout linear;
    TextView date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans_history);

        id = getIntent().getStringExtra("id");

        list = new ArrayList<>();

        linear = findViewById(R.id.linear);
        date = findViewById(R.id.date);
        toolbar = findViewById(R.id.toolbar);
        grid = findViewById(R.id.grid);
        manager = new GridLayoutManager(TransHistory.this, 1);
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

        toolbar.setTitle("Transaction history");


        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Dialog dialog = new Dialog(TransHistory.this);
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

                        Bean b = (Bean) getApplicationContext();

                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(b.baseurl)
                                .addConverterFactory(ScalarsConverterFactory.create())
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        AllApiIneterface cr = retrofit.create(AllApiIneterface.class);


                        Call<transBean> call = cr.getTrans(id , strDate);

                        call.enqueue(new Callback<transBean>() {
                            @Override
                            public void onResponse(Call<transBean> call, Response<transBean> response) {

                                if (response.body().getStatus().equals("1")) {
                                    adapter.setData(response.body().getData());
                                    linear.setVisibility(View.GONE);
                                } else {
                                    linear.setVisibility(View.VISIBLE);
                                    //Toast.makeText(TransHistory.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                }

                                progress.setVisibility(View.GONE);

                            }

                            @Override
                            public void onFailure(Call<transBean> call, Throwable t) {
                                progress.setVisibility(View.GONE);
                            }
                        });



                    }
                });



            }
        });


        adapter = new BillAdapter(this, list);

        grid.setAdapter(adapter);
        grid.setLayoutManager(manager);
    }

    @Override
    public void onResume() {
        super.onResume();

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c);

        Log.d("dddd" , formattedDate);

        date.setText("Date - " + formattedDate + " (click to change)");


        progress.setVisibility(View.VISIBLE);

        Bean b = (Bean) getApplicationContext();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.baseurl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AllApiIneterface cr = retrofit.create(AllApiIneterface.class);


        Call<transBean> call = cr.getTrans(id , formattedDate);

        call.enqueue(new Callback<transBean>() {
            @Override
            public void onResponse(Call<transBean> call, Response<transBean> response) {

                if (response.body().getStatus().equals("1")) {
                    adapter.setData(response.body().getData());
                    linear.setVisibility(View.GONE);
                } else {
                    linear.setVisibility(View.VISIBLE);
                    //Toast.makeText(TransHistory.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }

                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<transBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });


    }

    class BillAdapter extends RecyclerView.Adapter<BillAdapter.ViewHolder> {
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
            View view = inflater.inflate(R.layout.trans_list_model, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int i) {

            final Datum item = list.get(i);

            holder.purchase.setText(item.getItemsPurchased() + " Rs.");
            holder.scratch.setText(item.getScratchCardsRedeemed() + " Rs.");
            holder.perks.setText(item.getPerksRedeemed() + " credits");
            holder.cash.setText(item.getCashRewardsRedeemed() + " Rs.");
            holder.bills.setText(item.getBillsUploaded());
            holder.amount.setText(item.getVerifiedBillsAmount() + " Rs.");
            holder.date.setText(item.getCreated());

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView purchase, scratch, perks, cash, bills, amount, date;

            ViewHolder(@NonNull View itemView) {
                super(itemView);

                date = itemView.findViewById(R.id.date);
                purchase = itemView.findViewById(R.id.purchase);
                scratch = itemView.findViewById(R.id.scratch);
                perks = itemView.findViewById(R.id.perks);
                cash = itemView.findViewById(R.id.cash);
                bills = itemView.findViewById(R.id.bills);
                amount = itemView.findViewById(R.id.amount);

            }
        }
    }

}
