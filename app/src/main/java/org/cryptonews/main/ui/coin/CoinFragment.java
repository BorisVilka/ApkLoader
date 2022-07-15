package org.cryptonews.main.ui.coin;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.cryptonews.main.ui.activities.MyApp;
import org.cryptonews.main.R;
import org.cryptonews.main.Utils;
import org.cryptonews.main.databinding.FragmentCoinBinding;
import org.cryptonews.main.network.API1;
import org.cryptonews.main.network.Graph;
import org.cryptonews.main.network.MarketCoin;
import org.cryptonews.main.ui.activities.MainActivity;
import org.cryptonews.main.ui.list_utils.ListItem;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleEmitter;
import io.reactivex.rxjava3.core.SingleOnSubscribe;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class CoinFragment extends Fragment {

    private FragmentCoinBinding binding;
    private ListItem item;
    private int position;
    private List<List<String>> data;
    private Retrofit retrofit;
    private API1 api;
    private String id;
    private LineDataSet dataSet;
    private LineData lineData;
    private LineChart chart;
    private boolean scrollEnabled;
    private MarketCoin coin;
    private List<Entry> dataEntries;
    public Listener listener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        item = (ListItem) getArguments().getSerializable("Coin");
        position = getArguments().getInt("position");
        ((MainActivity)getActivity()).setTitle(item.getCoin().getName());
        LocalDateTime dateTime = LocalDateTime.now();
        Log.d("TAG",dateTime.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond()+" "+dateTime.minusDays(1).atZone(ZoneId.systemDefault()).toInstant().getEpochSecond());
        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.coingecko.com/")
                .client(MyApp.getClient().getClient1())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
       api = retrofit.create(API1.class);
        int ind = MyApp.appContext.getSharedPreferences(MyApp.prefs, Context.MODE_PRIVATE).getInt(MyApp.graph_sort,0);
       Single<String> searchSingle = Single.create(new SingleOnSubscribe<String>() {
                @Override
                public void subscribe(@NonNull SingleEmitter<String> emitter) throws Throwable {
                    //Log.d("TAG", api.getSearch(item.getCoin().getName().toLowerCase(Locale.ROOT)).execute().toString());
                    id = api.getSearch(item.getCoin().getName().toLowerCase(Locale.ROOT)).execute().body().coins.get(0).id;
                    api.getCoin(id).enqueue(new Callback<MarketCoin>() {
                        @Override
                        public void onResponse(Call<MarketCoin> call, Response<MarketCoin> response) {
                            Log.d("TAG","SUCCESS "+response.body());
                            coin = response.body();
                        }

                        @Override
                        public void onFailure(Call<MarketCoin> call, Throwable t) {
                        }
                    });
                    emitter.onSuccess(id);
                }
            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        searchSingle.subscribe(search -> {
            Call<Graph> graphCall = api.getGraph(search,"usd",
                    String.format("%d",Utils.getTime(ind).atZone(ZoneId.systemDefault()).toInstant().getEpochSecond()),
                    String.format("%d",dateTime.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond()));
            Single<List<List<String>>> simple = Single.create(new SingleOnSubscribe<List<List<String>>>() {
                @Override
                public void subscribe(@NonNull SingleEmitter<List<List<String>>> emitter) throws Throwable {
                    Graph graph = graphCall.execute().body();
                    if(graph!=null) emitter.onSuccess(graph.list);
                    else emitter.onSuccess(new ArrayList<>());
                }
            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            simple.subscribe(lists -> {
                if(getView()==null || getContext()==null) return;
                dataEntries = new ArrayList<>();
                int ind1 = MyApp.appContext.getSharedPreferences(MyApp.prefs, Context.MODE_PRIVATE).getInt(MyApp.graph_sort,0);
                for(int i = 0;i<lists.size();i++){
                    dataEntries.add(new Entry(Long.parseLong(lists.get(i).get(0))/1_000_000,
                            (float)Double.parseDouble(lists.get(i).get(1))));
                    if(ind1==0) i+=2;
                }
                ValueFormatter formatter1 = new ValueFormatter() {
                    @Override
                    public String getAxisLabel(float value, AxisBase axis) {
                        int ind = MyApp.appContext.getSharedPreferences(MyApp.prefs, Context.MODE_PRIVATE).getInt(MyApp.graph_sort,0);
                        return Utils.getDate(value,ind);
                    }
                };
                dataSet = new LineDataSet(dataEntries,"label");
                dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                dataSet.setDrawValues(false);
                dataSet.setDrawCircles(false);
                dataSet.setHighlightEnabled(true);
                dataSet.setDrawVerticalHighlightIndicator(true);
                dataSet.setDrawHorizontalHighlightIndicator(true);
                dataSet.setLineWidth(2f);
                dataSet.setCubicIntensity(0.1f);
                dataSet.setHighLightColor(getResources().getColor(R.color.purple_500,getContext().getTheme()));
                dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
                dataSet.setValueTextSize(10);
                dataSet.setValueTextColor(Color.GREEN);
                dataSet.setDrawFilled(true);
                setColors();
                chart = (LineChart) binding.line;
                lineData = new LineData(dataSet);
                lineData.setValueTextSize(10);
                lineData.setValueTextColor(Color.GREEN);
                lineData.setDrawValues(false);
                chart.setData(lineData);
                chart.getAxisLeft().setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        return String.format("$%.0f",value);
                    }
                });
                if(chart!=null) {
                    chart.setMaxHighlightDistance(300);
                chart.setDrawMarkers(true);
                chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                    @Override
                    public void onValueSelected(Entry e, Highlight h) {
                        TextPaint paint = new TextPaint();
                        paint.setAntiAlias(true);
                        paint.setTextSize(14*getResources().getDisplayMetrics().density);
                        binding.dateGraph.setX(
                                h.getXPx()<paint.measureText(Utils.getDate(e.getX(),3)) ? 0
                                        :h.getXPx()-paint.measureText(Utils.getDate(e.getX(),3)
                                ));
                        binding.dateGraph.setY(chart.getPivotY()*1.8f);
                        binding.priceGraph.setX(chart.getAxisLeft().getXOffset());
                        binding.priceGraph.setY(h.getYPx());
                        int ind = MyApp.appContext.getSharedPreferences(MyApp.prefs, Context.MODE_PRIVATE).getInt(MyApp.graph_sort,0);
                        ((TextView)binding.priceGraph).setText(String.format("$%.2f",e.getY()));
                        ((TextView)binding.dateGraph).setText(Utils.getDate(e.getX(),ind));
                        //((TextView)findViewById(R.id.textView22)).setText(String.format("$%.2f",e.getY())+" ");
                        binding.dateGraph.setVisibility(View.VISIBLE);
                        binding.priceGraph.setVisibility(View.VISIBLE);
                        binding.linear.requestDisallowInterceptTouchEvent(true);
                        //Log.d("TAG",h.getDataIndex()+" ");
                        double k = ((e.getY()/item.getCoin().getQuote().getUsd().getPrice())*100);
                        double txt = k>=100 ? k-100 : -1*(100-k);
                        //Log.d("TAG",k+" "+txt);
                        binding.percentChange1h.setText((txt>0 ? "+" : "-")+ String.format("%.2f",Math.abs(txt))+"%");
                        binding.percentChange1h.setTextColor(getResources().getColor(txt>0 ? R.color.green : R.color.red,getContext().getTheme()));
                        binding.firstPrice.setText("$"+String.format("%.2f",e.getY()));
                        listener.enable();
                    }

                    @Override
                    public void onNothingSelected() {
                        binding.dateGraph.setVisibility(View.INVISIBLE);
                        binding.priceGraph.setVisibility(View.INVISIBLE);
                        binding.linear.requestDisallowInterceptTouchEvent(false);
                        double txt = getChanges();
                        binding.percentChange1h.setText((txt>0 ? "+" : "-")+ String.format("%.2f",Math.abs(txt))+"%");
                        binding.percentChange1h.setTextColor(txt>0 ? Color.GREEN : Color.RED);
                        binding.firstPrice.setText("$"+String.format("%.2f",item.getCoin().getQuote().getUsd().getPrice()));
                        listener.disable();
                    }
                });
                chart.getXAxis().setLabelCount(5,true);
                chart.getXAxis().setXOffset(0);
                chart.getXAxis().setCenterAxisLabels(true);
                chart.getXAxis().setValueFormatter(formatter1);
                chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                chart.getXAxis().setDrawLabels(true);
                chart.getXAxis().setGranularityEnabled(true);
                chart.getXAxis().setEnabled(true);
                chart.getAxisLeft().setTextColor(MyApp.appContext.getSharedPreferences(MyApp.prefs,Context.MODE_PRIVATE)
                .getInt(MyApp.theme,0)==0 ? Color.BLACK : Color.WHITE);
                chart.getXAxis().setTextColor(MyApp.appContext.getSharedPreferences(MyApp.prefs,Context.MODE_PRIVATE)
                        .getInt(MyApp.theme,0)==0 ? Color.BLACK : Color.WHITE);
                chart.getAxisLeft().setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
                chart.getAxisLeft().setGranularityEnabled(true);
                chart.getAxisLeft().setXOffset(0);
                chart.getAxisLeft().setDrawZeroLine(false);
                chart.getAxisRight().setEnabled(false);
                chart.getLegend().setEnabled(false);
                chart.getDescription().setEnabled(false);
                chart.animateX(1000);
                }
                //chart.invalidate();
            });
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCoinBinding.inflate(inflater);
        binding.setItem(item);
        //Picasso.get().load(item.getInfo().getLogo()).resize(150,150).into(binding.logo);
        int ind = MyApp.appContext.getSharedPreferences(MyApp.prefs, Context.MODE_PRIVATE).getInt(MyApp.graph_sort,0);
        ((RadioButton)binding.group.getChildAt(ind)).setChecked(true);
        binding.group.setOnCheckedChangeListener((radioGroup, i) -> {
            //binding.line.clear();
            int k = 0;
            for(int j = 0;j<binding.group.getChildCount();j++) if(((RadioButton)binding.group.getChildAt(j)).isChecked()) k = j;
            Log.d("TAG",k+" ||");
            MyApp.appContext.getSharedPreferences(MyApp.prefs,Context.MODE_PRIVATE).edit().putInt(MyApp.graph_sort,k).apply();
            double txt = getChanges();
            binding.percentChange1h.setText((txt>0 ? "+" : "-")+ String.format("%.2f",Math.abs(txt))+"%");
            binding.percentChange1h.setTextColor(getResources().getColor(txt>0 ? R.color.green : R.color.red,getContext().getTheme()));
            invalidate(k);
        });
        binding.line.requestFocus();
        //binding.scroll.setOnTouchListener((view, motionEvent) -> scrollEnabled);
         return binding.getRoot();
    }

    public static CoinFragment getInstance(Bundle args) {
        CoinFragment fragment = new CoinFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void invalidate(int ind) {
        onAttach(MyApp.appContext);
        if(id==null) return;
        Call<Graph> graphCall = api.getGraph(id,"usd",
                String.format("%d",Utils.getTime(ind).atZone(ZoneId.systemDefault()).toInstant().getEpochSecond()),
                String.format("%d",LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().getEpochSecond()));
        Single<List<List<String>>> simple = Single.create(new SingleOnSubscribe<List<List<String>>>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<List<List<String>>> emitter) throws Throwable {
                Graph graph = graphCall.execute().body();
                if(graph!=null) emitter.onSuccess(graph.list);
                else emitter.onSuccess(new ArrayList<>());
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        simple.subscribe(new Consumer<List<List<String>>>() {
            @Override
            public void accept(List<List<String>> lists) throws Throwable {
                if(getView()==null) return;
                int ind1 = MyApp.appContext.getSharedPreferences(MyApp.prefs, Context.MODE_PRIVATE).getInt(MyApp.graph_sort,0);
                dataEntries = new ArrayList<>();
                for(int i = 0;i<lists.size();i++){
                    dataEntries.add(new Entry(Long.parseLong(lists.get(i).get(0))/1_000_000,
                            (float)Double.parseDouble(lists.get(i).get(1))));
                    if(ind1==0) i+=2;
                }
                dataSet = new LineDataSet(dataEntries,"label");
                dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                dataSet.setDrawValues(false);
                dataSet.setDrawCircles(false);
                dataSet.setHighlightEnabled(true);
                dataSet.setDrawVerticalHighlightIndicator(true);
                dataSet.setDrawHorizontalHighlightIndicator(true);
                dataSet.setLineWidth(2f);
                dataSet.setCubicIntensity(0.1f);
                dataSet.setHighLightColor(getResources().getColor(R.color.purple_500,getContext().getTheme()));
                dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
                dataSet.setValueTextSize(10);
                dataSet.setValueTextColor(Color.GREEN);
                dataSet.setDrawFilled(true);
                setColors();
                lineData = new LineData(dataSet);
                lineData.setValueTextSize(10);
                lineData.setValueTextColor(Color.GREEN);
                lineData.setDrawValues(false);
                if(chart!=null) {
                    chart.setData(lineData);
                    chart.animateX(1000);
                }
            }
        });
    }

    private void setColors() {
        double value = getChanges();
        if(value>0) {
            dataSet.setFillDrawable(getContext().getDrawable(R.drawable.graph_plus));
            dataSet.setColor(Color.GREEN);
        } else {
            dataSet.setFillDrawable(getContext().getDrawable(R.drawable.graph_minus));
            dataSet.setColor(Color.RED);
        }
    }

    private double getPrev(double x) {
        int ind = 0;
        for(int i = 0;i< dataEntries.size();i++) {
            if(Double.compare(x,dataEntries.get(i).getX())==0) {
                ind = i;
                break;
            }
        }
        return ind==0 ? x : dataEntries.get(ind-1).getX();
    }
    private double getChanges() {
        int ind = MyApp.appContext.getSharedPreferences(MyApp.prefs, Context.MODE_PRIVATE).getInt(MyApp.graph_sort,0);
        double value = 0;
        switch (ind) {
            case 0:
                value = item.getCoin().getQuote().getUsd().getPercent_change24h();
                break;
            case 1:
                value = item.getCoin().getQuote().getUsd().getPercent_change_7d();
                break;
            case 2:
                value = coin==null ? item.getCoin().getQuote().getUsd().getPercent_change_7d() : coin.data.d30;
                break;
            case 3:
                value = coin==null ? item.getCoin().getQuote().getUsd().getPercent_change_7d() : coin.data.y1;
                break;
        }
        return value;
    }
    public interface Listener {
        void enable();
        void disable();
    }
}