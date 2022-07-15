package org.cryptonews.main.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MarketData {

    @SerializedName("price_change_percentage_24h")
    @Expose
    public double d1;
    @SerializedName("price_change_percentage_7d")
    @Expose
    public double d7;
    @SerializedName("price_change_percentage_30d")
    @Expose
    public double d30;
    @SerializedName("price_change_percentage_1y")
    @Expose
    public double y1;

}
