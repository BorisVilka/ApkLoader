package org.cryptonews.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.material.tabs.TabLayout;

import org.cryptonews.main.databinding.FragmentRootBinding;
import org.cryptonews.main.ui.coin.CoinFragment;
import org.cryptonews.main.ui.list_utils.ListItem;
import org.cryptonews.main.ui.list_utils.adapters.ViewPagerAdapter;


public class RootFragment extends Fragment {

    private FragmentRootBinding binding;
    private ViewPagerAdapter adapter;
    private ListItem listItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listItem = (ListItem) getArguments().getSerializable("Coin");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRootBinding.inflate(inflater);
        adapter = new ViewPagerAdapter(this);
        setHasOptionsMenu(true);
        AdRequest request = new AdRequest.Builder().build();
        binding.adView5.loadAd(request);
        binding.pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                binding.tabs.selectTab(binding.tabs.getTabAt(position));
            }
        });
        binding.tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        binding.pager.setAdapter(adapter);
        // Inflate the layout for this fragment
        adapter.getCoinFragment().listener = new CoinFragment.Listener() {
            @Override
            public void enable() {
                binding.pager.setUserInputEnabled(false);
            }

            @Override
            public void disable() {
                binding.pager.setUserInputEnabled(true);
            }
        };
        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.item,menu);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        boolean checked = Utils.containsFavorites(listItem);
        menu.getItem(0).setIcon(checked!=true ? getResources().getDrawable(R.drawable.ic_baseline_favorite_border_24, getContext().getTheme())
                : getResources().getDrawable(R.drawable.ic_baseline_favorite_24, getContext().getTheme()));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.add_remove) {
            boolean checked = Utils.containsFavorites(listItem);
            item.setIcon(checked==true ? getResources().getDrawable(R.drawable.ic_baseline_favorite_border_24, getContext().getTheme())
                    : getResources().getDrawable(R.drawable.ic_baseline_favorite_24, getContext().getTheme()));
            Utils.favoritesMove(listItem,!checked);
        }
        return super.onOptionsItemSelected(item);
    }
    public FragmentRootBinding getBinding() {return binding;}
}