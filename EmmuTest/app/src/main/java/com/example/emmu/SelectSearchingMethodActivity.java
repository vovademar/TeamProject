package com.example.emmu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.emmu.CameraActivities.CameraActivity;

public class SelectSearchingMethodActivity extends AppCompatActivity {

    private RelativeLayout findMusicByMoodsButton;
    private RelativeLayout findMusicByPhotoButton;
    private RelativeLayout enterAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_searching_method);

        findMusicByMoodsButton = findViewById(R.id.find_music_by_moods_button);
        findMusicByPhotoButton = findViewById(R.id.find_music_by_photo_button);
        enterAccountButton = findViewById(R.id.enter_to_account_button);

        findMusicByMoodsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectSearchingMethodActivity.this, MoodActivity.class);
//                intent.putExtra("mood", " LIKE '%/storage/emulated/0/Emmu/Sad/%'");
                startActivity(intent);
                // Обработка нажатия на кнопку "Поиск музыки по настроению"
            }
        });

        findMusicByPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectSearchingMethodActivity.this, CameraActivity.class);
                startActivity(intent);
            }
        });

        enterAccountButton.setOnClickListener(v -> {
            Intent intent = new Intent(SelectSearchingMethodActivity.this, AuthorizationActivity.class);
            startActivity(intent);
        });
    }
}