package org.cryptonews.main.ui.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.cryptonews.main.ui.activities.MyApp;
import org.cryptonews.main.R;
import org.cryptonews.main.ui.activities.MainActivity;

import java.util.Locale;


public class SettingsFragment extends Fragment {

    private RadioGroup group, language;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_settings,container,false);
        group = (RadioGroup) root.findViewById(R.id.settings_group);
        SharedPreferences preferences = getContext().getSharedPreferences(MyApp.prefs, Context.MODE_PRIVATE);
        ((RadioButton)group.getChildAt(preferences.getInt(MyApp.theme,0))).setChecked(true);
        group.setOnCheckedChangeListener((radioGroup, i) -> {
            int ind = 0;
            if(i==R.id.light_theme) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            } else {
                ind = 1;
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
            preferences.edit().putInt(MyApp.theme,ind).commit();
        });
        language = (RadioGroup) root.findViewById(R.id.language_group);
        ((RadioButton)language.getChildAt(preferences.getString(MyApp.language,"ru").equals("ru")==true ? 0 : 1)).setChecked(true);
        language.setOnCheckedChangeListener((radioGroup, i) -> {
            Configuration configuration = getContext().getResources().getConfiguration();
            if(i==R.id.russian) {
                configuration.setLocale(new Locale("ru"));
                preferences.edit().putString(MyApp.language,"ru").commit();
            } else {
                configuration.setLocale(new Locale("en"));
                preferences.edit().putString(MyApp.language,"en").commit();
            }
            getActivity().finish();
            Intent intent = new Intent(getContext(),MainActivity.class);
            getContext().startActivity(intent);
        });
        return root;
    }
}