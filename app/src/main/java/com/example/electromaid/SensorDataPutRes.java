package com.example.electromaid;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SensorDataPutRes {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("message")
    @Expose
    private String message;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public SensorDataPutRes(String id, String message) {
        this.id = id;
        this.message = message;
    }
}
