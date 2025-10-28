package com.example.tickytackytoes;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import androidx.appcompat.app.AlertDialog;

public class DifficultySelectorFragment extends DialogFragment {

    public interface DifficultySelectorListener {
        void onDifficultySelection(int difficulty);
    }

    private DifficultySelectorListener listener;

    public void setDifficultySelectorListener(DifficultySelectorListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String[] difficultyNames = {"Easy", "Medium", "Hard"};
        final int[] difficultyInts = {0, 1, 2};

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Choose Difficulty - Default = Easy").setItems(difficultyNames, (dialog, which) -> {
            if(listener != null) {
                listener.onDifficultySelection(difficultyInts[which]);
            }
        });

        return builder.create();
    }
}