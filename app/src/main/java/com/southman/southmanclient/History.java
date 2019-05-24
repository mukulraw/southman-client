package com.southman.southmanclient;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.method.HideReturnsTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.southman.southmanclient.orderPOJO.Datum;
import com.southman.southmanclient.orderPOJO.orderBean;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class History extends AppCompatActivity {

    RecyclerView grid;
    GridLayoutManager manager;
    ProgressBar progress;
    List<Datum> list;
    BillAdapter adapter;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        list = new ArrayList<>();

        toolbar = findViewById(R.id.toolbar);
        grid = findViewById(R.id.grid);
        manager = new GridLayoutManager(History.this, 1);
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



        adapter = new BillAdapter(this , list);

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


    }

    class BillAdapter extends RecyclerView.Adapter<BillAdapter.ViewHolder>
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

                    holder.price.setText("Price - " + item.getPrice());

                    float pr = Float.parseFloat(item.getPrice());
                    float pa = Float.parseFloat(item.getCashValue());

                    holder.paid.setText("Collected - " + String.valueOf(pr - pa));

                    holder.paid.setVisibility(View.VISIBLE);
                    holder.price.setVisibility(View.VISIBLE);

                    break;
                case "cash":
                    holder.type.setText("REDEEM STORE - " + item.getId());

                    holder.price.setText("Price - " + item.getPrice());

                    float pr1 = Float.parseFloat(item.getPrice());
                    float pa1 = Float.parseFloat(item.getCashValue());

                    holder.paid.setText("Collected - " + String.valueOf(pr1 - pa1));

                    holder.paid.setVisibility(View.VISIBLE);
                    holder.price.setVisibility(View.VISIBLE);

                    break;
                case "scratch":
                    holder.type.setText("SCRATCH CARD - " + item.getId());
                    holder.paid.setVisibility(View.GONE);
                    holder.price.setVisibility(View.GONE);

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
