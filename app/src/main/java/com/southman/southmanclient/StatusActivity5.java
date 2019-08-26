package com.southman.southmanclient;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.southman.southmanclient.onlinePayPOJO.Data;
import com.southman.southmanclient.onlinePayPOJO.onlinePayBean;
import com.tarek360.instacapture.Instacapture;
import com.tarek360.instacapture.listener.SimpleScreenCapturingListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class StatusActivity5 extends AppCompatActivity {

    TextView status, amount, client_name, date, tag, paid, tid;
    Button share;
    ImageButton back;
    ImageView gpay;

    CardView order;

    ProgressBar progress;

    String id;

    TextView tid1, status1, cashdiscount, scratchcard, bill, balance;

    String oid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status5);

        id = getIntent().getStringExtra("id");

        status = findViewById(R.id.textView14);
        amount = findViewById(R.id.textView16);
        client_name = findViewById(R.id.textView29);
        date = findViewById(R.id.textView30);
        order = findViewById(R.id.textView33);
        share = findViewById(R.id.button3);
        tag = findViewById(R.id.textView32);
        back = findViewById(R.id.imageButton);
        paid = findViewById(R.id.textView17);
        progress = findViewById(R.id.progressBar5);
        tid = findViewById(R.id.textView34);
        gpay = findViewById(R.id.textView35);


        tid1 = findViewById(R.id.tid);
        status1 = findViewById(R.id.status);
        cashdiscount = findViewById(R.id.cash_discount);
        scratchcard = findViewById(R.id.scratch_card);
        bill = findViewById(R.id.bill);
        balance = findViewById(R.id.balance);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Instacapture.INSTANCE.capture(StatusActivity5.this, new SimpleScreenCapturingListener() {
                    @Override
                    public void onCaptureComplete(Bitmap bitmap) {
                        //Your code here..

                        String bitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, tid.getText().toString(), null);
                        Uri bitmapUri = Uri.parse(bitmapPath);

                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("image/png");
                        intent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
                        startActivity(Intent.createChooser(intent, "Share"));


                    }
                });

            }
        });


        /*order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(StatusActivity5.this , OrderDetails2.class);
                intent.putExtra("order" , oid);
                intent.putExtra("tid" , tid1.getText().toString());
                intent.putExtra("status" , status1.getText().toString());
                intent.putExtra("cash" , cashdiscount.getText().toString());
                intent.putExtra("scratch" , scratchcard.getText().toString());
                intent.putExtra("bill" , bill.getText().toString());
                intent.putExtra("balance" , balance.getText().toString());
                startActivity(intent);

            }
        });*/

        progress.setVisibility(View.VISIBLE);

        Bean b = (Bean) getApplicationContext();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.baseurl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AllApiIneterface cr = retrofit.create(AllApiIneterface.class);
        Call<onlinePayBean> call = cr.getSingleOrder2(id);

        call.enqueue(new Callback<onlinePayBean>() {
            @Override
            public void onResponse(Call<onlinePayBean> call, Response<onlinePayBean> response) {

                Data item = response.body().getData();

                //if (item.getStatus().equals("completed"))
                //{


                float ca = Float.parseFloat(item.getCash());
                float sc = Float.parseFloat(item.getScratch());
                float tb = Float.parseFloat(item.getAmount());

                float nb = tb - (ca + sc);

                if (item.getMode().equals("CASH")) {
                    gpay.setImageDrawable(getResources().getDrawable(R.drawable.ic_money2));
                } else {
                    gpay.setImageDrawable(getResources().getDrawable(R.drawable.ic_google_pay_mark_800_gray));
                }

                amount.setText(Html.fromHtml("\u20B9 " + String.valueOf(nb) + " <strike>\u20B9 " + item.getAmount() + "</strike>"));


                if (item.getStatus().equals("completed"))
                {
                    status.setText("Payment Successful");
                    order.setVisibility(View.VISIBLE);
                    //tag.setVisibility(View.VISIBLE);
                    amount.setCompoundDrawablesWithIntrinsicBounds(null , getResources().getDrawable(R.drawable.ic_checked) , null , null);
                }
                else if (item.getStatus().equals("rejected"))
                {
                    status.setText("Order Rejected");
                    order.setVisibility(View.GONE);
                    tag.setVisibility(View.GONE);
                    amount.setCompoundDrawablesWithIntrinsicBounds(null , getResources().getDrawable(R.drawable.ic_cancel) , null , null);
                }
                else
                {
                    status.setText("Order is Pending");
                    order.setVisibility(View.GONE);
                    tag.setVisibility(View.GONE);
                    amount.setCompoundDrawablesWithIntrinsicBounds(null , null , null , null);
                }


                client_name.setText(response.body().getData().getClient());
                client_name.setVisibility(View.VISIBLE);
                date.setText(response.body().getData().getCreated());
                back.setVisibility(View.VISIBLE);
                paid.setVisibility(View.VISIBLE);
                status.setVisibility(View.VISIBLE);
                amount.setVisibility(View.VISIBLE);


                date.setVisibility(View.VISIBLE);
                tid.setText("TXN ID - " + response.body().getData().getTxn());
                tid.setVisibility(View.VISIBLE);
                gpay.setVisibility(View.VISIBLE);


                tid1.setText("TXN ID - " + item.getTxn());
                status1.setText(item.getStatus());
                cashdiscount.setText("Cash Discount - \u20B9 " + item.getCash());
                scratchcard.setText("Scratch Discount - \u20B9 " + item.getScratch());
                bill.setText("Total Bill - \u20B9 " + item.getAmount());
                balance.setText("Balance Collect - \u20B9 " + String.valueOf(nb));


                oid = response.body().getData().getId();


                //}

                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<onlinePayBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });

    }
}
