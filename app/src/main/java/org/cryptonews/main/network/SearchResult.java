package org.cryptonews.main.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResult {

    @SerializedName("coins")
    @Expose
    public List<SearchItem> coins;

}
