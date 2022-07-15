package org.cryptonews.main.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface API1 {
    @GET("api/v3/coins/{id}/market_chart/range")
    Call<Graph> getGraph(@Path("id")String id,@Query("vs_currency") String usd, @Query("from") String time_old, @Query("to") String time_now);

    @GET("api/v3/search")
    Call<SearchResult> getSearch(@Query("query") String query);

    @GET("api/v3/coins/{id}")
    Call<MarketCoin> getCoin(@Path("id") String id);
}
