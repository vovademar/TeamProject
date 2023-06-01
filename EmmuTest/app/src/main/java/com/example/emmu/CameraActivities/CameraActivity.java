package com.example.emmu.CameraActivities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.media.MediaActionSound;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.emmu.CameraUtils.CameraPreview;
import com.example.emmu.CameraUtils.Permission;
import com.example.emmu.MainActivity;
import com.example.emmu.R;
import com.example.emmu.ml.Model;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.TransformToGrayscaleOp;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class CameraActivity extends AppCompatActivity {
    MediaActionSound sound = new MediaActionSound();
    private FrameLayout frameLayout;

    private Camera mCamera;

    Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File picture_file = getOutputMediaFile();
            try {
                sound.play(MediaActionSound.SHUTTER_CLICK);
                FileOutputStream fos = new FileOutputStream(picture_file);
                fos.write(data);
                fos.close();

                if (picture_file != null) {
                    String imageBase64 = getBase64FromPath(picture_file.getAbsolutePath());
                    String mood = getMoodFromServer("http://192.168.43.69:5000/analyze", "data:image/jpeg;base64," + imageBase64);

//                    String mood = getMood(picture_file);
//                    TextView textView = findViewById(R.id.camera_message_mood);
//                    textView.setText(mood);
                }

                mCamera.startPreview();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private CameraPreview cameraPreview;

    private int currentCameraId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.camera_activity);
        frameLayout = findViewById(R.id.frame_layout);
        Permission.checkAndRequestPermissions(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCamera = getCameraInstance(currentCameraId);
        cameraPreview = new CameraPreview(this, mCamera);
        frameLayout.addView(cameraPreview);
    }

    private Camera getCameraInstance(int currentCameraId) {
        Camera camera = null;
        try {
            camera = Camera.open(currentCameraId);
        } catch (Exception e) {
        }
        return camera;
    }

    private String getMood(File imageFile) throws Exception {
        final String[] emotions = new String[]{"angry", "disgust", "fear", "happy", "sad", "surprise", "neutral"};

        Bitmap imageBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        imageBitmap = Bitmap.createScaledBitmap(imageBitmap, 48, 48, true);

        TensorImage tensorImage = new TensorImage(DataType.FLOAT32);
        tensorImage.load(imageBitmap);

        TransformToGrayscaleOp transform = new TransformToGrayscaleOp();
        tensorImage = transform.apply(tensorImage);

        String emotion = "Not recognized";

        try {
            Model model = Model.newInstance(this);

            Model.Outputs outputs = model.process(tensorImage.getTensorBuffer());
            TensorBuffer outputFeature = outputs.getOutputFeature0AsTensorBuffer();

            float[] features = outputFeature.getFloatArray();

            int featureIndex = -1;
            float maxFeature = Float.MIN_VALUE;

            for (int i = 0; i < features.length; i++) {
                if (maxFeature < features[i]) {
                    maxFeature = features[i];
                    featureIndex = i;
                }
            }

            if (featureIndex < 0 || featureIndex >= 7)
                Log.e("Error", "Features index number not in 0 to 6");

            emotion = emotions[featureIndex];
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }

        return emotion;
    }

    private String getMoodFromServer(String url, String imageBase64) throws IOException, JSONException {

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.put("img_path", imageBase64);

        final String[] responseMood = new String[1];
        responseMood[0] = "Not recognized";

        AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                TextView textView = findViewById(R.id.camera_message_mood);
                String personMood = new String(responseBody, StandardCharsets.UTF_8);
                Toast.makeText(getApplicationContext(), "Your mood is " + personMood, Toast.LENGTH_LONG).show();
//                textView.setText(personMood);
                if (personMood.equals("sad")) {
                    Log.e("after ai", personMood);
                    Intent intent = new Intent(CameraActivity.this, MainActivity.class);
                    intent.putExtra("mood", " LIKE '%/storage/emulated/0/Emmu/Sad/%'");

                    Intent intent1 = new Intent(CameraActivity.this, MainActivity.class);
                    intent1.putExtra("mood1", "sad");
                    startActivity(intent);
                } else if (personMood.equals("happy")) {
                    Log.e("after ai", personMood);
                    Intent intent = new Intent(CameraActivity.this, MainActivity.class);
                    intent.putExtra("mood", " LIKE '%/storage/emulated/0/Emmu/Happy/%'");

                    Intent intent1 = new Intent(CameraActivity.this, MainActivity.class);
                    intent1.putExtra("mood1", "happy");
                    startActivity(intent);
                } else if (personMood.equals("angry")) {
                    Log.e("after ai", personMood);
                    Intent intent = new Intent(CameraActivity.this, MainActivity.class);
                    intent.putExtra("mood", " LIKE '%/storage/emulated/0/Emmu/Angry/%'");

                    Intent intent1 = new Intent(CameraActivity.this, MainActivity.class);
                    intent1.putExtra("mood1", "angry");
                    startActivity(intent);
                } else if (personMood.equals("disgust")) {
                    Log.e("after ai", personMood);
                    Intent intent = new Intent(CameraActivity.this, MainActivity.class);
                    intent.putExtra("mood", " LIKE '%/storage/emulated/0/Emmu/Disgust/%'");

                    Intent intent1 = new Intent(CameraActivity.this, MainActivity.class);
                    intent1.putExtra("mood1", "disgust");
                    startActivity(intent);
                } else if (personMood.equals("fear")) {
                    Log.e("after ai", personMood);
                    Intent intent = new Intent(CameraActivity.this, MainActivity.class);
                    intent.putExtra("mood", " LIKE '%/storage/emulated/0/Emmu/Fear/%'");

                    Intent intent1 = new Intent(CameraActivity.this, MainActivity.class);
                    intent1.putExtra("mood1", "fear");
                    startActivity(intent);
                } else if (personMood.equals("neutral")) {
                    Log.e("after ai", personMood);
                    Intent intent = new Intent(CameraActivity.this, MainActivity.class);
                    intent.putExtra("mood", " LIKE '%/storage/emulated/0/Emmu/Neutral/%'");

                    Intent intent1 = new Intent(CameraActivity.this, MainActivity.class);
                    intent1.putExtra("mood1", "neutral");
                    startActivity(intent);
                } else if (personMood.equals("surprise")) {
                    Log.e("after ai", personMood);
                    Intent intent = new Intent(CameraActivity.this, MainActivity.class);
                    intent.putExtra("mood", " LIKE '%/storage/emulated/0/Emmu/Surprised/%'");

                    Intent intent1 = new Intent(CameraActivity.this, MainActivity.class);
                    intent1.putExtra("mood1", "surprise");
                    startActivity(intent);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                responseMood[0] = "Failure";
            }
        };

        JSONObject jsonParams = new JSONObject();
        jsonParams.put("img_path", imageBase64);
        StringEntity entity = new StringEntity(jsonParams.toString());
        RequestHandle handle = asyncHttpClient.post(getApplicationContext(), url, entity, "application/json", handler);

        return responseMood[0];
    }

    public static String getBase64FromPath(String path) {
        String base64 = "";
        try {/*from w w w . ja va 2s . c om*/
            File file = new File(path);
            byte[] buffer = new byte[(int) file.length() + 100];
            int length = new FileInputStream(file).read(buffer);
            base64 = Base64.encodeToString(buffer, 0, length,
                    Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return base64;
    }

    public void rotateCamera(View view) {
        mCamera.stopPreview();
        cameraPreview.getHolder().removeCallback(cameraPreview);
        mCamera.release();

        if (currentCameraId == Camera.CameraInfo.CAMERA_FACING_BACK)
            currentCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
        else
            currentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;

        mCamera = getCameraInstance(currentCameraId);
        cameraPreview = new CameraPreview(CameraActivity.this, mCamera);
        frameLayout.removeAllViews();
        frameLayout.addView(cameraPreview);
    }

    private File getOutputMediaFile() {
        String state = Environment.getExternalStorageState();
        if (!state.equals(Environment.MEDIA_MOUNTED))
            return null;
        else {
            File folder_ui = new File(Environment.getExternalStorageDirectory() + File.separator + "Camera App");

            if (!folder_ui.exists()) {
                folder_ui.mkdirs();
            }

            File outputFile = new File(folder_ui, System.currentTimeMillis() + "_image.jpg");
            return outputFile;
        }
    }

    public void openGallery(View view) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        Uri uri = Uri.parse(Environment.getExternalStorageDirectory() + File.separator + "Camera App");
        intent.setDataAndType(uri, "images/*");
        startActivity(Intent.createChooser(intent, "Open folder"));
    }

    public void captureImage(View view) {
        if (mCamera != null) {
            mCamera.takePicture(null, null, mPictureCallback);
        }
    }
}
