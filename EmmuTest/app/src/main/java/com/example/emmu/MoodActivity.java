package com.example.emmu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.File;
import java.io.FileOutputStream;

import cz.msebera.android.httpclient.Header;

public class MoodActivity extends AppCompatActivity {
    private AsyncHttpClient asyncHttpClient;

    private AsyncHttpClient asyncHttpClient1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mood_selection);

        RelativeLayout sadButton = findViewById(R.id.sad_button);
        RelativeLayout happyButton = findViewById(R.id.happy_button);
        RelativeLayout angryButton = findViewById(R.id.angry_button);
        RelativeLayout disgustButton = findViewById(R.id.disgust_button);
        RelativeLayout fearButton = findViewById(R.id.fear_button);
        RelativeLayout neutralButton = findViewById(R.id.neutral_button);
        RelativeLayout surprisedButton = findViewById(R.id.surprised_button);

        sadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getMusic("http://192.168.0.104:5000/play_sad_radio", "http://192.168.0.104:5000/get_sad_name","/Emmu/Sad");
                Intent intent = new Intent(MoodActivity.this, MainActivity.class);
                intent.putExtra("mood", " LIKE '%/storage/emulated/0/Emmu/Sad/%'");

                Intent intent1 = new Intent(MoodActivity.this, MainActivity.class);
                intent1.putExtra("mood1", "sad");
                startActivity(intent);
            }
        });

        happyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MoodActivity.this, MainActivity.class);
                intent.putExtra("mood", " LIKE '%/storage/emulated/0/Emmu/Happy/%'");

                Intent intent1 = new Intent(MoodActivity.this, MainActivity.class);
                intent1.putExtra("mood1", "happy");
                startActivity(intent);
            }
        });

        angryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MoodActivity.this, MainActivity.class);
                intent.putExtra("mood", " LIKE '%/storage/emulated/0/Emmu/Angry/%'");

                Intent intent1 = new Intent(MoodActivity.this, MainActivity.class);
                intent1.putExtra("mood1", "angry");
                startActivity(intent);
            }
        });

        disgustButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MoodActivity.this, MainActivity.class);
                intent.putExtra("mood", " LIKE '%/storage/emulated/0/Emmu/Disgust/%'");

                Intent intent1 = new Intent(MoodActivity.this, MainActivity.class);
                intent1.putExtra("mood1", "disgust");
                startActivity(intent);
            }
        });

        fearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MoodActivity.this, MainActivity.class);
                intent.putExtra("mood", " LIKE '%/storage/emulated/0/Emmu/Fear/%'");

                Intent intent1 = new Intent(MoodActivity.this, MainActivity.class);
                intent1.putExtra("mood1", "fear");
                startActivity(intent);
            }
        });

        neutralButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MoodActivity.this, MainActivity.class);
                intent.putExtra("mood", " LIKE '%/storage/emulated/0/Emmu/Neutral/%'");

                Intent intent1 = new Intent(MoodActivity.this, MainActivity.class);
                intent1.putExtra("mood1", "neutral");
                startActivity(intent);
            }
        });

        surprisedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MoodActivity.this, MainActivity.class);
                intent.putExtra("mood", " LIKE '%/storage/emulated/0/Emmu/Surprised/%'");

                Intent intent1 = new Intent(MoodActivity.this, MainActivity.class);
                intent1.putExtra("mood1", "surprised");
                startActivity(intent);
            }
        });
    }

    private void getMusic(String mus, String name, String path) {
        asyncHttpClient1 = new AsyncHttpClient();
        asyncHttpClient = new AsyncHttpClient();
//        String url = "http://192.168.0.104:5000/play_sad_radio";

//        asyncHttpClient.get("http://192.168.0.104:5000/play_sad_radio", new AsyncHttpResponseHandler() {
        asyncHttpClient.get(mus, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    // сохраняем полученный файл на устройстве
                    String fileName = "sad_music.mp3";
                    File dir = new File(Environment.getExternalStorageDirectory() + path);//"/Emmu/Sad"
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    File file = new File(dir, fileName);
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(responseBody);
                    fos.close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("MoodActivity", "Failed to download sad music");
            }
        });
    }

}