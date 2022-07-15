package org.cryptonews.main.ui.list_utils.adapters;


import android.content.Context;
import android.widget.SimpleAdapter;

import org.cryptonews.main.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TagsAdapterHelper {

    private TagAdapter adapter;

    public TagsAdapterHelper(Context context,List<String> data) {
        List<Map<String,String>> d = new ArrayList<>();
        for(String i:data) {
            Map<String,String> m = new HashMap<>();
            m.put("txt",i);
            d.add(m);
        }
        adapter = new TagAdapter(context,d, R.layout.item_tag,new String[]{"txt"},new int[]{R.id.txt});
    }

    public TagAdapter getAdapter() {
        return adapter;
    }

    public class TagAdapter extends SimpleAdapter {

        public TagAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
        }
    }
}
