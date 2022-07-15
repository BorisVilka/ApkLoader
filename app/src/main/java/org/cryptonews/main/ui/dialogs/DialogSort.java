package org.cryptonews.main.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import org.cryptonews.main.ui.activities.MyApp;
import org.cryptonews.main.R;
import org.cryptonews.main.ui.DialogReference;

public class DialogSort extends DialogFragment {

    private boolean callFirst;
    private DialogReference fragment;
    private int indS;

    public DialogSort(DialogReference fragment, int indS) {
        this.fragment = fragment;
        this.indS = indS;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View title = getLayoutInflater().inflate(R.layout.sort_dialog_title,null), view = getLayoutInflater().inflate(R.layout.sort_dialog_view,null);
        builder.setCancelable(true);
        view.requestFocus();
        view.requestLayout();
        callFirst = true;
        SharedPreferences preferences = getContext().getSharedPreferences(MyApp.prefs, Context.MODE_PRIVATE);
        title.findViewById(R.id.close_title).setOnClickListener((v)->dismiss());
        ((RadioButton)((RadioGroup)view.findViewById(R.id.sort_group)).getChildAt(indS)).setChecked(true);
        ((RadioGroup)view.findViewById(R.id.sort_group)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int ind = 0;
                for(int j = 0;j<radioGroup.getChildCount();j++) if(radioGroup.getChildAt(j).getId()==i) ind = j;
                Log.d("TAG",ind+"");
                fragment.selectSort(((RadioButton)radioGroup.getChildAt(ind)).getText().toString(),(ind%2),ind, true);
            }
        });
        ((RadioButton)((RadioGroup)view.findViewById(R.id.sort_group)).getChildAt(indS)).setChecked(true);
        builder.setView(view);
        builder.setCustomTitle(title);
        return builder.create();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragment = null;
    }
}
