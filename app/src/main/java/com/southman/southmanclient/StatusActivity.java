package com.southman.southmanclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.southman.southmanclient.gPayPOJO.gPayBean;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class StatusActivity extends AppCompatActivity {

    TextView status , amount , client_name , date , order , tag , paid , tid;
    Button share;
    ImageButton back;
    ImageView gpay;

    ProgressBar progress;

    String id;

    String oid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

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

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        progress.setVisibility(View.VISIBLE);


        Log.d("successful" , "success");

        progress.setVisibility(View.VISIBLE);

        Bean b = (Bean) getApplicationContext();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.baseurl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

        Call<gPayBean> call = cr.getSingleOrder(id);
        call.enqueue(new Callback<gPayBean>() {
            @Override
            public void onResponse(Call<gPayBean> call, Response<gPayBean> response) {

                // image.setImageResource(R.drawable.success);
                // text.setText("Voucher purchased successfully. Benefits will get added to your account.");

                status.setText("Payment Successful");
                amount.setText("\u20B9 " + response.body().getData().getAmount());
                amount.setCompoundDrawablesWithIntrinsicBounds(null , getResources().getDrawable(R.drawable.ic_checked) , null , null);
                client_name.setText(response.body().getData().getClient());
                client_name.setVisibility(View.VISIBLE);
                date.setText(response.body().getData().getCreated());
                back.setVisibility(View.VISIBLE);
                paid.setVisibility(View.VISIBLE);
                status.setVisibility(View.VISIBLE);
                amount.setVisibility(View.VISIBLE);
                //tag.setVisibility(View.VISIBLE);
                order.setVisibility(View.VISIBLE);
                date.setVisibility(View.VISIBLE);
                tid.setText("TXN ID - " + response.body().getData().getTxn());
                tid.setVisibility(View.VISIBLE);
                gpay.setVisibility(View.VISIBLE);

                oid = response.body().getData().getId();

                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<gPayBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });



        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(StatusActivity.this , OrderDetails.class);
                intent.putExtra("order" , oid);
                startActivity(intent);

            }
        });

    }
}
