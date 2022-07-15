package org.cryptonews.main.network;

import android.content.Context;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.cryptonews.main.ui.activities.MyApp;

import java.io.Serializable;

public class USD implements Serializable {

    @SerializedName("price")
    @Expose
    private double price;
    @SerializedName("volume_24h")
    @Expose
    private double volume_24h;
    @SerializedName("volume_change_24h")
    @Expose
    private double volume_change24h;
    @SerializedName("percent_change_1h")
    @Expose
    private double percent_change1h;
    @SerializedName("percent_change_24h")
    @Expose
    private double percent_change24h;
    @SerializedName("percent_change_7d")
    @Expose
    private double percent_change_7d;
    @SerializedName("market_cap")
    @Expose
    private double market_cap;
    @SerializedName("market_cap_dominance")
    @Expose
    private double market_cap_dominance;
    @SerializedName("last_updated")
    @Expose
    private String last_updated;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getVolume_24h() {
        return volume_24h;
    }

    public void setVolume_24h(double volume_24h) {
        this.volume_24h = volume_24h;
    }

    public double getVolume_change24h() {
        return volume_change24h;
    }

    public void setVolume_change24h(double volume_change24h) {
        this.volume_change24h = volume_change24h;
    }

    public double getPercent_change1h() {
        return percent_change1h;
    }

    public void setPercent_change1h(double percent_change1h) {
        this.percent_change1h = percent_change1h;
    }


    public double getMarket_cap() {
        return market_cap;
    }

    public void setMarket_cap(double market_cap) {
        this.market_cap = market_cap;
    }

    public double getMarket_cap_dominance() {
        return market_cap_dominance;
    }

    public void setMarket_cap_dominance(double market_cap_dominance) {
        this.market_cap_dominance = market_cap_dominance;
    }

    public String getLast_updated() {
        return last_updated;
    }

    public void setLast_updated(String last_updated) {
        this.last_updated = last_updated;
    }
    public double getPercent_change24h() {
        return percent_change24h;
    }

    public void setPercent_change24h(double percent_change24h) {
        this.percent_change24h = percent_change24h;
    }

    public double getPercent_change_7d() {
        return percent_change_7d;
    }

    public void setPercent_change_7d(double percent_change_7d) {
        this.percent_change_7d = percent_change_7d;
    }

    public double current_percentChange() {
        int type = MyApp.getUtils().getContext().getSharedPreferences(MyApp.prefs, Context.MODE_PRIVATE).getInt(MyApp.changes,MyApp.week);
        double ans = getPercent_change_7d();
        switch (type) {
            case MyApp.hour:
                ans = getPercent_change1h();
                break;
            case MyApp.day:
                ans = getPercent_change24h();
                break;
            case MyApp.week:
                ans = getPercent_change_7d();
                break;
        }
        return ans;
    }
}
