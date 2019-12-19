package com.southman.southmanclient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.southman.southmanclient.benefitsPOJO.Datum;
import com.southman.southmanclient.benefitsPOJO.benefitsBean;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class expired2 extends Fragment {


    RecyclerView grid;
    GridLayoutManager manager;
    ProgressBar progress;
    List<Datum> list;
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
            public void onClick(View view) {

                progress.setVisibility(View.VISIBLE);

                Bean b = (Bean) getActivity().getApplicationContext();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(b.baseurl)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                AllApiIneterface cr = retrofit.create(AllApiIneterface.class);


                Call<benefitsBean> call = cr.returnCDM();

                call.enqueue(new Callback<benefitsBean>() {
                    @Override
                    public void onResponse(Call<benefitsBean> call, Response<benefitsBean> response) {

                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        onResume();

                        progress.setVisibility(View.GONE);

                    }

                    @Override
                    public void onFailure(Call<benefitsBean> call, Throwable t) {
                        progress.setVisibility(View.GONE);
                    }
                });

            }
        });


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


        Call<benefitsBean> call = cr.getExpired2();

        call.enqueue(new Callback<benefitsBean>() {
            @Override
            public void onResponse(Call<benefitsBean> call, Response<benefitsBean> response) {

                if (response.body().getStatus().equals("1")) {
                    adapter.setData(response.body().getData());
                    linear.setVisibility(View.GONE);

                    float tot = 0;

                    for (int i = 0 ; i < response.body().getData().size() ; i++)
                    {
                        float am = Float.parseFloat(response.body().getData().get(i).getAmount());
                        tot = tot + am;
                    }

                    date.setText("Total refund - ₹ " + tot + " (Return to CDM)");

                } else {
                    adapter.setData(response.body().getData());
                    linear.setVisibility(View.VISIBLE);

                    date.setText("Total refund - ₹ 0" + " (Return to CDM)");
                    //Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }

                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<benefitsBean> call, Throwable t) {
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
            View view = inflater.inflate(R.layout.order_list_model3, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {
            holder.setIsRecyclable(false);
            final Datum item = list.get(i);


            holder.status.setText(item.getCreated());

            holder.status.setVisibility(View.GONE);

            holder.type.setTextColor(Color.parseColor("#009688"));

            holder.code.setText(item.getUser() + "'s Scratch Coupon worth \u20B9" + item.getAmount() + " got expired on " + item.getExpiry());

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView code;

            final TextView type;
            final TextView status;


            ViewHolder(@NonNull View itemView) {
                super(itemView);

                code = itemView.findViewById(R.id.code);

                type = itemView.findViewById(R.id.type);
                status = itemView.findViewById(R.id.status);


            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(singleReceiver);

    }


}
