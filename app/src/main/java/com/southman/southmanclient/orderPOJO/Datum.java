package com.southman.southmanclient.orderPOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("deviceId")
    @Expose
    private String deviceId;
    @SerializedName("user")
    @Expose
    private String user;
    @SerializedName("cash_value")
    @Expose
    private String cashValue;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("table_name")
    @Expose
    private String tableName;
    @SerializedName("cash_rewards")
    @Expose
    private String cashRewards;
    @SerializedName("scratch_amount")
    @Expose
    private String scratchAmount;
    @SerializedName("bill")
    @Expose
    private String bill;
    @SerializedName("bill_amount")
    @Expose
    private String billAmount;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("created")
    @Expose
    private String created;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getCashValue() {
        return cashValue;
    }

    public void setCashValue(String cashValue) {
        this.cashValue = cashValue;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getCashRewards() {
        return cashRewards;
    }

    public void setCashRewards(String cashRewards) {
        this.cashRewards = cashRewards;
    }

    public String getScratchAmount() {
        return scratchAmount;
    }

    public void setScratchAmount(String scratchAmount) {
        this.scratchAmount = scratchAmount;
    }

    public String getBill() {
        return bill;
    }

    public void setBill(String bill) {
        this.bill = bill;
    }

    public String getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(String billAmount) {
        this.billAmount = billAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
