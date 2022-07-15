package org.cryptonews.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.squareup.picasso.Picasso;

import org.cryptonews.main.databinding.FragmentInfoBinding;
import org.cryptonews.main.ui.list_utils.ListItem;


public class InfoFragment extends Fragment implements View.OnClickListener {

    private FragmentInfoBinding binding;
    private ListItem item;

    public InfoFragment() {
        // Required empty public constructor
    }

    public static InfoFragment newInstance(Bundle args) {
        InfoFragment fragment = new InfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        item = (ListItem) getArguments().getSerializable("Coin");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInfoBinding.inflate(inflater);
        binding.setItem(item);
        Picasso.get().load(item.getInfo().getLogo()).resize(100,100).into(binding.logoInfo);
        binding.siteInfo.setOnClickListener(this);
        binding.sourceInfo.setOnClickListener(this);
        binding.redditInfo.setOnClickListener(this);
        if(!item.getInfo().getUrls().containsKey("website")) {
            binding.siteInfo.setHeight(0);
            binding.siteInfo.setEnabled(false);
            binding.siteInfo.setVisibility(View.INVISIBLE);
        }
        if(!item.getInfo().getUrls().containsKey("reddit")) {
            binding.redditInfo.setHeight(0);
            binding.redditInfo.setEnabled(false);
            binding.redditInfo.setVisibility(View.INVISIBLE);
        }
        if(!item.getInfo().getUrls().containsKey("source_code")) {
            binding.sourceInfo.setHeight(0);
            binding.sourceInfo.setEnabled(false);
            binding.sourceInfo.setVisibility(View.INVISIBLE);
        }
        Log.d("TAG",item.getInfo().getUrls().toString());
        return binding.getRoot();
    }

    @Override
    public void onClick(View view) {
        String url = null;
        switch (view.getId()) {
            case R.id.site_info:
                url = item.getInfo().getUrls().get("website").get(0);
                break;
            case R.id.reddit_info:
                url = item.getInfo().getUrls().get("reddit").get(0);
                break;
            case R.id.source_info:
                url = item.getInfo().getUrls().get("source_code").get(0);
                break;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }
}