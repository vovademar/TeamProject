package com.example.emmu.CameraActivities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.media.MediaActionSound;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.emmu.CameraUtils.CameraPreview;
import com.example.emmu.CameraUtils.Permission;
import com.example.emmu.R;
import com.example.emmu.ml.Model;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.TransformToGrayscaleOp;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;

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
                    String mood = getMood(picture_file);

                    TextView textView = findViewById(R.id.camera_message_mood);
                    textView.setText(mood);
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
        final String[] emotions = new String[] {"angry", "disgust", "fear", "happy", "sad", "surprise", "neutral"};

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
        }
        catch (Exception e) {
            Log.e("Error", e.getMessage());
        }

        return emotion;
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
