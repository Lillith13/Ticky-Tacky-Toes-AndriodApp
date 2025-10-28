package com.example.tickytackytoes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {

            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            return insets;

        });
    }

    public void playerSetupLaunch() {
        Intent playerSetup = new Intent(this, PlayerSetup.class);
        startActivity(playerSetup);
    }

    // onClick for both buttons, each button will pass either 1 or 2 to the playerSetup class
    public static int playerCount = 1; // default player count == 1
    public static int getPlayerCount() {
        // this function will allow me to access player count from other views
        return playerCount;
    }
    public void setPlayerCount(int res) {
        playerCount = res;
    }
    public void playerCountSelected(View view) {
        if (view.getId() == R.id.one) {
            setPlayerCount(1);
        } else {
            setPlayerCount(2);
        }
        playerSetupLaunch();
    }

}