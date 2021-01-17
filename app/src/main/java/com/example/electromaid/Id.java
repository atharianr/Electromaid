package com.example.electromaid;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Id {

    @SerializedName("$oid")
    @Expose
    private String oid;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    @Override
    public String toString() {
        return "Id{" +
                "oid='" + oid + '\'' +
                '}';
    }
}
