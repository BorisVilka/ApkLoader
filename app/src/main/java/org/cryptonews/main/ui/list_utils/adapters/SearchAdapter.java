package org.cryptonews.main.ui.list_utils.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.cryptonews.main.R;
import org.cryptonews.main.Utils;
import org.cryptonews.main.databinding.ListItemBinding;
import org.cryptonews.main.ui.list_utils.ListItem;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private List<ListItem> data;
    private ClickListener listener;
    private CheckListener checkListener;

    public SearchAdapter(ClickListener listener, CheckListener checkListener) {
        this.listener = listener;
        this.checkListener = checkListener;
        data = new ArrayList<>();
    }
    public void setData(List<ListItem> data) {
        this.data = data;
        notifyDataSetChanged();
    }
    public interface ClickListener {
       void click(ListItem item, int position);
    }
    public interface CheckListener {
        void click(boolean checked,ListItem item, int position);
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        holder.bind(data.get(position), position);
        holder.itemView.setOnClickListener((v)-> listener.click(data.get(position),position));
        holder.binding.checkFavorite.setOnCheckedChangeListener((compoundButton, b) ->
                checkListener.click(b,data.get(position),position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class SearchViewHolder extends Adapter.ViewHolder {

        private ListItemBinding binding;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ListItemBinding.bind(itemView);
        }
        public void bind(ListItem item, int pos) {
            Log.d("TAG",item.getCoin().getSlug());
            Picasso.get().load(item.getInfo().getLogo()).resize(70,70).into(binding.icon);
            binding.setCoin(item.getCoin());
            binding.setPosition(pos);
            binding.checkFavorite.setChecked(Utils.containsFavorites(item));
        }
    }
}
