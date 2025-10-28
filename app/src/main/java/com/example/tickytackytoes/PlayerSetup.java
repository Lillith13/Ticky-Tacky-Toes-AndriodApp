package com.example.tickytackytoes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;



public class PlayerSetup extends AppCompatActivity {

    public static String[] playerNames = new String[2];

    public int selectedXColor;
    public int selectedOColor;

    public int difficulty = 0; // default 0 == easy
    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }
    public int getDifficulty() {
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

        } else {
            playerNames[1] = "Player 2";

            /// make difficulty selector button invisible if not single player game

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
        /// this function will work the same way that the colorPicker function does, opening a fragment/model for selection
        // setDifficulty(buttonID);
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
