package com.example.emmu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    private AsyncHttpClient asyncHttpClient;
    private AsyncHttpClient asyncHttpClient1;
    RecyclerView recyclerView;
    TextView noMusicTextView;
    ArrayList<AudioModel> songsList = new ArrayList<>();
    Button loadButton;
    Button refreshButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadButton = findViewById(R.id.loadButton);
        recyclerView = findViewById(R.id.recycler_view);
        noMusicTextView = findViewById(R.id.no_songs_text);
        refreshButton = findViewById(R.id.refreshButton);
        TextView myTextView = findViewById(R.id.songs_text);
//        Button refreshButton = findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });
        String mood1 = getIntent().getStringExtra("mood");
        Log.e("mmod", mood1);

        String input = mood1.substring(1, mood1.length() - 1);
        String[] parts = input.split("/");
        String result = parts[parts.length - 2];
        result.toLowerCase();
        Log.e("mmod", result);
        myTextView.setText(result + " SONgs");
        loadButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (result.equals("Sad")) {
                    getMusic("http://192.168.43.69:5000/play_sad_radio", "http://192.168.43.69:5000/get_sad_name", "/Emmu/Sad");
                }
                if (result.equals("Happy")) {
                    getMusic("http://192.168.43.69:5000/play_happy_radio", "http://192.168.43.69:5000/get_happy_name", "/Emmu/Happy");
                }
                if (result.equals("Angry")) {
                    getMusic("http://192.168.43.69:5000/play_angry_radio", "http://192.168.43.69:5000/get_angry_name", "/Emmu/Angry");
                }
                if (result.equals("Disgust")) {
                    getMusic("http://192.168.43.69:5000/play_disgust_radio", "http://192.168.43.69:5000/get_disgust_name", "/Emmu/Disgust");
                }
                if (result.equals("Fear")) {
                    getMusic("http://192.168.43.69:5000/play_fear_radio", "http://192.168.43.69:5000/get_fear_name", "/Emmu/Fear");
                }
                if (result.equals("Neutral")) {
                    getMusic("http://192.168.43.69:5000/play_neutral_radio", "http://192.168.43.69:5000/get_neutral_name", "/Emmu/Neutral");
                }
                if (result.equals("Surprised")) {
                    getMusic("http://192.168.43.69:5000/play_surprise_radio", "http://192.168.43.69:5000/get_surprise_name", "/Emmu/Surprised");
                }
            }
        });

        if (checkPermission() == false) {
            requestPermission();
            return;
        }

        if (checkPermissionWr() == false) {
            requestPermissionWr();
            return;
        }

        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION
        };
        String mood = getIntent().getStringExtra("mood");
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0 AND " + MediaStore.Audio.Media.DATA + mood;

        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, null, null);
        while (cursor.moveToNext()) {
            AudioModel songData = new AudioModel(cursor.getString(1), cursor.getString(0), cursor.getString(2));
            if (new File(songData.getPath()).exists())
                songsList.add(songData);
        }

        if (songsList.size() == 0) {
            noMusicTextView.setVisibility(View.VISIBLE);
        } else {
            //recyclerview
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new MusicListAdapter(songsList, getApplicationContext()));
        }

    }

    boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    boolean checkPermissionWr() {
        int result = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(MainActivity.this, "READ PERMISSION IS REQUIRED,PLEASE ALLOW FROM SETTTINGS", Toast.LENGTH_SHORT).show();
        } else
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
    }

    void requestPermissionWr() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(MainActivity.this, "READ PERMISSION IS REQUIRED,PLEASE ALLOW FROM SETTTINGS", Toast.LENGTH_SHORT).show();
        } else
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
    }

    //    @Override
//    protected void onResume() {
//        super.onResume();
//        if(checkPermission() == false){
//            requestPermission();
//            return;
//        }
//
//        String[] projection = {
//                MediaStore.Audio.Media.TITLE,
//                MediaStore.Audio.Media.DATA,
//                MediaStore.Audio.Media.DURATION
//        };
//        String mood = getIntent().getStringExtra("mood");
//        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0 AND " + MediaStore.Audio.Media.DATA + mood;
//
//        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,projection,selection,null,null);
//        ArrayList<AudioModel> songsList = new ArrayList<>();
//        while(cursor.moveToNext()){
//            AudioModel songData = new AudioModel(cursor.getString(1),cursor.getString(0),cursor.getString(2));
//            if(new File(songData.getPath()).exists())
//                songsList.add(songData);
//        }
//
//        if(songsList.size()==0){
//            noMusicTextView.setVisibility(View.VISIBLE);
//        }else{
//            noMusicTextView.setVisibility(View.GONE);
//            recyclerView.setLayoutManager(new LinearLayoutManager(this));
//            recyclerView.setAdapter(new MusicListAdapter(songsList,getApplicationContext()));
//        }
//    }


//    private void getSadMusic() {
//        asyncHttpClient = new AsyncHttpClient();
////        String url = "http://192.168.43.69:5000/play_sad_radio";
//        asyncHttpClient.get("http://192.168.43.69:5000/play_sad_radio", new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                try {
//                    // сохраняем полученный файл на устройстве
//                    String fileName = "sadmujjh.mp3";
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

    public static void markAsMusicFile(Context context, String filePath) {
        MediaScannerConnection.scanFile(
                context,
                new String[]{filePath},
                new String[]{"audio/mp3"},
                new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                        // File scan completed
                        // You can perform additional actions here if needed
                    }
                });
    }

    private void getMusic(String mus, String name, String path) {
        asyncHttpClient1 = new AsyncHttpClient();
        asyncHttpClient = new AsyncHttpClient();
        final String[] fileName = new String[1];


        asyncHttpClient1.get(name, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    fileName[0] = (new String(responseBody, StandardCharsets.UTF_8)) + ".mp3";
                    Log.e("TrackName", new String(responseBody, StandardCharsets.UTF_8));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("MoodActivity", "Failed to get track name");
            }
        });

        asyncHttpClient.get(mus, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    // сохраняем полученный файл на устройстве

                    File dir = new File(Environment.getExternalStorageDirectory() + path);//"/Emmu/Sad"
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    File file = new File(dir, fileName[0]);
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(responseBody);
                    fos.close();
                    Context context = getApplicationContext(); // Obtain the Context instance
                    markAsMusicFile(context, file.getPath());
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


    @Override
    protected void onResume() {
        super.onResume();
        if (recyclerView != null) {
            recyclerView.setAdapter(new MusicListAdapter(songsList, getApplicationContext()));
        }
    }
}