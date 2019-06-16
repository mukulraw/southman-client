package com.southman.southmanclient;

import com.southman.southmanclient.billPOJO.billBean;
import com.southman.southmanclient.clientsPOJO.clientsBean;
import com.southman.southmanclient.currentPOJO.currentBean;
import com.southman.southmanclient.loginPOJO.loginBean;
import com.southman.southmanclient.orderPOJO.orderBean;
import com.southman.southmanclient.transPOJO.transBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface AllApiIneterface {


    @Multipart
    @POST("southman/api/login.php")
    Call<loginBean> login(
            @Part("username") String username,
            @Part("password") String password,
            @Part("token") String token
    );

    @Multipart
    @POST("southman/api/register2.php")
    Call<loginBean> register(
            @Part("name") String name,
            @Part("phone") String phone,
            @Part("message") String messae
    );

    @Multipart
    @POST("southman/api/getBills.php")
    Call<billBean> getBills(
            @Part("client") String client
    );

    @Multipart
    @POST("southman/api/getBills3.php")
    Call<billBean> getBills3(
            @Part("client") String client
    );

    @Multipart
    @POST("southman/api/verifyBill.php")
    Call<billBean> verifyBill(
            @Part("id") String id,
            @Part("amount") String amount
    );

    @Multipart
    @POST("southman/api/verifyBill2.php")
    Call<billBean> verifyBill2(
            @Part("id") String id,
            @Part("amount") String amount
    );

    @Multipart
    @POST("southman/api/getOrders.php")
    Call<orderBean> getOrders(
            @Part("id") String client,
            @Part("date") String date
    );

    @Multipart
    @POST("southman/api/getOrders12.php")
    Call<orderBean> getOrders12(
            @Part("id") String client,
            @Part("date") String date
    );

    @Multipart
    @POST("southman/api/getOrders13.php")
    Call<orderBean> getOrders13(
            @Part("id") String client,
            @Part("date") String date
    );

    @Multipart
    @POST("southman/api/getOrders3.php")
    Call<orderBean> getOrders3(
            @Part("id") String client,
            @Part("date") String date
    );

    @Multipart
    @POST("southman/api/getOrders32.php")
    Call<orderBean> getOrders32(
            @Part("id") String client,
            @Part("date") String date
    );

    @Multipart
    @POST("southman/api/getOrders33.php")
    Call<orderBean> getOrders33(
            @Part("id") String client,
            @Part("date") String date
    );

    @Multipart
    @POST("southman/api/getOrders2.php")
    Call<orderBean> getOrders2(
            @Part("id") String client,
            @Part("date") String date
    );

    @Multipart
    @POST("southman/api/getOrders22.php")
    Call<orderBean> getOrders22(
            @Part("id") String client,
            @Part("date") String date
    );

    @Multipart
    @POST("southman/api/getOrders23.php")
    Call<orderBean> getOrders23(
            @Part("id") String client,
            @Part("date") String date
    );

    @Multipart
    @POST("southman/api/getCurrent.php")
    Call<currentBean> getCurrent(
            @Part("client") String client
    );

    @Multipart
    @POST("southman/api/completeTrans.php")
    Call<currentBean> completeTrans(
            @Part("client") String client
    );

    @Multipart
    @POST("southman/api/cancelOrder.php")
    Call<currentBean> cancelOrder(
            @Part("id") String user
    );

    @Multipart
    @POST("southman/api/getTrans.php")
    Call<transBean> getTrans(
            @Part("client") String client,
            @Part("date") String date
    );

    @GET("southman/api/getClients.php")
    Call<clientsBean> getClients();

    @Multipart
    @POST("southman/api/completeOrder.php")
    Call<orderBean> completeOrder(
            @Part("id") String client
    );

}
