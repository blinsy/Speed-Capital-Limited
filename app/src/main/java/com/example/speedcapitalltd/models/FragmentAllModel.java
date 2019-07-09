package com.example.speedcapitalltd.models;

public class FragmentAllModel {
    private int image;
    private String payment;
    private String ClientName;
    private String Amount;
    private String date;

    public FragmentAllModel(int image, String payment, String clientName, String amount, String date) {
        this.image = image;
        this.payment = payment;
        this.ClientName = clientName;
        this.Amount = amount;
        this.date = date;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getClientName() {
        return ClientName;
    }

    public void setClientName(String clientName) {
        ClientName = clientName;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
