package com.southman.southmanclient;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.dewinjm.monthyearpicker.MonthYearPickerDialog;
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
    TextView profit;
    String dd;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profit_layout, container, false);


        linear = view.findViewById(R.id.linear);
        date = view.findViewById(R.id.date);
        progress = view.findViewById(R.id.progress);
        profit = view.findViewById(R.id.grid);


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
                                    profit.setText("Current Profit - \u20B9 " + response.body().getData());
                                    profit.setVisibility(View.VISIBLE);
                                } else {

                                    linear.setVisibility(View.VISIBLE);
                                    profit.setVisibility(View.GONE);
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
                    profit.setText("Current Profit - \u20B9 " + response.body().getData());
                } else {

                    linear.setVisibility(View.VISIBLE);
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


}
