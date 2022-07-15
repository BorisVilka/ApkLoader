package org.cryptonews.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import org.cryptonews.main.databinding.FragmentFavoritesBinding;
import org.cryptonews.main.ui.DialogReference;
import org.cryptonews.main.ui.activities.MyApp;
import org.cryptonews.main.ui.dialogs.DialogSort;
import org.cryptonews.main.ui.list_utils.ListItem;
import org.cryptonews.main.ui.list_utils.MyExecutor;
import org.cryptonews.main.ui.list_utils.PagedDiffUtilCallback;
import org.cryptonews.main.ui.list_utils.adapters.PagedAdapter;
import org.cryptonews.main.ui.list_utils.data_sources.FavoritesDataSource;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleOnSubscribe;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class FavoritesFragment extends Fragment implements DialogReference {

    private FragmentFavoritesBinding binding;
    private PagedAdapter adapter;
    private InterstitialAd mInterstitialAd;
    private DialogFragment fragment;
    private SharedPreferences preferences;
    private FavoritesDataSource dataSource;
    private PagedDiffUtilCallback callback;
    private PagedList.Config config;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        preferences = getContext().getSharedPreferences(MyApp.prefs, Context.MODE_PRIVATE);
        binding = FragmentFavoritesBinding.inflate(inflater);
        setHasOptionsMenu(true);
        binding.favoriteList.addItemDecoration(new DividerItemDecoration(binding.favoriteList.getContext(),DividerItemDecoration.VERTICAL));
        binding.favoriteList.setLayoutManager(new LinearLayoutManager(getContext()));
        AdRequest request = new AdRequest.Builder().build();
        binding.adView4.loadAd(request);
        binding.swipeFav.setColorSchemeColors(Color.BLUE,Color.MAGENTA,Color.GREEN);
        binding.swipeFav.setOnRefreshListener(() -> {
            loadList();
        });
        binding.switchMarketFav.setChecked(preferences.getBoolean(MyApp.marketInfo_fav,false));
        binding.switchMarketFav.setOnCheckedChangeListener((compoundButton, b) -> {
            preferences.edit().putBoolean(MyApp.marketInfo_fav, b).commit();
            adapter.notifyDataSetChanged();
        });
        binding.sortItemFav.setOnClickListener(view -> {
            fragment = new DialogSort(FavoritesFragment.this, preferences.getInt(MyApp.checked_index_fav,0));
            fragment.show(getActivity().getSupportFragmentManager(),"TAG");
        });
        callback = new PagedDiffUtilCallback();
        config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(10)
                .setPrefetchDistance(5)
                .setInitialLoadSizeHint(10)
                .build();
        setAdapter();
        binding.favoriteList.setAdapter(adapter);
        selectSort(null,0,0,false);
        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.open) {
            if(binding.motionFav.getCurrentState()==R.id.start_fav) binding.motionFav.transitionToState(R.id.end_fav);
            else binding.motionFav.transitionToState(R.id.start_fav);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void selectSort(String type, int order, int ind, boolean b) {
        loadList();
    }
    private void loadList() {
        Single<PagedList<ListItem>> completable = Single.create((SingleOnSubscribe<PagedList<ListItem>>) emitter -> {
            PagedList<ListItem> list = new PagedList.Builder(dataSource,config)
                    .setFetchExecutor(Executors.newSingleThreadExecutor())
                    .setNotifyExecutor(new MyExecutor())
                    .build();
            emitter.onSuccess(list);
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        binding.swipeFav.setRefreshing(true);
        completable.subscribe(listItems -> {
            adapter.submitList(listItems);
            binding.swipeFav.setRefreshing(false);
        });
    }
    private void setAdapter() {
        Set<String> set1 = getContext().getSharedPreferences(MyApp.prefs, Context.MODE_PRIVATE).getStringSet(MyApp.favorites,new HashSet<>());
        List<String> list = new ArrayList<>();
        set1.stream().forEach((s)->{list.add(s);});
        dataSource = new FavoritesDataSource(list);
        adapter = new PagedAdapter(callback, (item, position)->{
            MyApp.count++;
            if(MyApp.count%3==0) {
                AdRequest adRequest = new AdRequest.Builder().build();
                InterstitialAd.load(getContext(),getString(R.string.ads_id), adRequest,
                        new InterstitialAdLoadCallback() {
                            @Override
                            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                                // The mInterstitialAd reference will be null until
                                // an ad is loaded.
                                interstitialAd.show(getActivity());
                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                // Handle the error
                                Log.d("TAG","Error "+loadAdError.getMessage());
                            }
                        });
            }
            Bundle bundle = new Bundle();
            bundle.putSerializable("Coin",item);
            bundle.putInt("Position",position);
            Navigation.findNavController(getActivity(),R.id.nav_host_fragment_content_main)
                    .navigate(R.id.action_nav_favorites_to_rootFragment,bundle);
        }, (checked, item, position)-> {
            Log.d("TAG",item.getCoin().getName()+" "+position);
            Set<String> set = preferences.getStringSet(MyApp.favorites,new HashSet<>());
            Set<String> newSet = new HashSet<>(set);
            if(checked) {
                newSet.add(String.valueOf(item.getCoin().getId()));
            } else newSet.remove(String.valueOf(item.getCoin().getId()));
            preferences.edit().putStringSet(MyApp.favorites,newSet).commit();
            dataSource.getIds().remove(position);
            binding.favoriteList.setAdapter(null);
            setAdapter();
            binding.favoriteList.setAdapter(adapter);
            loadList();
        });
    }
}