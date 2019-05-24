package com.southman.southmanclient;

import com.southman.southmanclient.billPOJO.billBean;
import com.southman.southmanclient.loginPOJO.loginBean;
import com.southman.southmanclient.orderPOJO.orderBean;

import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface AllApiIneterface {


    @Multipart
    @POST("bigboss/api/login.php")
    Call<loginBean> login(
            @Part("username") String username,
            @Part("password") String password,
            @Part("token") String token
    );

    @Multipart
    @POST("bigboss/api/getBills.php")
    Call<billBean> getBills(
            @Part("client") String client
    );

    @Multipart
    @POST("bigboss/api/getBills3.php")
    Call<billBean> getBills3(
            @Part("client") String client
    );

    @Multipart
    @POST("bigboss/api/getOrders.php")
    Call<orderBean> getOrders(
            @Part("id") String client
    );

    @Multipart
    @POST("bigboss/api/getOrders3.php")
    Call<orderBean> getOrders3(
            @Part("id") String client
    );

    @Multipart
    @POST("bigboss/api/getOrders2.php")
    Call<orderBean> getOrders2(
            @Part("id") String client
    );

    @Multipart
    @POST("bigboss/api/completeOrder.php")
    Call<orderBean> completeOrder(
            @Part("id") String client
    );

}
