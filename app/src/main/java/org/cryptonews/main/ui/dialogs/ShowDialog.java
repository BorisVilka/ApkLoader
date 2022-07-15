package org.cryptonews.main.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import org.cryptonews.main.ui.activities.MyApp;
import org.cryptonews.main.R;

public class ShowDialog extends DialogFragment {


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.show_dialog_view,null,false);
        view.findViewById(R.id.button).setOnClickListener((v)->{
            boolean b = !((CheckBox)view.findViewById(R.id.checkBox)).isChecked();
            getContext().getSharedPreferences(MyApp.prefs, Context.MODE_PRIVATE).edit().putBoolean(MyApp.dialog,b).commit();
            dismiss();
        });
        builder.setCancelable(true);
        builder.setView(view);
        return builder.create();
    }
}
