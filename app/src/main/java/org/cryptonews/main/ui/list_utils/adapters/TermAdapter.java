package org.cryptonews.main.ui.list_utils.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.cryptonews.main.ui.activities.MyApp;
import org.cryptonews.main.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TermAdapter extends RecyclerView.Adapter<TermAdapter.TermHolder> {

    List<Term> data;

    public TermAdapter() {
        data = new ArrayList<>();
        Configuration configuration = new Configuration();
        configuration.setLocale(new Locale(MyApp.getUtils().getContext().getSharedPreferences(MyApp.prefs,Context.MODE_PRIVATE).getString(MyApp.language,"ru")));
        Context context = MyApp.getUtils().getContext().createConfigurationContext(configuration);
        data.add(new Term(context.getString(R.string.altcoin_title),context.getString(R.string.altcoin)));
        data.add(new Term(context.getString(R.string.bitcoin_title),context.getString(R.string.bitcoin)));
        data.add(new Term(context.getString(R.string.blockchain_title),context.getString(R.string.blockchain)));
        data.add(new Term(context.getString(R.string.bulls_title),context.getString(R.string.bulls)));
        data.add(new Term(context.getString(R.string.flats_title),context.getString(R.string.flats)));
        data.add(new Term(context.getString(R.string.gap_title),context.getString(R.string.gap)));
        data.add(new Term(context.getString(R.string.dump_title),context.getString(R.string.dump)));
        data.add(new Term(context.getString(R.string.green_title),context.getString(R.string.green)));
        data.add(new Term(context.getString(R.string.inside_title),context.getString(R.string.inside)));
        data.add(new Term(context.getString(R.string.cryptowallet_title),context.getString(R.string.cryptowallet)));
        data.add(new Term(context.getString(R.string.kits_title),context.getString(R.string.kits)));
        data.add(new Term(context.getString(R.string.red_title),context.getString(R.string.red)));
        data.add(new Term(context.getString(R.string.bears_title),context.getString(R.string.bears)));
        data.add(new Term(context.getString(R.string.otskok_title),context.getString(R.string.otskok)));
        data.add(new Term(context.getString(R.string.pump_title),context.getString(R.string.pump)));
        data.add(new Term(context.getString(R.string.hand_title),context.getString(R.string.hand)));
        data.add(new Term(context.getString(R.string.sliv_title),context.getString(R.string.sliv)));
        data.add(new Term(context.getString(R.string.toTheMoon_title),context.getString(R.string.toTheMoon)));
        data.add(new Term(context.getString(R.string.shitcoin_title),context.getString(R.string.shitcoin)));
    }


    @NonNull
    @Override
    public TermHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TermHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.term_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull TermHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class TermHolder extends RecyclerView.ViewHolder {

        private TextView title, text;

        public TermHolder(@NonNull View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title_term);
            //text = (TextView) itemView.findViewById(R.id.term);
        }
        public void bind(Term pair) {
            title.setText(pair.title+" - "+pair.text);
        }
    }
    private static class Term {
        String title, text;
        public Term(String title, String text) {
            this.title = title;
            this.text = text;
        }

    }
}
