package com.southman.southmanclient;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.southman.southmanclient.transPOJO.Datum;
import com.southman.southmanclient.transPOJO.transBean;

import java.util.ArrayList;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans_history);

        id = getIntent().getStringExtra("id");

        list = new ArrayList<>();

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


        adapter = new BillAdapter(this, list);

        grid.setAdapter(adapter);
        grid.setLayoutManager(manager);
    }

    @Override
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


        Call<transBean> call = cr.getTrans(id);

        call.enqueue(new Callback<transBean>() {
            @Override
            public void onResponse(Call<transBean> call, Response<transBean> response) {

                if (response.body().getStatus().equals("1")) {
                    adapter.setData(response.body().getData());
                } else {
                    Toast.makeText(TransHistory.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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
