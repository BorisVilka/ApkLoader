package org.cryptonews.main.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Quote implements Serializable {

    @SerializedName("USD")
    @Expose
    private USD usd;

    public USD getUsd() {
        return usd;
    }

    public void setUsd(USD usd) {
        this.usd = usd;
    }

}
