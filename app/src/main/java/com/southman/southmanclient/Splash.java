package com.southman.southmanclient;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.southman.southmanclient.loginPOJO.loginBean;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Splash extends AppCompatActivity {

    Timer timer;
    ProgressBar progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        progress = findViewById(R.id.progress);

        String id = SharePreferenceUtils.getInstance().getString("id");
        String username = SharePreferenceUtils.getInstance().getString("username");
        final String password = SharePreferenceUtils.getInstance().getString("password");

        if (id.length() > 0)
        {


            progress.setVisibility(View.VISIBLE);

            Bean b = (Bean) getApplicationContext();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(b.baseurl)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

            Log.d("asdas" , SharePreferenceUtils.getInstance().getString("token"));

            Call<loginBean> call = cr.login(username, password, SharePreferenceUtils.getInstance().getString("token"));

            call.enqueue(new Callback<loginBean>() {
                @Override


                public void onResponse(Call<loginBean> call, Response<loginBean> response) {


                    if (response.body().getStatus().equals("1")) {

                        SharePreferenceUtils.getInstance().saveString("id" , response.body().getData().getId());
                        SharePreferenceUtils.getInstance().saveString("name" , response.body().getData().getName());
                        SharePreferenceUtils.getInstance().saveString("username" , response.body().getData().getUsername());
                        SharePreferenceUtils.getInstance().saveString("password" , password);
                        SharePreferenceUtils.getInstance().saveString("type" , response.body().getData().getTyp());

                        if (response.body().getData().getTyp().equals("admin"))
                        {
                            Intent i = new Intent(Splash.this, MainActivity2.class);
                            startActivity(i);
                            finishAffinity();
                        }
                        else
                        {
                            Intent i = new Intent(Splash.this, MainActivity.class);
                            startActivity(i);
                            finishAffinity();
                        }


                    }
                    else
                    {
                        SharePreferenceUtils.getInstance().deletePref();
                        Intent i = new Intent(Splash.this , Login.class);
                        startActivity(i);
                        finish();
                    }

                    Toast.makeText(Splash.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    progress.setVisibility(View.GONE);

                }

                @Override
                public void onFailure(Call<loginBean> call, Throwable t) {
                    progress.setVisibility(View.GONE);
                }
            });


                    /*if (SharePreferenceUtils.getInstance().getString("type").equals("admin"))
                    {
                        Intent i = new Intent(Splash.this , MainActivity2.class);
                        startActivity(i);
                        finish();
                    }
                    else
                    {
                        Intent i = new Intent(Splash.this , MainActivity.class);
                        startActivity(i);
                        finish();
                    }*/


        }
        else
        {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {



                    Intent i = new Intent(Splash.this , Login.class);
                    startActivity(i);
                    finish();



                }
            } , 1500);


        }



    }
}
