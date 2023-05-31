package com.example.emmu;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;


import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadFileTask{



//    private static final String TAG = "DownloadFileTask";
//    private Context mContext;
//
//    public DownloadFileTask(Context context) {
//        mContext = context;
//    }
//
//    @Override
//    protected Void doInBackground(String... urls) {
//        String url = urls[0];
//        try {
//            URL downloadUrl = new URL(url);
//            HttpURLConnection connection = (HttpURLConnection) downloadUrl.openConnection();
//            connection.setRequestMethod("GET");
//            connection.setDoOutput(true);
//            connection.connect();
//
//            // Получаем имя файла из заголовков ответа
//            String fileName = "";
//            String disposition = connection.getHeaderField("Content-Disposition");
//            if (disposition != null && disposition.indexOf("attachment") != -1) {
//                int index = disposition.indexOf("filename=");
//                if (index > 0) {
//                    fileName = disposition.substring(index + 10, disposition.length() - 1);
//                }
//            }
//
//            // Создаем директорию, если она еще не существует
//            File directory = new File("/storage/emulated/0/Emmu/Sad/");
//            if (!directory.exists()) {
//                directory.mkdirs();
//            }
//
//            // Создаем файл и записываем в него данные из ответа на GET-запрос
//            File file = new File(directory, fileName);
//            FileOutputStream outputStream = new FileOutputStream(file);
//
//            InputStream inputStream = connection.getInputStream();
//
//            byte[] buffer = new byte[1024];
//            int len;
//            while ((len = inputStream.read(buffer)) != -1) {
//                outputStream.write(buffer, 0, len);
//            }
//
//            outputStream.close();
//            inputStream.close();
//
//        } catch (Exception e) {
//            Log.e(TAG, "Error downloading file: " + e.getMessage());
//        }
//        return null;
//    }
}