package org.cryptonews.main.ui.dialogs;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import org.cryptonews.main.ui.activities.MyApp;
import org.cryptonews.main.R;
import org.cryptonews.main.ui.DialogReference;

public class PercentDialog extends DialogFragment {

    private DialogReference reference;

    public PercentDialog(DialogReference reference) {
        this.reference = reference;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View title = LayoutInflater.from(getContext()).inflate(R.layout.percent_dialog_title,null);
        title.findViewById(R.id.percent_close).setOnClickListener((view -> dismiss()));
        View view = LayoutInflater.from(getContext()).inflate(R.layout.percent_dialog_view,null);
        SharedPreferences preferences = getContext().getSharedPreferences(MyApp.prefs, Context.MODE_PRIVATE);
        ((RadioButton)((RadioGroup)view.findViewById(R.id.percent_group)).getChildAt(preferences.getInt(MyApp.changes,MyApp.week))).setChecked(true);
        ((RadioGroup)view.findViewById(R.id.percent_group)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int ind = 0;
                for(int j = 0;j<radioGroup.getChildCount();j++) if(radioGroup.getChildAt(j).getId()==i) ind = j;
                preferences.edit().putInt(MyApp.changes,ind).commit();
                reference.selectSort(null,0,0,false);
            }
        });
        builder.setView(view);
        builder.setCustomTitle(title);
        return builder.create();
    }
}
