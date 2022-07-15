package org.cryptonews.main;

import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.cryptonews.main.ui.activities.MyApp;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StrategyAdapter extends RecyclerView.Adapter<StrategyAdapter.StrategyHolder> {

    private List<Term> data;

    public StrategyAdapter() {
        data = new ArrayList<>();
        Configuration configuration = new Configuration();
        configuration.setLocale(new Locale(MyApp.getUtils().getContext().getSharedPreferences(MyApp.prefs, Context.MODE_PRIVATE).getString(MyApp.language,"ru")));
        Context context = MyApp.getUtils().getContext().createConfigurationContext(configuration);
        data.add(new Term(context.getString(R.string.arbitr_title),context.getString(R.string.arbitr)));
        data.add(new Term(context.getString(R.string.diapozon_title),context.getString(R.string.diapozon)));
        data.add(new Term(context.getString(R.string.buy_and_ud_title),context.getString(R.string.buy_and_ud)));
        data.add(new Term(context.getString(R.string.obr_torg_title),context.getString(R.string.obr_torg)));
        data.add(new Term(context.getString(R.string.scalping_title),context.getString(R.string.scalping)));
        data.add(new Term(context.getString(R.string.gold_title),context.getString(R.string.gold)));
        data.add(new Term(context.getString(R.string.sovets_title),context.getString(R.string.sovets)));
    }


    @NonNull
    @Override
    public StrategyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StrategyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.stratey_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull StrategyHolder holder, int position) {
            holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class StrategyHolder extends RecyclerView.ViewHolder {

        private TextView text,title;

        public StrategyHolder(@NonNull View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.strategy);
            title = (TextView) itemView.findViewById(R.id.title_Strat);
        }
        public void bind(Term t) {
            this.text.setText(t.text);
            this.title.setText(t.title);
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
