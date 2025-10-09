package com.example.tickytackytoes;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import androidx.appcompat.app.AlertDialog;

public class ColorPickerFragment extends DialogFragment {

    // Callback interface to return the selected color
    public interface ColorPickerListener {
        void onColorSelected(int color);
    }

    private ColorPickerListener listener;

    // Setter for the callback
    public void setColorPickerListener(ColorPickerListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Color options (names and their int values)
        final String[] colorNames = {"Black", "Blue", "Teal", "Dark Gray", "Orange", "Pink", "Dark Purple"};
        final int[] colors = { R.color.black, R.color.blue, R.color.teal_700, R.color.gray_dark, R.color.orange_dark, R.color.purple, R.color.purple_dark};

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Pick a Color")
                .setItems(colorNames, (dialog, which) -> {
                    if (listener != null) {
                        listener.onColorSelected(colors[which]);
                    }
                });

        return builder.create();
    }
}
