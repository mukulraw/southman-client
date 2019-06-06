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
    @POST("bigboss/api/login.php")
    Call<loginBean> login(
            @Part("username") String username,
            @Part("password") String password,
            @Part("token") String token
    );

    @Multipart
    @POST("bigboss/api/register2.php")
    Call<loginBean> register(
            @Part("name") String name,
            @Part("phone") String phone,
            @Part("message") String messae
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
    @POST("bigboss/api/verifyBill.php")
    Call<billBean> verifyBill(
            @Part("id") String id,
            @Part("amount") String amount
    );

    @Multipart
    @POST("bigboss/api/verifyBill2.php")
    Call<billBean> verifyBill2(
            @Part("id") String id,
            @Part("amount") String amount
    );

    @Multipart
    @POST("bigboss/api/getOrders.php")
    Call<orderBean> getOrders(
            @Part("id") String client,
            @Part("date") String date
    );

    @Multipart
    @POST("bigboss/api/getOrders12.php")
    Call<orderBean> getOrders12(
            @Part("id") String client,
            @Part("date") String date
    );

    @Multipart
    @POST("bigboss/api/getOrders13.php")
    Call<orderBean> getOrders13(
            @Part("id") String client,
            @Part("date") String date
    );

    @Multipart
    @POST("bigboss/api/getOrders3.php")
    Call<orderBean> getOrders3(
            @Part("id") String client,
            @Part("date") String date
    );

    @Multipart
    @POST("bigboss/api/getOrders32.php")
    Call<orderBean> getOrders32(
            @Part("id") String client,
            @Part("date") String date
    );

    @Multipart
    @POST("bigboss/api/getOrders33.php")
    Call<orderBean> getOrders33(
            @Part("id") String client,
            @Part("date") String date
    );

    @Multipart
    @POST("bigboss/api/getOrders2.php")
    Call<orderBean> getOrders2(
            @Part("id") String client,
            @Part("date") String date
    );

    @Multipart
    @POST("bigboss/api/getOrders22.php")
    Call<orderBean> getOrders22(
            @Part("id") String client,
            @Part("date") String date
    );

    @Multipart
    @POST("bigboss/api/getOrders23.php")
    Call<orderBean> getOrders23(
            @Part("id") String client,
            @Part("date") String date
    );

    @Multipart
    @POST("bigboss/api/getCurrent.php")
    Call<currentBean> getCurrent(
            @Part("client") String client
    );

    @Multipart
    @POST("bigboss/api/completeTrans.php")
    Call<currentBean> completeTrans(
            @Part("client") String client
    );

    @Multipart
    @POST("bigboss/api/cancelOrder.php")
    Call<currentBean> cancelOrder(
            @Part("id") String user
    );

    @Multipart
    @POST("bigboss/api/getTrans.php")
    Call<transBean> getTrans(
            @Part("client") String client,
            @Part("date") String date
    );

    @GET("bigboss/api/getClients.php")
    Call<clientsBean> getClients();

    @Multipart
    @POST("bigboss/api/completeOrder.php")
    Call<orderBean> completeOrder(
            @Part("id") String client
    );

}
