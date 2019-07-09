package com.example.speedcapitalltd.models;

public class SelectClientModel {
    private int image;
    private String recordId;
    private String ClientName;
    private boolean checked = false;

    //
    public SelectClientModel(int image, String recordId, String clientName, boolean checked) {
        this.image = image;
        this.recordId = recordId;
        this.ClientName = clientName;
        this.checked = checked;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getClientName() {
        return ClientName;
    }

    public void setClientName(String clientName) {
        ClientName = clientName;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
