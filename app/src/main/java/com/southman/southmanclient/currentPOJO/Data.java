package com.southman.southmanclient.currentPOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("full_name")
    @Expose
    private String fullName;
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
    @SerializedName("reserved")
    @Expose
    private String reserved;
    @SerializedName("cdm")
    @Expose
    private String cdm;
    @SerializedName("southman")
    @Expose
    private String southman;
    @SerializedName("to_southman")
    @Expose
    private String toSouthman;
    @SerializedName("from_southman")
    @Expose
    private String fromSouthman;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public String getReserved() {
        return reserved;
    }

    public void setReserved(String reserved) {
        this.reserved = reserved;
    }

    public String getCdm() {
        return cdm;
    }

    public void setCdm(String cdm) {
        this.cdm = cdm;
    }

    public String getSouthman() {
        return southman;
    }

    public void setSouthman(String southman) {
        this.southman = southman;
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
}
