package org.cryptonews.main.ui.list_utils.holders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.cryptonews.main.R;
import org.cryptonews.main.Utils;

import java.util.Comparator;

public class FireHolder extends RecyclerView.ViewHolder implements Comparator<FireHolder> {

    public TextView name, text, date;

    public FireHolder(@NonNull View itemView) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.name_post);
        text = (TextView) itemView.findViewById(R.id.text_post);
        date = (TextView) itemView.findViewById(R.id.date);
    }


    @Override
    public int compare(FireHolder fireHolder, FireHolder t1) {
        return -1*(Utils.convertDate(t1.date.getText().toString())-Utils.convertDate(fireHolder.date.getText().toString()));
    }
}