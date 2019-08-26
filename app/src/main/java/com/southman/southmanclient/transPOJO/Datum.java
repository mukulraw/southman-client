package com.southman.southmanclient.transPOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("client")
    @Expose
    private String client;
    @SerializedName("voucher")
    @Expose
    private String voucher;
    @SerializedName("redeem")
    @Expose
    private String redeem;
    @SerializedName("gpay")
    @Expose
    private String gpay;
    @SerializedName("cash")
    @Expose
    private String cash;
    @SerializedName("to_southman")
    @Expose
    private String toSouthman;
    @SerializedName("from_southman")
    @Expose
    private String fromSouthman;
    @SerializedName("created")
    @Expose
    private String created;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getVoucher() {
        return voucher;
    }

    public void setVoucher(String voucher) {
        this.voucher = voucher;
    }

    public String getRedeem() {
        return redeem;
    }

    public void setRedeem(String redeem) {
        this.redeem = redeem;
    }

    public String getGpay() {
        return gpay;
    }

    public void setGpay(String gpay) {
        this.gpay = gpay;
    }

    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }

    public String getToSouthman() {
        return toSouthman;
    }

    public void setToSouthman(String toSouthman) {
        this.toSouthman = toSouthman;
    }

    public String getFromSouthman() {
        return fromSouthman;
    }

    public void setFromSouthman(String fromSouthman) {
        this.fromSouthman = fromSouthman;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
