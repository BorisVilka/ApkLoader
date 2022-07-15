package org.cryptonews.main.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Info implements Serializable {

    @SerializedName("logo")
    @Expose
    private String logo;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("date_added")
    @Expose
    private String date_added;
    @SerializedName("date_launched")
    @Expose
    private String date_launched;
    @SerializedName("urls")
    @Expose
    Map<String, List<String>> urls;

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate_added() {
        return date_added;
    }

    public void setDate_added(String date_added) {
        this.date_added = date_added;
    }

    public String getDate_launched() {
        return date_launched;
    }

    public void setDate_launched(String date_launched) {
        this.date_launched = date_launched;
    }

    public Map<String, List<String>> getUrls() {
        return urls;
    }

    public void setUrls(Map<String, List<String>> urls) {
        this.urls = urls;
    }
}
