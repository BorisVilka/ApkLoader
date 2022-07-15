package org.cryptonews.main.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Coin implements Serializable {


    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("symbol")
    @Expose
    private String symbol;

    @SerializedName("slug")
    @Expose
    private String slug;

    @SerializedName("cmc_rank")
    @Expose
    private int cmc_rank;

    @SerializedName("num_market_pairs")
    @Expose
    private int num_market_pairs;


    @SerializedName("circulating_apply")
    @Expose
    private int circulating_supply;


    @SerializedName("total_supply")
    @Expose
    private String total_supply;

    @SerializedName("max_supply")
    @Expose
    private String max_supply;


    @SerializedName("last_updated")
    @Expose
    private String last_updated;


    @SerializedName("date_added")
    @Expose
    private String date_added;

    @SerializedName("quote")
    @Expose
    private Quote quote;

    @SerializedName("tags")
    @Expose
    private List<String> tags;

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public int getCmc_rank() {
        return cmc_rank;
    }

    public void setCmc_rank(int cmc_rank) {
        this.cmc_rank = cmc_rank;
    }

    public int getNum_market_pairs() {
        return num_market_pairs;
    }

    public void setNum_market_pairs(int num_market_pairs) {
        this.num_market_pairs = num_market_pairs;
    }

    public int getCirculating_supply() {
        return circulating_supply;
    }

    public void setCirculating_supply(int circulating_supply) {
        this.circulating_supply = circulating_supply;
    }

    public String getTotal_supply() {
        return total_supply;
    }

    public void setTotal_supply(String total_supply) {
        this.total_supply = total_supply;
    }

    public String getLast_updated() {
        return last_updated;
    }

    public void setLast_updated(String last_updated) {
        this.last_updated = last_updated;
    }

    public String getDate_added() {
        return date_added;
    }

    public void setDate_added(String date_added) {
        this.date_added = date_added;
    }

    public String getMax_supply() {
        return max_supply;
    }

    public void setMax_supply(String max_supply) {
        this.max_supply = max_supply;
    }

    public Coin() {
    }

    public Quote getQuote() {
        return quote;
    }

    public void setQuote(Quote quote) {
        this.quote = quote;
    }

    public String getNameAndSymbol() {
        return String.format("%s (%s)",getName(),getSymbol());
    }
}
