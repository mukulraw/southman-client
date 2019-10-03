package com.southman.southmanclient;

import android.app.SharedElementCallback;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.southman.southmanclient.loginPOJO.loginBean;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Login extends AppCompatActivity {

    Button submit;

    TextView signup;

    EditText email, pass;

    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signup = findViewById(R.id.signup);

        submit = findViewById(R.id.submit);

        progress = findViewById(R.id.progress);

        email = findViewById(R.id.email);

        pass = findViewById(R.id.password);


        /*FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( Login.this,  new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                Log.e("newToken",newToken);

                SharePreferenceUtils.getInstance().saveString("token" , newToken);

            }
        });*/


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i =new Intent(Login.this , Register.class);
                startActivity(i);
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String u = email.getText().toString();
                String p = pass.getText().toString();

                if (u.length() > 0) {
                    if (p.length() > 0) {

                        progress.setVisibility(View.VISIBLE);

                        Bean b = (Bean) getApplicationContext();

                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(b.baseurl)
                                .addConverterFactory(ScalarsConverterFactory.create())
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

                        Log.d("asdas" , SharePreferenceUtils.getInstance().getString("token"));

                        Call<loginBean> call = cr.login(u, p, SharePreferenceUtils.getInstance().getString("token"));

                        call.enqueue(new Callback<loginBean>() {
                            @Override


                            public void onResponse(Call<loginBean> call, Response<loginBean> response) {


                                if (response.body().getStatus().equals("1")) {

                                    SharePreferenceUtils.getInstance().saveString("id" , response.body().getData().getId());
                                    SharePreferenceUtils.getInstance().saveString("name" , response.body().getData().getName());
                                    SharePreferenceUtils.getInstance().saveString("username" , response.body().getData().getUsername());
                                    SharePreferenceUtils.getInstance().saveString("type" , response.body().getData().getTyp());

                                    if (response.body().getData().getTyp().equals("admin"))
                                    {
                                        Intent i = new Intent(Login.this, MainActivity2.class);
                                        startActivity(i);
                                        finishAffinity();
                                    }
                                    else
                                    {
                                        Intent i = new Intent(Login.this, MainActivity.class);
                                        startActivity(i);
                                        finishAffinity();
                                    }


                                }

                                Toast.makeText(Login.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                progress.setVisibility(View.GONE);

                            }

                            @Override
                            public void onFailure(Call<loginBean> call, Throwable t) {
                                progress.setVisibility(View.GONE);
                            }
                        });

                    } else {
                        Toast.makeText(Login.this, "Invalid password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Login.this, "Invalid username", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }
}
