package org.cryptonews.main.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface API {

    @GET("/v1/cryptocurrency/listings/latest?")
    Call<Answer> getWallets(@Query("limit") int limit, @Query("start") int start, @Query("sort") String type, @Query("sort_dir") String order);
    @GET("/v1/cryptocurrency/info")
    Call<Metadata> getMetadata(@Query("id") String symbols);
    @GET("/v1/cryptocurrency/quotes/latest")
    Call<Favorites> getFavorites(@Query("id") String symbols);

    @GET("/v1/cryptocurrency/info")
    Call<Metadata> getSearchMetadata(@Query("slug") String symbols);
    @GET("/v1/cryptocurrency/quotes/latest")
    Call<Favorites> getFavoritesSearch(@Query("slug") String symbols);

}
