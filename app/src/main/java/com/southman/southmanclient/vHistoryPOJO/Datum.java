package com.southman.southmanclient.vHistoryPOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("txn")
    @Expose
    private String txn;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("client")
    @Expose
    private String client;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("mode")
    @Expose
    private String mode;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("pid")
    @Expose
    private String pid;
    @SerializedName("cash")
    @Expose
    private String cash;
    @SerializedName("scratch")
    @Expose
    private String scratch;
    @SerializedName("user_id")
    @Expose
    private String user_id;
    @SerializedName("user")
    @Expose
    private String user;
    @SerializedName("rating")
    @Expose
    private String rating;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTxn() {
        return txn;
    }

    public void setTxn(String txn) {
        this.txn = txn;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getStatus() {
        return status;
    }

    public String getMode() {
        return mode;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getCash() {
        return cash;
    }

    public String getScratch() {
        return scratch;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }

    public void setScratch(String scratch) {
        this.scratch = scratch;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
