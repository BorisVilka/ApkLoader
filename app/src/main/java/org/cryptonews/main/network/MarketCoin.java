package org.cryptonews.main.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MarketCoin {

    @SerializedName("market_data")
    @Expose
    public MarketData data;
}
