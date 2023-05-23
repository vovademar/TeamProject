//package com.example.emmu
//
//import android.Manifest
//import android.content.pm.PackageManager
//import android.net.Uri
//import android.os.Bundle
//import android.util.Log
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.camera.core.CameraSelector
//import androidx.camera.core.ImageCapture
//import androidx.camera.core.ImageCaptureException
//import androidx.camera.core.Preview
//import androidx.camera.lifecycle.ProcessCameraProvider
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import org.emotionalmusic.yandexauth.databinding.CameraLayoutBinding
//import java.io.File
//import java.text.SimpleDateFormat
//import java.util.Locale
//import java.util.concurrent.ExecutorService
//import java.util.concurrent.Executors
//
//class CameraActivity : AppCompatActivity() {
//    private lateinit var cameraExecutor: ExecutorService
//    private lateinit var binding: CameraLayoutBinding
//
//    private lateinit var outputDir: File
//    private var imageCapture: ImageCapture? = null
//
//    companion object {
//        private const val TAG = "CameraX"
//        private const val FILE_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
//        private const val PERMISSION_CODE = 10
//        private val PERMISSION = arrayOf(Manifest.permission.CAMERA)
//    }
//
//    private fun getOutputDir(): File {
//        val mediaDir = externalMediaDirs.firstOrNull()?.absoluteFile.let {
//            File(it, resources.getString(R.string.app_name)).apply {
//                mkdir()
//            }
//        }
//        return if (mediaDir != null && mediaDir.exists()) mediaDir
//        else filesDir
//    }
//
//    /**
//     * Проверка пермижинов - разрешение на камеру
//     */
//    private fun allPermissionGranted() = PERMISSION.all {
//        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.camera_layout)
//
//        binding = CameraLayoutBinding.inflate(layoutInflater)
//        val view = binding.root
//        setContentView(view)
//
//        if (allPermissionGranted()) {
//            startCamera()
//        } else {
//            ActivityCompat.requestPermissions( //запрашиваем разрешение на камеру
//                this,
//                PERMISSION,
//                PERMISSION_CODE
//            )
//        }
//
//        binding.save.setOnClickListener {
//            takePhoto()
//        }
//
//        outputDir = getOutputDir()
//        cameraExecutor =
//            Executors.newSingleThreadExecutor() // обмен информации между двумя потоками
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        cameraExecutor.shutdown()
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//
//        if (requestCode == PERMISSION_CODE) {
//            if (allPermissionGranted()) {
//                startCamera()
//            } else {
//                Toast.makeText(this, "Permission error", Toast.LENGTH_SHORT).show()
//                finish() // выходим из приложения
//            }
//        }
//    }
//
//    private fun startCamera() {
//        val cameraProviderFuture =
//            ProcessCameraProvider.getInstance(this) //пытаемся получить жизненный цикл, к которому будем привязываться
//        cameraProviderFuture.addListener(
//            Runnable {
//                val cameraProvider = cameraProviderFuture.get()
//                val preview = Preview.Builder().build().also { // связываем камеру с preview
//                    it.setSurfaceProvider(binding.pvCamera.surfaceProvider)
//                }
//
//                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA // выбираем камеру
//                imageCapture = ImageCapture.Builder().build()
//                try {
//                    cameraProvider.unbindAll()
//                    cameraProvider.bindToLifecycle(
//                        this,
//                        cameraSelector,
//                        preview,
//                        imageCapture
//                    )
//                } catch (e: Exception) {
//                    Log.e(TAG, "Bind error", e)
//                }
//            },
//            ContextCompat.getMainExecutor(this)
//        )
//    }
//
//    private fun takePhoto() {
//        val imageCapture = imageCapture ?: return // проверка, есть ли что сохранять
//
//        val photoFile = File(
//            outputDir,
//            SimpleDateFormat(FILE_FORMAT, Locale.US)
//                .format(System.currentTimeMillis()) + ".jpg"
//        )
//
//        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
//
//        imageCapture.takePicture(
//            outputOptions,
//            ContextCompat.getMainExecutor(baseContext),
//            object : ImageCapture.OnImageSavedCallback { //callback после выполнения фотографии
//                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
//                    val uri = Uri.fromFile(photoFile)
//                    val msg = "Photo: $uri"
//                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
//                    Log.d(TAG, msg)
//                }
//
//                override fun onError(exception: ImageCaptureException) {
//                    Toast.makeText(
//                        baseContext,
//                        "Save error: ${exception.message}",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
//        )
//    }
//}