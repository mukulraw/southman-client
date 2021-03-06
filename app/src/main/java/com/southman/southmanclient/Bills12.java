package com.southman.southmanclient;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.model.FileLoader;
import com.jsibbold.zoomage.ZoomageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.southman.southmanclient.billPOJO.billBean;
import com.southman.southmanclient.createOrderPOJO.createOrderBean;
import com.southman.southmanclient.currentPOJO.currentBean;

import com.southman.southmanclient.onlinePayPOJO.onlinePayBean;
import com.southman.southmanclient.orderPOJO.orderBean;
import com.southman.southmanclient.vHistoryPOJO.Datum;
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

public class Bills12 extends Fragment {


    RecyclerView grid;
    GridLayoutManager manager;
    ProgressBar progress;
    List<Datum> list;
    BillAdapter adapter;
    TextView date;
    LinearLayout linear;

    String dd;

    float min = 0;

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
        public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {
            holder.setIsRecyclable(false);
            final Datum item = list.get(i);


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
                    if (item.getAmount().length() > 0)
                    {
                        holder.code.setText(item.getClient() + " has requested to pay \u20B9 " + item.getAmount());
                    }
                    else
                    {
                        holder.code.setText(item.getClient() + " has requested for final bill amount");
                    }

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

                            if (item.getAmount().length() > 0)
                            {
                                Intent intent = new Intent(context , CollectCash.class);
                                intent.putExtra("id" , item.getId());
                                intent.putExtra("cash" , item.getCash());
                                intent.putExtra("scratch" , item.getScratch());
                                intent.putExtra("pid" , item.getPid());
                                intent.putExtra("amount" , item.getAmount());
                                intent.putExtra("tid" , item.getTxn());
                                intent.putExtra("date" , item.getCreated());
                                intent.putExtra("user_id" , item.getUser_id());
                                intent.putExtra("user" , item.getClient());
                                context.startActivity(intent);
                            }
                            else
                            {
                                final Dialog dialog3 = new Dialog(getActivity());
                                dialog3.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog3.setCancelable(false);
                                dialog3.setContentView(R.layout.enter_bill_amount_dialog);
                                dialog3.show();

                                final float min = Float.parseFloat(SharePreferenceUtils.getInstance().getString("min"));

                                final EditText amount = dialog3.findViewById(R.id.editText);
                                Button confirm = dialog3.findViewById(R.id.button10);
                                TextView mbill = dialog3.findViewById(R.id.textView5);
                                Button cancel = dialog3.findViewById(R.id.button11);
                                final ProgressBar pbar = dialog3.findViewById(R.id.progressBar6);

                                if (min > 0)
                                {
                                    mbill.setText("Minimum bill amount is \u20B9 " + (min + 1));
                                    mbill.setVisibility(View.VISIBLE);
                                }
                                else
                                {
                                    mbill.setVisibility(View.GONE);
                                }

                                cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        dialog3.dismiss();

                                        progress.setVisibility(View.VISIBLE);

                                        Bean b = (Bean) getActivity().getApplicationContext();


                                        Retrofit retrofit = new Retrofit.Builder()
                                                .baseUrl(b.baseurl)
                                                .addConverterFactory(ScalarsConverterFactory.create())
                                                .addConverterFactory(GsonConverterFactory.create())
                                                .build();

                                        AllApiIneterface cr = retrofit.create(AllApiIneterface.class);


                                        Call<onlinePayBean> call = cr.cancelOrder(item.getId());

                                        call.enqueue(new Callback<onlinePayBean>() {
                                            @Override
                                            public void onResponse(Call<onlinePayBean> call, Response<onlinePayBean> response) {

                                                Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                                progress.setVisibility(View.GONE);

                                                onResume();

                                            }

                                            @Override
                                            public void onFailure(Call<onlinePayBean> call, Throwable t) {
                                                progress.setVisibility(View.GONE);
                                            }
                                        });

                                    }
                                });


                                confirm.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {



                                        String aa = amount.getText().toString();

                                        if (aa.length() > 0) {

                                            float aaa = Float.parseFloat(aa);

                                            if (aaa > min)
                                            {
                                                progress.setVisibility(View.VISIBLE);

                                                Bean b = (Bean) getActivity().getApplicationContext();


                                                Retrofit retrofit = new Retrofit.Builder()
                                                        .baseUrl(b.baseurl)
                                                        .addConverterFactory(ScalarsConverterFactory.create())
                                                        .addConverterFactory(GsonConverterFactory.create())
                                                        .build();

                                                AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

                                                Call<createOrderBean> call = cr.createOrder(item.getId() , String.valueOf(aaa));
                                                call.enqueue(new Callback<createOrderBean>() {
                                                    @Override
                                                    public void onResponse(Call<createOrderBean> call, Response<createOrderBean> response) {

                                                        if (response.body().getStatus().equals("1")) {

                                                            dialog3.dismiss();
                                                            onResume();
                                                        }

                                                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                                        progress.setVisibility(View.GONE);

                                                    }

                                                    @Override
                                                    public void onFailure(Call<createOrderBean> call, Throwable t) {
                                                        progress.setVisibility(View.GONE);
                                                    }
                                                });

                                            }
                                            else
                                            {
                                                Toast.makeText(getContext(), "Please enter a valid amount", Toast.LENGTH_SHORT).show();
                                            }



                                        } else {
                                            Toast.makeText(getContext(), "Please enter a valid amount", Toast.LENGTH_SHORT).show();
                                        }


                                    }
                                });
                            }



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
