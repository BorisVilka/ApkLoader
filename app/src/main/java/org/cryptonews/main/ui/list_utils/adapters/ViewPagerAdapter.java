package org.cryptonews.main.ui.list_utils.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.cryptonews.main.InfoFragment;
import org.cryptonews.main.ui.coin.CoinFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    private Fragment[] fragments;

    public ViewPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
        fragments = new Fragment[]{CoinFragment.getInstance(fragment.getArguments()), InfoFragment.newInstance(fragment.getArguments())};
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments[position];
    }

    @Override
    public int getItemCount() {
        return fragments.length;
    }

    public CoinFragment getCoinFragment() {
        return (CoinFragment) fragments[0];
    }
}
