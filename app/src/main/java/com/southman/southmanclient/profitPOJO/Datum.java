package com.southman.southmanclient.profitPOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("full_name")
    @Expose
    private String fullName;
    @SerializedName("cdm")
    @Expose
    private String cdm;
    @SerializedName("total_profit")
    @Expose
    private String totalProfit;
    @SerializedName("month_profit")
    @Expose
    private String monthProfit;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCdm() {
        return cdm;
    }

    public void setCdm(String cdm) {
        this.cdm = cdm;
    }

    public String getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(String totalProfit) {
        this.totalProfit = totalProfit;
    }

    public String getMonthProfit() {
        return monthProfit;
    }

    public void setMonthProfit(String monthProfit) {
        this.monthProfit = monthProfit;
    }

}
