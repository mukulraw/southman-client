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
    @SerializedName("items_purchased")
    @Expose
    private String itemsPurchased;
    @SerializedName("verified_bills_amount")
    @Expose
    private String verifiedBillsAmount;
    @SerializedName("perks_redeemed")
    @Expose
    private String perksRedeemed;
    @SerializedName("cash_rewards_redeemed")
    @Expose
    private String cashRewardsRedeemed;
    @SerializedName("scratch_cards_redeemed")
    @Expose
    private String scratchCardsRedeemed;
    @SerializedName("bills_uploaded")
    @Expose
    private String billsUploaded;
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

    public String getItemsPurchased() {
        return itemsPurchased;
    }

    public void setItemsPurchased(String itemsPurchased) {
        this.itemsPurchased = itemsPurchased;
    }

    public String getVerifiedBillsAmount() {
        return verifiedBillsAmount;
    }

    public void setVerifiedBillsAmount(String verifiedBillsAmount) {
        this.verifiedBillsAmount = verifiedBillsAmount;
    }

    public String getPerksRedeemed() {
        return perksRedeemed;
    }

    public void setPerksRedeemed(String perksRedeemed) {
        this.perksRedeemed = perksRedeemed;
    }

    public String getCashRewardsRedeemed() {
        return cashRewardsRedeemed;
    }

    public void setCashRewardsRedeemed(String cashRewardsRedeemed) {
        this.cashRewardsRedeemed = cashRewardsRedeemed;
    }

    public String getScratchCardsRedeemed() {
        return scratchCardsRedeemed;
    }

    public void setScratchCardsRedeemed(String scratchCardsRedeemed) {
        this.scratchCardsRedeemed = scratchCardsRedeemed;
    }

    public String getBillsUploaded() {
        return billsUploaded;
    }

    public void setBillsUploaded(String billsUploaded) {
        this.billsUploaded = billsUploaded;
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
