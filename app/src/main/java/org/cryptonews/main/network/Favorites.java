package org.cryptonews.main.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class Favorites {

    @SerializedName("data")
    @Expose
    Map<String,Coin> data;
    @SerializedName("status")
    @Expose
    Status status;

    public Map<String, Coin> getData() {
        return data;
    }

    public void setData(Map<String, Coin> data) {
        this.data = data;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
