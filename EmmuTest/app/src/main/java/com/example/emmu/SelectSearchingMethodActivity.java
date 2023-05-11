package com.example.emmu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SelectSearchingMethodActivity extends AppCompatActivity {

    private Button findMusicByMoodsButton;
    private Button findMusicByPhotoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_searching_method);

        findMusicByMoodsButton = findViewById(R.id.find_music_by_moods_button);
        findMusicByPhotoButton = findViewById(R.id.find_music_by_photo_button);

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
                // Обработка нажатия на кнопку "Поиск музыки по фото"
            }
        });
    }
}