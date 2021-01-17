package com.example.electromaid;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SensorDataPut {

    @SerializedName("id")
    @Expose
    private String id_device;

    @SerializedName("daya")
    @Expose
    private String daya;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("aktif")
    @Expose
    private String aktif;

    @SerializedName("device")
    @Expose
    private List<Device> devices = null;

    public SensorDataPut(ArrayList<Device> devices) {

    }


    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    @Override
    public String toString() {
        return "SensorDataPut{" +
                "devices=" + devices +
                '}';
    }
}
