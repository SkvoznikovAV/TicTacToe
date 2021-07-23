package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        findViewById(R.id.btn_pvp).setOnClickListener(v -> {
            startGameActivity(false);
        });

        findViewById(R.id.btn_pvai).setOnClickListener(v -> {
            startGameActivity(true);
        });
    }

    private void startGameActivity(boolean ai) {
        Intent intent = new Intent(StartActivity.this, MainActivity.class);
        intent.putExtra(MainActivity.KEY_VERSUS_AI, ai);
        startActivity(intent);
    }
}