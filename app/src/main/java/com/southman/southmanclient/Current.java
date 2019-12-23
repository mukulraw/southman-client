package com.southman.southmanclient;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.southman.southmanclient.currentPOJO.Data;
import com.southman.southmanclient.currentPOJO.currentBean;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Current extends AppCompatActivity {

    String id, name , type;
    Toolbar toolbar;

    Button complete , gpaycomplete;

    TextView voucher, redeem, gpay, cash, fromsoouthman , tosouthman , history , refund , expired;
    ProgressBar progress;

    LinearLayout exp , cts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current2);

        name = getIntent().getStringExtra("name");
        id = getIntent().getStringExtra("id");
        type = getIntent().getStringExtra("type");

        toolbar = findViewById(R.id.toolbar);
        complete = findViewById(R.id.complete);
        voucher = findViewById(R.id.voucher);
        redeem = findViewById(R.id.redeem);
        gpay = findViewById(R.id.gpay);
        cash = findViewById(R.id.cash);
        history = findViewById(R.id.history);
        progress = findViewById(R.id.progress);
        fromsoouthman = findViewById(R.id.from_southman);
        tosouthman = findViewById(R.id.to_southman);
        refund = findViewById(R.id.refund);
        expired = findViewById(R.id.expired);
        exp = findViewById(R.id.exp);
        cts = findViewById(R.id.cts);
        gpaycomplete = findViewById(R.id.gpaycomplete);

        if (type.equals("client"))
        {
            complete.setVisibility(View.GONE);
            gpaycomplete.setVisibility(View.GONE);
            exp.setVisibility(View.GONE);
            cts.setVisibility(View.GONE);
        }
        else
        {
            complete.setVisibility(View.VISIBLE);
            gpaycomplete.setVisibility(View.VISIBLE);
            exp.setVisibility(View.VISIBLE);
            cts.setVisibility(View.VISIBLE);
        }

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationIcon(R.drawable.arrowleft);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        toolbar.setTitle(name);


        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Current.this , TransHistory.class);
                intent.putExtra("id" , id);
                startActivity(intent);

            }
        });

        gpaycomplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                final Dialog dialog = new Dialog(Current.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.comp_dialog);
                dialog.show();

                Button ookk = dialog.findViewById(R.id.button2);
                Button canc = dialog.findViewById(R.id.button4);

                canc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


                ookk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();

                        progress.setVisibility(View.VISIBLE);

                        Bean b = (Bean) getApplicationContext();

                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(b.baseurl)
                                .addConverterFactory(ScalarsConverterFactory.create())
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

                        Call<currentBean> call = cr.completeTrans2(id);

                        call.enqueue(new Callback<currentBean>() {
                            @Override
                            public void onResponse(Call<currentBean> call, Response<currentBean> response) {

                                Toast.makeText(Current.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                progress.setVisibility(View.GONE);

                                loadData();

                            }

                            @Override
                            public void onFailure(Call<currentBean> call, Throwable t) {

                            }
                        });

                    }
                });








            }
        });


        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                final Dialog dialog = new Dialog(Current.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.comp_dialog);
                dialog.show();

                Button ookk = dialog.findViewById(R.id.button2);
                Button canc = dialog.findViewById(R.id.button4);

                canc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


                ookk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();

                        progress.setVisibility(View.VISIBLE);

                        Bean b = (Bean) getApplicationContext();

                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(b.baseurl)
                                .addConverterFactory(ScalarsConverterFactory.create())
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

                        Call<currentBean> call = cr.completeTrans(id);

                        call.enqueue(new Callback<currentBean>() {
                            @Override
                            public void onResponse(Call<currentBean> call, Response<currentBean> response) {

                                Toast.makeText(Current.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                progress.setVisibility(View.GONE);

                                loadData();

                            }

                            @Override
                            public void onFailure(Call<currentBean> call, Throwable t) {

                            }
                        });

                    }
                });








            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        loadData();

    }

    void loadData()
    {
        progress.setVisibility(View.VISIBLE);

        Bean b = (Bean) getApplicationContext();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.baseurl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

        Call<currentBean> call = cr.getCurrent(id);

        call.enqueue(new Callback<currentBean>() {
            @Override
            public void onResponse(Call<currentBean> call, Response<currentBean> response) {

                if (response.body().getStatus().equals("1"))
                {

                    Data item = response.body().getData();


                    voucher.setText("\u20B9 " + item.getVoucher());
                    redeem.setText("\u20B9 " + item.getRedeem());
                    gpay.setText("\u20B9 " + item.getGpay());
                    cash.setText("\u20B9 " + item.getCash());
                    fromsoouthman.setText("\u20B9 " + item.getFromSouthman());
                    tosouthman.setText("\u20B9 " + item.getToSouthman());
                    refund.setText("\u20B9 " + item.getReserved());
                    expired.setText("\u20B9 " + item.getExpired());

                    complete.setText(item.getButtontext());

                }

                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<currentBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });

    }


}
