package com.example.tickytackytoes;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class GameBoard extends AppCompatActivity {



    private TicTacToeBoard tictactoeboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        
        setContentView(R.layout.activity_game_board);

        Button playAgainBTN = findViewById(R.id.playAgainButton);
        Button homeBTN = findViewById(R.id.homeButton);
        TextView playerTurn = findViewById(R.id.playerDisplay);

        if (PlayerSetup.playerNames != null) {
            playerTurn.setText((String.format("%s's Turn", PlayerSetup.playerNames[0])));
        }

        playAgainBTN.setVisibility(View.GONE);
        homeBTN.setVisibility(View.GONE);

        tictactoeboard = findViewById(R.id.ticTacToeBoard);

        int xColor = getIntent().getIntExtra("XColor", Color.GRAY);
        int oColor= getIntent().getIntExtra("OColor", Color.DKGRAY);

        if(xColor != 0) {
            tictactoeboard.setXColor(getColor(xColor));
        } else {
            tictactoeboard.setXColor(Color.GRAY);
        }
        if(oColor != 0) {
            tictactoeboard.setOColor(getColor(oColor));
        } else {
            tictactoeboard.setOColor(Color.GRAY);
        }

        tictactoeboard.setBoardColor((getResources().getConfiguration().uiMode &
                Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES ? Color.LTGRAY : Color.DKGRAY);


        tictactoeboard.setUpGame(playAgainBTN, homeBTN, playerTurn);


    }

    public void playAgainButtonClick(View view) {
        tictactoeboard.resetGame();
        tictactoeboard.invalidate();
    };

    public void homeButtonClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    };
}