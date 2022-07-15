package org.cryptonews.main.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Graph {

    @SerializedName("prices")
    @Expose
    public List<List<String>> list;

}
