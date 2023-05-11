package com.example.emmu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MoodActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mood_selection);

        Button sadButton = findViewById(R.id.sad_button);
        Button happyButton = findViewById(R.id.happy_button);

        sadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MoodActivity.this, MainActivity.class);
                intent.putExtra("mood", " LIKE '%/storage/emulated/0/Emmu/Sad/%'");
                startActivity(intent);
            }
        });

        happyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MoodActivity.this, MainActivity.class);
                intent.putExtra("mood", " LIKE '%/storage/emulated/0/Emmu/Happy/%'");
                startActivity(intent);
            }
        });
    }
}