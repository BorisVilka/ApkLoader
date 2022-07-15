package org.cryptonews.main.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Status {

    @SerializedName("timestamp")
    @Expose
    private String timestamp;

    @SerializedName("error_message")
    @Expose
    private String error_message;

    @SerializedName("error_code")
    @Expose
    private int error_code;

    @SerializedName("elapsed")
    @Expose
    private int elapsed;

    @SerializedName("credit_count")
    @Expose
    private int credit_count;
}
