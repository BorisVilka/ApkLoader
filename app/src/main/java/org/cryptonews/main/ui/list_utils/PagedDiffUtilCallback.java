package org.cryptonews.main.ui.list_utils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

public class PagedDiffUtilCallback extends DiffUtil.ItemCallback<ListItem> {

    @Override
    public boolean areItemsTheSame(@NonNull ListItem oldItem, @NonNull ListItem newItem) {
        return oldItem.getCoin().getId()==newItem.getCoin().getId();
    }

    @Override
    public boolean areContentsTheSame(@NonNull ListItem oldItem, @NonNull ListItem newItem) {
        return oldItem.getCoin().getId()==newItem.getCoin().getId();
    }
}
