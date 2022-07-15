package org.cryptonews.main.ui.list_utils.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;

import com.squareup.picasso.Picasso;

import org.cryptonews.main.Utils;
import org.cryptonews.main.ui.list_utils.ListItem;
import org.cryptonews.main.R;
import org.cryptonews.main.databinding.ListItemBinding;

public class PagedAdapter extends PagedListAdapter<ListItem, PagedAdapter.PagedViewHolder> {

    private ClickListener listener;
    private CheckListener checkListener;

    public PagedAdapter(@NonNull DiffUtil.ItemCallback<ListItem> diffCallback, ClickListener listener, CheckListener checkListener) {
        super(diffCallback);
        this.listener = listener;
        this.checkListener = checkListener;
    }
    public interface ClickListener {
        public void click(ListItem item, int position);
    }
    public interface CheckListener {
        public void click(boolean checked,ListItem item, int position);
    }
    @NonNull
    @Override
    public PagedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new PagedViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull PagedViewHolder holder, int position) {
        holder.bind(getItem(position), position);
        holder.itemView.setOnClickListener((v)-> listener.click(getItem(position),position));
        holder.binding.checkFavorite.setOnCheckedChangeListener((compoundButton, b) ->
                checkListener.click(b,getItem(holder.getAdapterPosition()),position));
    }

    public static class PagedViewHolder extends Adapter.ViewHolder {

        private ListItemBinding binding;

        public PagedViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ListItemBinding.bind(itemView);
        }
        public void bind(ListItem item, int pos) {
            Log.d("TAG",item.getCoin().getSlug());
            if(item.getInfo()!=null) Picasso.get().load(item.getInfo().getLogo()).resize(70,70).into(binding.icon);
            binding.setCoin(item.getCoin());
            binding.setPosition(pos);
            binding.checkFavorite.setChecked(Utils.containsFavorites(item));
        }
    }


}
