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

import com.southman.southmanclient.orderPOJO.orderBean;
import com.southman.southmanclient.voucherPOJO.Datum;
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


                        Call<voucherBean> call = cr.getOrders1(SharePreferenceUtils.getInstance().getString("id"), strDate);

                        call.enqueue(new Callback<voucherBean>() {
                            @Override
                            public void onResponse(Call<voucherBean> call, Response<voucherBean> response) {

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
                            public void onFailure(Call<voucherBean> call, Throwable t) {
                                progress.setVisibility(View.GONE);
                            }
                        });


                    }
                });


            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c);

        Log.d("dddd", formattedDate);

        date.setText("Date - " + formattedDate + " (click to change)");

        dd = formattedDate;

        progress.setVisibility(View.VISIBLE);

        Bean b = (Bean) getActivity().getApplicationContext();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.baseurl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AllApiIneterface cr = retrofit.create(AllApiIneterface.class);


        Call<voucherBean> call = cr.getOrders1(SharePreferenceUtils.getInstance().getString("id"), formattedDate);

        call.enqueue(new Callback<voucherBean>() {
            @Override
            public void onResponse(Call<voucherBean> call, Response<voucherBean> response) {

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
            public void onFailure(Call<voucherBean> call, Throwable t) {
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

            final Datum item = list.get(i);

            holder.date.setText(item.getCreated());

            holder.user.setText(item.getUser() + " purchased Scratch Card worth Rs. " + item.getAmount());

            holder.type.setText("VOUCHER STORE");
            holder.type.setTextColor(Color.parseColor("#009688"));



        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView date, type , user;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                date = itemView.findViewById(R.id.date);
                type = itemView.findViewById(R.id.type);
                user = itemView.findViewById(R.id.user);
            }
        }
    }


}
