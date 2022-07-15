package org.cryptonews.main;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
import org.cryptonews.main.network.API1;
import org.cryptonews.main.network.Graph;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleEmitter;
import io.reactivex.rxjava3.core.SingleOnSubscribe;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SomeActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_some);
        LocalDateTime dateTime = LocalDateTime.now();
        Log.d("TAG",dateTime.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond()+" "+dateTime.minusDays(1).atZone(ZoneId.systemDefault()).toInstant().getEpochSecond());
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.coingecko.com/")
                .client(MyApp.getClient().getClient1())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        API1 api = retrofit.create(API1.class);
        Call<Graph> graphCall = api.getGraph("bitcoin","usd",
                String.format("%d",dateTime.minusDays(1).atZone(ZoneId.systemDefault()).toInstant().getEpochSecond()),
                String.format("%d",dateTime.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond()));
        Single<List<List<String>>> simple = Single.create(new SingleOnSubscribe<List<List<String>>>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<List<List<String>>> emitter) throws Throwable {
                emitter.onSuccess(graphCall.execute().body().list);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        simple.subscribe(lists -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            List<Entry> data = new ArrayList<>();
            for(int i = 0;i<lists.size();i++){
                data.add(new Entry(Long.parseLong(lists.get(i).get(0))/1_000_000,
                        (float)Double.parseDouble(lists.get(i).get(1))));
            }
            ValueFormatter formatter1 = new ValueFormatter() {
                @Override
                public String getAxisLabel(float value, AxisBase axis) {
                    return Utils.getDate(value,3);
                }
            };
            LineDataSet dataSet = new LineDataSet(data,"label");
            dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            dataSet.setColor(Color.GREEN);
            dataSet.setDrawValues(false);
            dataSet.setDrawCircles(false);
            dataSet.setHighlightEnabled(true);
            dataSet.setDrawVerticalHighlightIndicator(true);
            dataSet.setDrawHorizontalHighlightIndicator(true);
            dataSet.setLineWidth(2f);
            dataSet.setCubicIntensity(0.1f);
            dataSet.setHighLightColor(getResources().getColor(R.color.purple_500,getTheme()));
            dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
            dataSet.setValueTextSize(10);
            dataSet.setValueTextColor(Color.GREEN);
            dataSet.setDrawFilled(true);
            dataSet.setFillDrawable(getDrawable(R.drawable.graph_plus));
            LineChart chart = (LineChart) findViewById(R.id.line);
            LineData lineData = new LineData(dataSet);
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
            chart.setMaxHighlightDistance(300);
            chart.setDrawMarkers(true);
            chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, Highlight h) {
                    TextPaint paint = new TextPaint();
                    paint.setAntiAlias(true);
                    paint.setTextSize(14*getResources().getDisplayMetrics().density);
                    findViewById(R.id.date_graph).setX(
                            h.getXPx()<paint.measureText(Utils.getDate(e.getX(),3)) ? 0
                                    :h.getXPx()-paint.measureText(Utils.getDate(e.getX(),3)
                    ));
                    findViewById(R.id.date_graph).setY(chart.getPivotY()*1.8f);
                    findViewById(R.id.price_graph).setX(chart.getAxisLeft().getXOffset());
                    findViewById(R.id.price_graph).setY(h.getYPx());
                    ((TextView)findViewById(R.id.price_graph)).setText(String.format("$%.2f",e.getY()));
                    ((TextView)findViewById(R.id.date_graph)).setText(Utils.getDate(e.getX(),3));
                    ((TextView)findViewById(R.id.textView22)).setText(String.format("$%.2f",e.getY())+" ");
                    findViewById(R.id.date_graph).setVisibility(View.VISIBLE);
                    findViewById(R.id.price_graph).setVisibility(View.VISIBLE);
                }

                @Override
                public void onNothingSelected() {
                    findViewById(R.id.date_graph).setVisibility(View.INVISIBLE);
                    findViewById(R.id.price_graph).setVisibility(View.INVISIBLE);
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
            chart.getAxisLeft().setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
            chart.getAxisLeft().setGranularityEnabled(true);
            chart.getAxisLeft().setXOffset(0);
            chart.getAxisLeft().setDrawZeroLine(false);
            chart.getAxisRight().setEnabled(false);
            chart.getLegend().setEnabled(false);
            chart.getDescription().setEnabled(false);
            chart.invalidate();
        });
    }

}