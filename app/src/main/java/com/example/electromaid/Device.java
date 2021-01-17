package com.example.electromaid;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Device {

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

    public Device(String id_device, String daya, String status, String aktif) {
        this.id_device = id_device;
        this.daya = daya;
        this.status = status;
        this.aktif = aktif;
    }

    public String getId_device() {
        return id_device;
    }

    public void setId_device(String id_device) {
        this.id_device = id_device;
    }

    public String getDaya() {
        return daya;
    }

    public void setDaya(String daya) {
        this.daya = daya;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAktif() {
        return aktif;
    }

    public void setAktif(String aktif) {
        this.aktif = aktif;
    }

    @Override
    public String toString() {
        return "Device{" +
                "id_device='" + id_device + '\'' +
                ", daya='" + daya + '\'' +
                ", status='" + status + '\'' +
                ", aktif='" + aktif + '\'' +
                '}';
    }
}
