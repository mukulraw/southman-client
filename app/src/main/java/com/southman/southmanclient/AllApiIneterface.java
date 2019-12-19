package com.southman.southmanclient;

import com.southman.southmanclient.benefitsPOJO.benefitsBean;
import com.southman.southmanclient.billPOJO.billBean;
import com.southman.southmanclient.clientsPOJO.clientsBean;
import com.southman.southmanclient.createOrderPOJO.createOrderBean;
import com.southman.southmanclient.currentPOJO.currentBean;
import com.southman.southmanclient.gPayPOJO.gPayBean;
import com.southman.southmanclient.loginPOJO.loginBean;
import com.southman.southmanclient.onlinePayPOJO.onlinePayBean;
import com.southman.southmanclient.orderPOJO.orderBean;
import com.southman.southmanclient.profitPOJO.profitBean;
import com.southman.southmanclient.transPOJO.transBean;
import com.southman.southmanclient.vHistoryPOJO.vHistoryBean;
import com.southman.southmanclient.voucherHistoryPOJO.voucherHistoryBean;
import com.southman.southmanclient.voucherPOJO.voucherBean;

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
    @POST("southman/api/getOrders1.php")
    Call<vHistoryBean> getOrders1(
            @Part("id") String client,
            @Part("date") String date
    );

    @Multipart
    @POST("southman/api/getOrders12.php")
    Call<vHistoryBean> getOrders12(
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
    @POST("southman/api/getSingleOrder2.php")
    Call<onlinePayBean> getSingleOrder2(
            @Part("id") String id
    );

    @Multipart
    @POST("southman/api/getOrders3.php")
    Call<orderBean> getOrders3(
            @Part("id") String client,
            @Part("date") String date
    );

    @Multipart
    @POST("southman/api/onlinePay.php")
    Call<onlinePayBean> onlinePay(
            @Part("id") String id,
            @Part("pid") String pid,
            @Part("user_id") String user_id
    );

    @Multipart
    @POST("southman/api/getOrders31.php")
    Call<vHistoryBean> getOrders31(
            @Part("id") String client,
            @Part("date") String date
    );

    @Multipart
    @POST("southman/api/getOrders32.php")
    Call<vHistoryBean> getOrders32(
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
    @POST("southman/api/getBenefits.php")
    Call<benefitsBean> getBenefits(
            @Part("id") String client,
            @Part("date") String date
    );

    @Multipart
    @POST("southman/api/getExpired.php")
    Call<benefitsBean> getExpired(
            @Part("id") String client
    );


    @GET("southman/api/getExpired2.php")
    Call<benefitsBean> getExpired2();

    @GET("southman/api/returnCDM.php")
    Call<benefitsBean> returnCDM();

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
    @POST("southman/api/rejectOrder.php")
    Call<onlinePayBean> cancelOrder(
            @Part("id") String id
    );

    @Multipart
    @POST("southman/api/cancelOrder.php")
    Call<createOrderBean> cancelOrder2(
            @Part("id") String id
    );

    @Multipart
    @POST("southman/api/getTrans.php")
    Call<transBean> getTrans(
            @Part("client") String client,
            @Part("date") String date
    );

    @Multipart
    @POST("southman/api/getProfit.php")
    Call<profitBean> getProfit(
            @Part("date") String date
    );

    @GET("southman/api/getClients.php")
    Call<clientsBean> getClients();

    @Multipart
    @POST("southman/api/completeOrder.php")
    Call<orderBean> completeOrder(
            @Part("id") String client
    );


    @Multipart
    @POST("southman/api/getSingleOrder.php")
    Call<gPayBean> getSingleOrder(
            @Part("id") String id
    );

    @Multipart
    @POST("southman/api/getOrderHistory.php")
    Call<voucherHistoryBean> getOrderHistory(
            @Part("order_id") String order_id
    );

    @Multipart
    @POST("southman/api/createOrder2.php")
    Call<createOrderBean> createOrder(
            @Part("oid") String oid,
            @Part("amount") String amount
    );

}
