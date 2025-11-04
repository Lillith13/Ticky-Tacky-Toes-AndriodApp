package com.example.tickytackytoes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.FileNotFoundException;
import java.io.PrintStream;


public class PlayerSetup extends AppCompatActivity {

    public static String[] playerNames = new String[2];

    public int selectedXColor;
    public int selectedOColor;

    private static int difficulty = 3; // default 0 == easy
    public void setDifficulty(int diff) {
        // Update your logic class
        this.difficulty = diff;

        // Update your button text instantly
        Button difficultyButton = findViewById(R.id.chooseDifficulty);

        String label;
        switch (diff) {
            case 0:
                label = "Easy";
                break;
            case 1:
                label = "Medium";
                break;
            case 2:
                label = "Hard";
                break;
            default: // 3
                label = "Choose Difficulty";
        }

        difficultyButton.setText(String.format("CPU: %s", label));
    }

    public static int getDifficulty() {
        return difficulty;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.player_setup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // set playerName defaults
        playerNames[0] = "Player 1";
        if (MainActivity.getPlayerCount() == 1) {
            playerNames[1] = "CPU";

            TextInputLayout p2nInputContainer = findViewById(R.id.p2n);
            TextView p2nInput = findViewById(R.id.p2n_Input);

            p2nInputContainer.setHint(playerNames[1]);
            p2nInput.setText(playerNames[1]);

            setDifficulty(3); // default difficulty set to easy
            System.out.println(getDifficulty());

        } else {
            playerNames[1] = "Player 2";
            // make difficulty selector button invisible if not single player game
            Button diffSelButt = findViewById(R.id.chooseDifficulty);
            diffSelButt.setVisibility(View.GONE);
        }
    }


    public void customizeMarker(View view) {
        ///! Customize marker button onClick function
        ColorPickerFragment dialog = new ColorPickerFragment();
        int id = view.getId();

        if (id == R.id.custP1marker) {
            dialog.setColorPickerListener(color -> {
                selectedXColor = color;
                view.setBackgroundColor(getColor(color));
            });
            dialog.show(getSupportFragmentManager(), "XColorPicker");

        } else if (id == R.id.custP2marker) {
            dialog.setColorPickerListener(color -> {
                selectedOColor = color;
                view.setBackgroundColor(getColor(color));
            });
            dialog.show(getSupportFragmentManager(), "OColorPicker");
        }
    }

    /// if single player => choose difficulty (default == easy)
    // onClick listener for difficulty selection
    public void difficultySelect(View view) {
        DifficultySelectorFragment dialog = new DifficultySelectorFragment();
        dialog.setDifficultySelectorListener(this::setDifficulty);
        dialog.show(getSupportFragmentManager(), "Choose Difficulty - Default = Easy");
    }

    // define GameBoard Intent function
    public void startGame(View view) {
        // set Intent (transfer from playerSetup view to GameBoard view
        Intent playGame = new Intent(this, GameBoard.class);

        // only pull names if names entered
        TextView p1Name = findViewById(R.id.p1n_Input);
        TextView p2Name = findViewById(R.id.p2n_Input);
        if (!p1Name.getText().toString().isEmpty()) playerNames[0] = p1Name.getText().toString();
        if (!p2Name.getText().toString().isEmpty() && MainActivity.getPlayerCount() == 1) playerNames[1] = "CPU: " + p2Name.getText().toString();
        else if (!p2Name.getText().toString().isEmpty()) playerNames[1] = p2Name.getText().toString();

        playGame.putExtra("XColor", selectedXColor);
        playGame.putExtra("OColor", selectedOColor);

        startActivity(playGame);
    }

}
