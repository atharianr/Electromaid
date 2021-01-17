package com.example.electromaid;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SensorData {
    @SerializedName("master_id")
    @Expose
    private String master_id;

    @SerializedName("watt_total")
    @Expose
    private String watt_total;

    @SerializedName("_id")
    @Expose
    private Id _id;

    @SerializedName("device")
    @Expose
    private ArrayList<Device> device;

    public void setMaster_id(String master_id) {
        this.master_id = master_id;
    }

    public void setWatt_total(String watt_total) {
        this.watt_total = watt_total;
    }

    public void set_id(Id _id) {
        this._id = _id;
    }

    public void setDevice(ArrayList<Device> device) {
        this.device = device;
    }

    public String getMaster_id() {
        return master_id;
    }

    public String getWatt_total() {
        return watt_total;
    }

    public Id get_id() {
        return _id;
    }

    public ArrayList<Device> getDevice() {
        return device;
    }

    public SensorData(String master_id, String watt_total, Id _id, ArrayList<Device> device) {
        this.master_id = master_id;
        this.watt_total = watt_total;
        this._id = _id;
        this.device = device;
    }

    @Override
    public String toString() {
        return "SensorData{" +
                "master_id='" + master_id + '\'' +
                ", watt_total='" + watt_total + '\'' +
                ", _id=" + _id +
                ", device=" + device +
                '}';
    }
}
