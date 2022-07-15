package org.cryptonews.main.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import org.cryptonews.main.ui.activities.MyApp;
import org.cryptonews.main.R;
import org.cryptonews.main.Utils;
import org.cryptonews.main.databinding.FragmentHomeBinding;
import org.cryptonews.main.network.API;
import org.cryptonews.main.network.Favorites;
import org.cryptonews.main.network.Metadata;
import org.cryptonews.main.ui.DialogReference;
import org.cryptonews.main.ui.dialogs.DialogSort;
import org.cryptonews.main.ui.dialogs.PercentDialog;
import org.cryptonews.main.ui.list_utils.ListItem;
import org.cryptonews.main.ui.list_utils.PagedDiffUtilCallback;
import org.cryptonews.main.ui.list_utils.adapters.PagedAdapter;
import org.cryptonews.main.ui.list_utils.adapters.SearchAdapter;
import org.cryptonews.main.ui.list_utils.data_sources.PagedDataSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleOnSubscribe;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Retrofit;

public class HomeFragment extends Fragment implements DialogReference {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private RecyclerView recyclerView;
    private PagedAdapter adapter;
    private InterstitialAd mInterstitialAd;
    private DialogFragment fragment;
    private SharedPreferences preferences;
    private PagedDataSource dataSource;
    private PagedDiffUtilCallback callback;
    private PagedList.Config config;
    private SearchView searchView;
    private SearchAdapter adapterSearch;
    private boolean one;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        preferences = getContext().getSharedPreferences(MyApp.prefs, Context.MODE_PRIVATE);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        setHasOptionsMenu(true);
        recyclerView = binding.list;
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL));
        binding.swipe.setColorSchemeColors(Color.BLUE,Color.MAGENTA,Color.GREEN);
        binding.swipe.setOnRefreshListener(() -> {
            homeViewModel.iniList(()->{loadList();});
        });
        MobileAds.initialize(getContext());
        AdRequest request = new AdRequest.Builder().build();
        binding.adView3.loadAd(request);
        binding.switchMarket.setChecked(preferences.getBoolean(MyApp.marketInfo,true));
        binding.switchMarket.setOnCheckedChangeListener((compoundButton, b) -> {
            Log.d("TAG",b+"");
            preferences.edit().putBoolean(MyApp.marketInfo,b).commit();
            adapter.notifyDataSetChanged();
        });
        binding.percentItem.setOnClickListener(view -> {
            fragment = new PercentDialog(HomeFragment.this);
            fragment.show(getActivity().getSupportFragmentManager(),"TAG");
        });
        View root = binding.getRoot();
        binding.sortItem.setOnClickListener(view -> {
            fragment = new DialogSort(HomeFragment.this, preferences.getInt(MyApp.checked_index,1));
            fragment.show(getActivity().getSupportFragmentManager(),"TAG");
        });
        Set<String> set1 = getContext().getSharedPreferences(MyApp.prefs, Context.MODE_PRIVATE).getStringSet(MyApp.favorites,new HashSet<>());
        List<String> list = new ArrayList<>();
        set1.stream().forEach((s)->{list.add(s);});
        Log.d("TAG",list.toString());
        adapterSearch = new SearchAdapter((item, position)->{
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
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("Coin",item);
                                bundle.putInt("Position",position);
                                Navigation.findNavController(getActivity(),R.id.nav_host_fragment_content_main)
                                        .navigate(R.id.action_nav_home_to_rootFragment,bundle);
                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                // Handle the error
                                Log.d("TAG","Error "+loadAdError.getMessage());
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("Coin",item);
                                bundle.putInt("Position",position);
                                Navigation.findNavController(getActivity(),R.id.nav_host_fragment_content_main)
                                        .navigate(R.id.action_nav_home_to_rootFragment,bundle);
                            }
                        });
            } else {
                Bundle bundle = new Bundle();
                bundle.putSerializable("Coin",item);
                bundle.putInt("Position",position);
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment_content_main)
                        .navigate(R.id.action_nav_home_to_rootFragment,bundle);
            }

        }, (checked, item, position)-> {
            Log.d("TAG",checked+" "+position+" "+item.getCoin().getId());
            Utils.favoritesMove(item,checked);
        });
        dataSource = homeViewModel.getDataSource();
        callback = new PagedDiffUtilCallback();
        config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(10)
                .setPrefetchDistance(5)
                .setInitialLoadSizeHint(10)
                .build();
        adapter = new PagedAdapter(callback, (item, position)->{
            if(one) return;
           MyApp.count++;
           one = true;
           if(MyApp.count%3==0) {
               AdRequest adRequest = new AdRequest.Builder().build();
               InterstitialAd.load(getContext(),getString(R.string.ads_id), adRequest,
                       new InterstitialAdLoadCallback() {
                           @Override
                           public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                               // The mInterstitialAd reference will be null until
                               // an ad is loaded.
                               one  = false;
                               interstitialAd.show(getActivity());
                           }

                           @Override
                           public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                               // Handle the error
                               one = false;
                               Log.d("TAG","Error "+loadAdError.getMessage());
                           }
                       });
               Bundle bundle = new Bundle();
               bundle.putSerializable("Coin",item);
               bundle.putInt("Position",position);
               Navigation.findNavController(getActivity(),R.id.nav_host_fragment_content_main)
                       .navigate(R.id.action_nav_home_to_rootFragment,bundle);
           } else {
               one = false;
               Bundle bundle = new Bundle();
               bundle.putSerializable("Coin",item);
               bundle.putInt("Position",position);
               Navigation.findNavController(getActivity(),R.id.nav_host_fragment_content_main)
                       .navigate(R.id.action_nav_home_to_rootFragment,bundle);
           }
        }, (checked, item, position)-> {
                Log.d("TAG",checked+" "+position+" "+item.getCoin().getId());
                Utils.favoritesMove(item,checked);
                });
        recyclerView.setAdapter(adapter);
        select();
        initList();
        return root;
    }
    private void initList() {
        if(homeViewModel.getList()==null) {
            homeViewModel.getCompletable().subscribe(listItems -> {
                homeViewModel.setList(listItems);
                loadList();
            });
        } else loadList();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private void select() {
        binding.typeSort.setText(preferences.getString(MyApp.type_sort,"Ранг"));
        binding.sortTypeIcon.setImageDrawable( preferences.getInt(MyApp.order_sort,0)%2==0
                ? getContext().getDrawable(R.drawable.ic_baseline_straight_24)
                : getContext().getDrawable(R.drawable.ic_baseline_south_24));
        binding.percType.setText(getContext().getResources().getStringArray(R.array.percent_types)[preferences.getInt(MyApp.changes,MyApp.week)]);  binding.typeSort.setText(preferences.getString(MyApp.type_sort,"Ранг"));
        binding.sortTypeIcon.setImageDrawable( preferences.getInt(MyApp.order_sort,0)%2==0
                ? getContext().getDrawable(R.drawable.ic_baseline_straight_24)
                : getContext().getDrawable(R.drawable.ic_baseline_south_24));
        binding.percType.setText(getContext().getResources().getStringArray(R.array.percent_types)[preferences.getInt(MyApp.changes,MyApp.week)]);
    }
    @Override
    public void selectSort(String type, int order, int ind, boolean b) {
       if(b) {
           preferences.edit()
                   .putString(MyApp.type_sort,type)
                   .putInt(MyApp.order_sort,order)
                   .putInt(MyApp.checked_index,ind)
                   .commit();
       }
       binding.typeSort.setText(preferences.getString(MyApp.type_sort,"Ранг"));
        binding.sortTypeIcon.setImageDrawable( preferences.getInt(MyApp.order_sort,0)%2==0
                 ? getContext().getDrawable(R.drawable.ic_baseline_straight_24)
    : getContext().getDrawable(R.drawable.ic_baseline_south_24));
        binding.percType.setText(getContext().getResources().getStringArray(R.array.percent_types)[preferences.getInt(MyApp.changes,MyApp.week)]);
        homeViewModel.iniList(()->{loadList();});
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main,menu);
        searchView = (SearchView) menu.getItem(0).getActionView();
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG","CLICK");
            }
        });
        searchView.setOnCloseListener(() -> {
            Log.d("TAG","CLOSE");
            binding.list.setAdapter(adapter);
            adapterSearch.setData(null);
            return false;
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Single<List<ListItem>> single = Single.create((SingleOnSubscribe<List<ListItem>>)
                        emitter -> emitter.onSuccess(searchResult(s))).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
                single.subscribe(listItems -> {
                    adapterSearch.setData(listItems);
                    binding.list.setAdapter(adapterSearch);
                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.open) {
            if(binding.motion.getCurrentState()==R.id.start) binding.motion.transitionToState(R.id.end);
            else binding.motion.transitionToState(R.id.start);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void loadList() {
        /*Single<PagedList<ListItem>> completable = Single.create((SingleOnSubscribe<PagedList<ListItem>>) emitter -> {
            PagedList<ListItem> list = new PagedList.Builder(dataSource,config)
                    .setFetchExecutor(Executors.newSingleThreadExecutor())
                    .setNotifyExecutor(new MyExecutor())
                    .build();
            emitter.onSuccess(list);
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        binding.swipe.setRefreshing(true);
        completable.subscribe(listItems -> {
            adapter.submitList(listItems);
            adapter.notifyDataSetChanged();
            binding.swipe.setRefreshing(false);
        });*/
        adapter.submitList(homeViewModel.getList());
        adapter.notifyDataSetChanged();
        if(binding!=null) binding.swipe.setRefreshing(false);
    }

    private List<ListItem> searchResult(String key) {
        Retrofit retrofit = MyApp.getClient().getRetrofitInstance();
        API api = retrofit.create(API.class);
        Call<Favorites> call = api.getFavoritesSearch(key);
        try {
            Favorites favorites = call.execute().body();
            Call<Metadata> metadataCall = api.getSearchMetadata(key);
            Metadata metadata = metadataCall.execute().body();
            List<ListItem> list = new ArrayList<>();
            if(favorites==null) {
                return new ArrayList<>();
            }
            for(String s:favorites.getData().keySet()) {
                list.add(new ListItem(favorites.getData().get(s), metadata.getInfo().get(s)));
            }
           return list;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

}