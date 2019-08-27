package com.southman.southmanclient;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.southman.southmanclient.onlinePayPOJO.onlinePayBean;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class CollectCash extends AppCompatActivity {

    TextView status, amount, client_name, date, paid, tid;

    ImageButton back;
    ImageView gpay;



    ProgressBar progress;

    String id;

    TextView tid1, status1, cashdiscount, scratchcard, bill, balance;

    String oid , cash , scratch , pid , amm , txn , dd , uid , user;

    Button approve , reject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_cash);

        id = getIntent().getStringExtra("id");
        cash = getIntent().getStringExtra("cash");
        scratch = getIntent().getStringExtra("scratch");
        pid = getIntent().getStringExtra("pid");
        amm = getIntent().getStringExtra("amount");
        txn = getIntent().getStringExtra("tid");
        dd = getIntent().getStringExtra("date");
        uid = getIntent().getStringExtra("user_id");
        user = getIntent().getStringExtra("user");

        status = findViewById(R.id.textView14);
        amount = findViewById(R.id.textView16);
        client_name = findViewById(R.id.textView29);
        date = findViewById(R.id.textView30);
        approve = findViewById(R.id.button);
        reject = findViewById(R.id.button5);

        back = findViewById(R.id.imageButton);
        paid = findViewById(R.id.textView17);
        progress = findViewById(R.id.progressBar5);
        tid = findViewById(R.id.textView34);
        gpay = findViewById(R.id.textView35);


        tid.setText("TID - " + txn);
        date.setText(dd);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        final float ca = Float.parseFloat(cash);
        float sc = Float.parseFloat(scratch);
        float tb = Float.parseFloat(amm);

        float nb = tb - (ca + sc);

        client_name.setText("Please collect \u20B9 " + nb + " from " + user);

        amount.setText(Html.fromHtml("\u20B9 " + String.valueOf(nb) + " <strike>\u20B9 " + amm + "</strike>"));


        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final Dialog dialog = new Dialog(CollectCash.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.approve_dialog);
                dialog.show();

                Button ok = dialog.findViewById(R.id.button2);
                Button cancel = dialog.findViewById(R.id.button4);

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();

                        progress.setVisibility(View.VISIBLE);

                        Bean b = (Bean) getApplicationContext();


                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(b.baseurl)
                                .addConverterFactory(ScalarsConverterFactory.create())
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

                        Log.d("iiidd" , id);
                        Log.d("pid" , pid);
                        Log.d("uid" , uid);

                        Call<onlinePayBean> call = cr.onlinePay(id , pid , uid);

                        call.enqueue(new Callback<onlinePayBean>() {
                            @Override
                            public void onResponse(Call<onlinePayBean> call, Response<onlinePayBean> response) {



                                progress.setVisibility(View.GONE);

                                Intent intent = new Intent(CollectCash.this , StatusActivity5.class);
                                intent.putExtra("id" , id);
                                startActivity(intent);
                                finish();

                            }

                            @Override
                            public void onFailure(Call<onlinePayBean> call, Throwable t) {
                                progress.setVisibility(View.GONE);
                            }
                        });

                    }
                });

            }
        });

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final Dialog dialog = new Dialog(CollectCash.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.delete_dialog);
                dialog.show();


                Button ok = dialog.findViewById(R.id.button2);
                Button cancel = dialog.findViewById(R.id.button4);


                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialog.dismiss();

                    }
                });

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialog.dismiss();

                        progress.setVisibility(View.VISIBLE);

                        Bean b = (Bean) getApplicationContext();


                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(b.baseurl)
                                .addConverterFactory(ScalarsConverterFactory.create())
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        AllApiIneterface cr = retrofit.create(AllApiIneterface.class);


                        Call<onlinePayBean> call = cr.cancelOrder(id);

                        call.enqueue(new Callback<onlinePayBean>() {
                            @Override
                            public void onResponse(Call<onlinePayBean> call, Response<onlinePayBean> response) {

                                Toast.makeText(CollectCash.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                //dialog.dismiss();

                                progress.setVisibility(View.GONE);

                                finish();


                            }

                            @Override
                            public void onFailure(Call<onlinePayBean> call, Throwable t) {
                                progress.setVisibility(View.GONE);
                            }
                        });

                    }
                });




            }
        });


    }
}
