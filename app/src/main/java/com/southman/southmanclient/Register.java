package com.southman.southmanclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.southman.southmanclient.loginPOJO.loginBean;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Register extends AppCompatActivity {

    Button submit;

    EditText email, pass, mess;

    ProgressBar progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        submit = findViewById(R.id.submit);

        progress = findViewById(R.id.progress);

        email = findViewById(R.id.email);

        pass = findViewById(R.id.password);

        mess = findViewById(R.id.message);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String u = email.getText().toString();
                String p = pass.getText().toString();
                String m = mess.getText().toString();

                if (u.length() > 0) {
                    if (p.length() == 10) {

                        if (m.length() > 0) {
                            progress.setVisibility(View.VISIBLE);

                            Bean b = (Bean) getApplicationContext();

                            Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl(b.baseurl)
                                    .addConverterFactory(ScalarsConverterFactory.create())
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();

                            AllApiIneterface cr = retrofit.create(AllApiIneterface.class);


                            Call<loginBean> call = cr.register(u, p, m);

                            call.enqueue(new Callback<loginBean>() {
                                @Override


                                public void onResponse(Call<loginBean> call, Response<loginBean> response) {

                                    Toast.makeText(Register.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                    progress.setVisibility(View.GONE);

                                    finish();

                                }

                                @Override
                                public void onFailure(Call<loginBean> call, Throwable t) {
                                    progress.setVisibility(View.GONE);
                                }
                            });
                        } else {
                            Toast.makeText(Register.this, "Invalid message", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(Register.this, "Invalid phone", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Register.this, "Invalid username", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }
}
