package com.southman.southmanclient;

import android.content.Context;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.southman.southmanclient.voucherHistoryPOJO.Datum;
import com.southman.southmanclient.voucherHistoryPOJO.voucherHistoryBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class OrderDetails extends AppCompatActivity {

    private Toolbar toolbar;
    ProgressBar bar;
    String base;


    CartAdapter adapter;

    GridLayoutManager manager;

    RecyclerView grid;

    List<Datum> list;

    String order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        order = getIntent().getStringExtra("order");

        list = new ArrayList<>();

        toolbar = findViewById(R.id.toolbar3);
        bar = findViewById(R.id.progressBar3);
        grid = findViewById(R.id.grid);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        toolbar.setNavigationIcon(R.drawable.arrowleft);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderDetails.this.finish();
            }
        });

        toolbar.setTitle("Order Details");

        adapter = new CartAdapter(list , this);

        manager = new GridLayoutManager(this , 1);

        grid.setAdapter(adapter);
        grid.setLayoutManager(manager);

    }

    class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder>
    {

        List<Datum> list = new ArrayList<>();
        Context context;
        LayoutInflater inflater;

        CartAdapter(List<Datum> list, Context context)
        {
            this.context = context;
            this.list = list;
        }

        void setgrid(List<Datum> list) {

            this.list = list;
            notifyDataSetChanged();

        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            inflater = (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);

            View view = inflater.inflate(R.layout.prod_list_model5, viewGroup, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i1) {

            Datum item = list.get(i1);

            //viewHolder.setIsRecyclable(false);




            viewHolder.quantity.setText("Quantity - " + item.getQuantity());




            //viewHolder.buy.setQuantity(Integer.parseInt(item.getQuantity()));







            Glide.with(context).load(base + "southman/admin2/upload/products3/" + item.getProduct_image()).into(viewHolder.imageView);

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder
        {
            ImageView imageView;

            LinearLayout benefits;

            Button viewBenefits;



            TextView quantity;

            //QuantityView buy;
            // TextView name;

            ViewHolder(@NonNull View itemView) {
                super(itemView);

                imageView = itemView.findViewById(R.id.image);
                //buy = itemView.findViewById(R.id.play);
                benefits = itemView.findViewById(R.id.benefits);
                viewBenefits = itemView.findViewById(R.id.view);


                quantity = itemView.findViewById(R.id.play);

                //buy.setSideTapEnabled(true);

                //name = itemView.findViewById(R.id.name);


            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadCart();

    }

    void loadCart()
    {
        bar.setVisibility(View.VISIBLE);

        Bean b = (Bean) getApplicationContext();

        base = b.baseurl;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.baseurl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

        Call<voucherHistoryBean> call = cr.getOrderHistory(order);
        call.enqueue(new Callback<voucherHistoryBean>() {
            @Override
            public void onResponse(Call<voucherHistoryBean> call, Response<voucherHistoryBean> response) {

                if (response.body().getData().size() > 0)
                {


                    adapter.setgrid(response.body().getData());


                }
                else
                {
                    adapter.setgrid(response.body().getData());
                }

                bar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<voucherHistoryBean> call, Throwable t) {
                bar.setVisibility(View.GONE);
            }
        });

    }

}
