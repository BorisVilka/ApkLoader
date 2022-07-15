package org.cryptonews.main.ui.list_utils.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.cryptonews.main.ui.list_utils.Post;
import org.cryptonews.main.R;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostHolder> {

    private List<Post> data;

    public PostAdapter(List<Post> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PostHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
       holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class PostHolder extends RecyclerView.ViewHolder {

        public TextView name, text, date;
        private CheckBox box;
        private int lines;

        public PostHolder(@NonNull View itemView) {
            super(itemView);
            lines = -1;
            name = (TextView) itemView.findViewById(R.id.name_post);
            text = (TextView) itemView.findViewById(R.id.text_post);
            date = (TextView) itemView.findViewById(R.id.date);
            box = (CheckBox) itemView.findViewById(R.id.visible_status);
            itemView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                   if(lines==-1) {
                       lines = text.getLineCount();
                       if (lines > 1) {
                           text.setLines(1);
                       } else {
                           box.setVisibility(View.INVISIBLE);
                       }
                   }
                }
            });
            box.setOnCheckedChangeListener((compoundButton, b) -> {
                text.setLines(b==true ? lines : 1);
            });
        }

        public void bind(Post p) {

            name.setText(p.getName());
            text.setText(p.getText());
            date.setText(p.getDate());
        }
    }
}
