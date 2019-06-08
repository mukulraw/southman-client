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
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jsibbold.zoomage.ZoomageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
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

public class Bills22 extends Fragment {

    RecyclerView grid;
    GridLayoutManager manager;
    ProgressBar progress;
    List<Datum> list;
    BillAdapter adapter;
    TextView date;
    LinearLayout linear;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bill_layout , container , false);

        list = new ArrayList<>();
        date = view.findViewById(R.id.date);
        linear = view.findViewById(R.id.linear);
        grid = view.findViewById(R.id.grid);
        manager = new GridLayoutManager(getContext() , 1);
        progress = view.findViewById(R.id.progress);

        adapter = new BillAdapter(getActivity() , list);

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


                        Call<orderBean> call = cr.getOrders32(SharePreferenceUtils.getInstance().getString("id") , strDate);

                        call.enqueue(new Callback<orderBean>() {
                            @Override
                            public void onResponse(Call<orderBean> call, Response<orderBean> response) {

                                if (response.body().getStatus().equals("1"))
                                {
                                    adapter.setData(response.body().getData());
                                    linear.setVisibility(View.GONE);
                                }
                                else
                                {
                                    adapter.setData(response.body().getData());
                                    linear.setVisibility(View.VISIBLE);
                                 //   Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                }

                                progress.setVisibility(View.GONE);

                            }

                            @Override
                            public void onFailure(Call<orderBean> call, Throwable t) {
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

        Log.d("dddd" , formattedDate);

        date.setText("Date - " + formattedDate + " (click to change)");


        progress.setVisibility(View.VISIBLE);

        Bean b = (Bean) getActivity().getApplicationContext();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.baseurl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AllApiIneterface cr = retrofit.create(AllApiIneterface.class);


        Call<orderBean> call = cr.getOrders32(SharePreferenceUtils.getInstance().getString("id") , formattedDate);

        call.enqueue(new Callback<orderBean>() {
            @Override
            public void onResponse(Call<orderBean> call, Response<orderBean> response) {

                if (response.body().getStatus().equals("1"))
                {
                    adapter.setData(response.body().getData());
                    linear.setVisibility(View.GONE);
                }
                else
                {
                    adapter.setData(response.body().getData());
                    linear.setVisibility(View.VISIBLE);
                    //   Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
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
            View view = inflater.inflate(R.layout.redeem_list_mode2 , viewGroup , false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int i) {

            final Datum item = list.get(i);


            holder.verify.setText(item.getStatus());

            holder.cancel.setVisibility(View.GONE);

            holder.client.setText(item.getClient());

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
                    holder.order.setText("ORDER NO. - " + item.getId() + " (Table - " + item.getTableName() + ")");
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
                    holder.order.setText("ORDER NO. - " + item.getId() + " (Table - " + item.getTableName() + ")");
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

            loader.displayImage(item.getBill() , holder.bill , options);

            holder.bill.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(true);
                    dialog.setContentView(R.layout.zoom_dialog);
                    dialog.show();

                    ZoomageView zoom = dialog.findViewById(R.id.zoom);

                    loader.displayImage(item.getBill() , zoom , options);


                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder
        {
            TextView order, customer, cash, scratch, total, collect , client;
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
                client = itemView.findViewById(R.id.client);
            }
        }
    }



}
