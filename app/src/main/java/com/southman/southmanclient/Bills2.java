package com.southman.southmanclient;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class Bills2 extends Fragment {

    RecyclerView grid;
    GridLayoutManager manager;
    ProgressBar progress;
    List<Datum> list;
    BillAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bill_layout , container , false);

        list = new ArrayList<>();

        grid = view.findViewById(R.id.grid);
        manager = new GridLayoutManager(getContext() , 1);
        progress = view.findViewById(R.id.progress);

        adapter = new BillAdapter(getActivity() , list);

        grid.setAdapter(adapter);
        grid.setLayoutManager(manager);


        return view;
    }

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


        Call<orderBean> call = cr.getOrders3(SharePreferenceUtils.getInstance().getString("id"));

        call.enqueue(new Callback<orderBean>() {
            @Override
            public void onResponse(Call<orderBean> call, Response<orderBean> response) {

                if (response.body().getStatus().equals("1"))
                {
                    adapter.setData(response.body().getData());
                }
                else
                {
                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
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
                    holder.type.setTextColor(Color.parseColor("#009688"));
                    holder.price.setText("Benefits - " + item.getPrice() + " credits");

                    try {

                        float pr = Float.parseFloat(item.getPrice());
                        float pa = Float.parseFloat(item.getCashValue());

                        holder.paid.setText("Pending benefits - " + String.valueOf(pr - pa) + " credits");

                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                    holder.paid.setVisibility(View.VISIBLE);
                    holder.price.setVisibility(View.VISIBLE);

                    break;
                case "cash":
                    holder.type.setText("REDEEM STORE - " + item.getId());
                    holder.type.setTextColor(Color.parseColor("#689F38"));
                    holder.price.setText("Benefits - " + item.getPrice() + " credits");

                    try {

                        float pr1 = Float.parseFloat(item.getPrice());
                        float pa1 = Float.parseFloat(item.getCashValue());

                        holder.paid.setText("Pending benefits - " + String.valueOf(pr1 - pa1) + " credits");

                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                    holder.paid.setVisibility(View.VISIBLE);
                    holder.price.setVisibility(View.VISIBLE);

                    break;
                case "scratch":
                    holder.type.setText("SCRATCH CARD - " + item.getId());
                    holder.type.setTextColor(Color.parseColor("#F9A825"));
                    holder.paid.setVisibility(View.GONE);
                    //holder.price.setVisibility(View.GONE);

                    holder.price.setText("Benefits - " + item.getCashValue() + " credits");
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
