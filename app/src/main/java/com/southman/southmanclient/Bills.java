package com.southman.southmanclient;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.Rating;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.southman.southmanclient.orderPOJO.orderBean;
import com.southman.southmanclient.vHistoryPOJO.Datum;
import com.southman.southmanclient.vHistoryPOJO.vHistoryBean;
import com.southman.southmanclient.voucherPOJO.voucherBean;

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

public class Bills extends Fragment {


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


                        Call<vHistoryBean> call = cr.getOrders1(SharePreferenceUtils.getInstance().getString("id"), dd);

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


        Call<vHistoryBean> call = cr.getOrders1(SharePreferenceUtils.getInstance().getString("id"), dd);

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
            View view = inflater.inflate(R.layout.order_list_model1, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int i) {

            holder.setIsRecyclable(false);

            final Datum item = list.get(i);

            holder.status.setText(item.getCreated());


            holder.type.setText("#" + item.getTxn());
            holder.code.setText(item.getClient() + " paid \u20B9 " + item.getAmount());
            holder.type.setTextColor(Color.parseColor("#009688"));

            holder.date.setVisibility(View.GONE);
            //holder.price.setVisibility(View.VISIBLE);
            try {
                holder.price.setRating(Float.parseFloat(item.getRating()));
            }catch (Exception e)
            {
                e.printStackTrace();
            }


            holder.paid.setText("completed");

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context , StatusActivity.class);
                    intent.putExtra("id" , item.getId());
                    context.startActivity(intent);

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
