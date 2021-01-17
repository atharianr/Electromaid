package com.example.electromaid;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotifRes {
    @SerializedName("_id")
    @Expose
    private Id id;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("notifikasi")
    @Expose
    private String notifikasi;

    @SerializedName("waktu")
    @Expose
    private String waktu;

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotifikasi() {
        return notifikasi;
    }

    public void setNotifikasi(String notifikasi) {
        this.notifikasi = notifikasi;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public NotifRes(String title, String notifikasi, String waktu) {
        this.title = title;
        this.notifikasi = notifikasi;
        this.waktu = waktu;
    }

    @Override
    public String toString() {
        return "NotifRes{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", notifikasi='" + notifikasi + '\'' +
                ", waktu='" + waktu + '\'' +
                '}';
    }
}
