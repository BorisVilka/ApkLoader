package org.cryptonews.main.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class Metadata {
    @SerializedName("data")
    @Expose
    private Map<String, Info> info;

    @SerializedName("status")
    @Expose
    private Status status;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Map<String, Info> getInfo() {
        return info;
    }

    public void setInfo(Map<String, Info> info) {
        this.info = info;
    }

}
