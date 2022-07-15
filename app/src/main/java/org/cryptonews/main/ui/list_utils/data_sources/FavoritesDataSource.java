package org.cryptonews.main.ui.list_utils.data_sources;

import androidx.annotation.NonNull;
import androidx.paging.PositionalDataSource;

import org.cryptonews.main.ui.activities.MyApp;
import org.cryptonews.main.Utils;
import org.cryptonews.main.network.API;
import org.cryptonews.main.network.Favorites;
import org.cryptonews.main.network.Metadata;
import org.cryptonews.main.ui.list_utils.ListItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;

public class FavoritesDataSource extends PositionalDataSource<ListItem> {

    private List<String> ids;

    public FavoritesDataSource(List<String> ids) {
        this.ids = ids;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback<ListItem> callback) {
        if(params.requestedStartPosition>=ids.size()) {
            callback.onResult(new ArrayList<>(),params.requestedStartPosition);
            return;
        }
        Retrofit retrofit = MyApp.getClient().getRetrofitInstance();
        API api = retrofit.create(API.class);
        Call<Favorites> call = api.getFavorites(Utils.getFavoritesQuery(ids,params.requestedStartPosition,params.requestedLoadSize));
        try {
            Favorites favorites = call.execute().body();
            Call<Metadata> metadataCall = api.getMetadata(Utils.getFavoritesQuery(ids,params.requestedStartPosition,params.requestedLoadSize));
            Metadata metadata = metadataCall.execute().body();
            List<ListItem> list = new ArrayList<>();
            for(String s:favorites.getData().keySet()) {
                list.add(new ListItem(favorites.getData().get(s), metadata.getInfo().get(s)));
            }
            callback.onResult(list,params.requestedStartPosition);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<ListItem> callback) {
        if(params.startPosition>=ids.size()) {
            callback.onResult(new ArrayList<>());
            return;
        }
        Retrofit retrofit = MyApp.getClient().getRetrofitInstance();
        API api = retrofit.create(API.class);
        Call<Favorites> call = api.getFavorites(Utils.getFavoritesQuery(ids,params.startPosition,params.loadSize));
        try {
            Favorites favorites = call.execute().body();
            Call<Metadata> metadataCall = api.getMetadata(Utils.getFavoritesQuery(ids,params.startPosition,params.loadSize));
            Metadata metadata = metadataCall.execute().body();
            List<ListItem> list = new ArrayList<>();
            for(String s:favorites.getData().keySet()) {
                list.add(new ListItem(favorites.getData().get(s), metadata.getInfo().get(s)));
            }
            callback.onResult(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getIds() {return ids;}


}
