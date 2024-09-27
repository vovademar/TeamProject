package com.example.emmu;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mood_selection);

        RelativeLayout sadButton = findViewById(R.id.sad_button);
        RelativeLayout happyButton = findViewById(R.id.happy_button);

        sadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getSadMusic();
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

//    private void getSadMusic(){
//        asyncHttpClient = new AsyncHttpClient();
////        String url = "http://192.168.0.104:5000/play_sad_radio";
//        asyncHttpClient.get("http://192.168.0.104:5000/play_sad_radio", new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                try {
//                    // сохраняем полученный файл на устройстве
//                    String fileName = "sad_music.mp3";
//                    File dir = new File(Environment.getExternalStorageDirectory() + "/Emmu/Sad");
//                    if (!dir.exists()) {
//                        dir.mkdirs();
//                    }
//                    File file = new File(dir, fileName);
//                    FileOutputStream fos = new FileOutputStream(file);
//                    fos.write(responseBody);
//                    fos.close();
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                Log.e("MoodActivity", "Failed to download sad music");
//            }
//        });
//    }

}