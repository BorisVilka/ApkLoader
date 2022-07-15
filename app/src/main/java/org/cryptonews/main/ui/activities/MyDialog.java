package org.cryptonews.main.ui.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.squareup.picasso.Picasso;

import org.cryptonews.main.databinding.StartDialogBinding;

public class MyDialog extends DialogFragment {

    private AlertObject alert;
    private View.OnClickListener listener;

    public MyDialog(AlertObject alert, View.OnClickListener listener) {
        this.alert = alert;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        StartDialogBinding binding = StartDialogBinding.inflate(getLayoutInflater());
        Picasso.get().load(Uri.parse(alert.image)).into(binding.fbLogo);
        binding.fbButton.setOnClickListener(listener);
        binding.fbDesc.setText(alert.description);
        binding.fbButton.setText(alert.button);
        binding.fbTitle.setText(alert.title);
        builder.setView(binding.getRoot());
        return builder.create();
    }
}
