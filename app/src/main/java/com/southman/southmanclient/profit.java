package com.southman.southmanclient;

import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.dewinjm.monthyearpicker.MonthYearPickerDialog;
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment;
import com.southman.southmanclient.profitPOJO.Datum;
import com.southman.southmanclient.profitPOJO.profitBean;

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

public class profit extends Fragment {

    ProgressBar progress;
    TextView date;
    LinearLayout linear;
    List<Datum> list;
    BillAdapter adapter;
    String dd;
    RecyclerView grid;
    GridLayoutManager manager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bill_layout, container, false);

        list = new ArrayList<>();

        linear = view.findViewById(R.id.linear);
        date = view.findViewById(R.id.date);
        progress = view.findViewById(R.id.progress);
        grid = view.findViewById(R.id.grid);
        manager = new GridLayoutManager(getContext(), 1);

        adapter = new BillAdapter(getActivity(), list);

        grid.setAdapter(adapter);
        grid.setLayoutManager(manager);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                int yearSelected;
                int monthSelected;

//Set default values
                Calendar calendar = Calendar.getInstance();
                yearSelected = calendar.get(Calendar.YEAR);
                monthSelected = calendar.get(Calendar.MONTH);

                MonthYearPickerDialogFragment dialogFragment = MonthYearPickerDialogFragment
                        .getInstance(monthSelected, yearSelected);

                dialogFragment.setOnDateSetListener(new MonthYearPickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(int year, int monthOfYear) {
                        // do something

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");


                        String strDate = dateFormat.format(calendar.getTime());

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


                        Call<profitBean> call = cr.getProfit(dd);

                        call.enqueue(new Callback<profitBean>() {
                            @Override
                            public void onResponse(Call<profitBean> call, Response<profitBean> response) {

                                if (response.body().getStatus().equals("1")) {

                                    linear.setVisibility(View.GONE);

                                    adapter.setData(response.body().getData());
                                } else {

                                    linear.setVisibility(View.VISIBLE);

                                    adapter.setData(response.body().getData());
                                    //Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                }

                                progress.setVisibility(View.GONE);

                            }

                            @Override
                            public void onFailure(Call<profitBean> call, Throwable t) {
                                progress.setVisibility(View.GONE);
                            }
                        });

                    }
                });


                dialogFragment.show(getChildFragmentManager(), null);



            }
        });

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
        String formattedDate = df.format(c);

        Log.d("dddd", formattedDate);

        date.setText("Date - " + formattedDate + " (click to change)");

        dd = formattedDate;


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


        Call<profitBean> call = cr.getProfit(dd);

        call.enqueue(new Callback<profitBean>() {
            @Override
            public void onResponse(Call<profitBean> call, Response<profitBean> response) {

                if (response.body().getStatus().equals("1")) {

                    linear.setVisibility(View.GONE);
                    adapter.setData(response.body().getData());
                } else {

                    linear.setVisibility(View.VISIBLE);
                    adapter.setData(response.body().getData());
                    //Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }

                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<profitBean> call, Throwable t) {
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
            View view = inflater.inflate(R.layout.profit_list_model, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {
            holder.setIsRecyclable(false);
            final Datum item = list.get(i);


            holder.name.setText(item.getFullName());


            holder.month.setText("\u20B9 " + item.getMonthProfit());
            holder.cdm.setText("\u20B9 " + item.getCdm());
            holder.current.setText("TOTAL PROFIT TILL TODAY - \u20B9 " + item.getTotalProfit());


        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView name;

            final TextView month;
            final TextView cdm , current;


            ViewHolder(@NonNull View itemView) {
                super(itemView);

                name = itemView.findViewById(R.id.textView6);
                month = itemView.findViewById(R.id.textView10);
                cdm = itemView.findViewById(R.id.textView9);
                current = itemView.findViewById(R.id.textView11);


            }
        }
    }


}
