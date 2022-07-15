package org.cryptonews.main.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.MultiDexApplication;

import com.yandex.metrica.YandexMetrica;
import com.yandex.metrica.YandexMetricaConfig;

import org.cryptonews.main.Utils;
import org.cryptonews.main.network.Client;

import java.util.Locale;

public class MyApp extends MultiDexApplication {

    public final static String prefs = "PREFS",
            type_sort = "TYPE",
            order_sort = "ORDER",
            checked_index = "CHECKED_INDEX",
            marketInfo = "MARKET_INFO",
            type_sort_fav = "TYPE_FAV",
            order_sort_fav = "ORDER_FAV",
            checked_index_fav = "CHECKED_INDEX_FAV",
            marketInfo_fav = "MARKET_INFO_FAV",
            theme="THEME",
            favorites="FAVORITES",
            language = "LANGUAGE",
            dialog = "DIALOG",
            graph_sort = "GRAPH",
            changes = "CHANGES";
    private final String API_key = "2a7bf394-1c45-47c5-be82-1384a03636f9";
    public final static int hour = 0, day = 1, week = 2;
    public static int count;

    private static Client client;
    private static Utils utils;
    public static Context appContext;

    @Override
    public void onCreate() {
        SharedPreferences preferences = getSharedPreferences(prefs,MODE_PRIVATE);
        AppCompatDelegate.setDefaultNightMode(preferences.getInt(theme,0)==0 ?
                AppCompatDelegate.MODE_NIGHT_NO : AppCompatDelegate.MODE_NIGHT_YES);
        super.onCreate();
        //startService(new Intent(this,MyService.class));
        YandexMetricaConfig config = YandexMetricaConfig.newConfigBuilder(API_key).build();
        YandexMetrica.activate(getApplicationContext(), config);
        YandexMetrica.enableActivityAutoTracking(this);
        appContext = getApplicationContext();
        count = 0;
        client = new Client();
        utils = new Utils(getApplicationContext());
        Log.d("TAG",getResources().getConfiguration().getLocales().get(0).getDisplayLanguage());
    }
    public static Utils getUtils() {return utils;}
    public static Client getClient() {
        return client;
    }
}
