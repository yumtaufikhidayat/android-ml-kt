package com.taufik.androidmachinelearning.image_classification_object_detection.ui.activity

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.core.resolutionselector.AspectRatioStrategy
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.taufik.androidmachinelearning.databinding.ActivityCameraImageClassificationObjectDetectionBinding
import com.taufik.androidmachinelearning.image_classification_object_detection.helper.ObjectDetectorHelper
import com.taufik.androidmachinelearning.onlineimageclassification.ext.Ext.showToast
import com.taufik.androidmachinelearning.utils.Constants
import org.tensorflow.lite.task.gms.vision.detector.Detection
import java.util.concurrent.Executors

class CameraImageClassificationObjectDetectionActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityCameraImageClassificationObjectDetectionBinding.inflate(layoutInflater)
    }
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private lateinit var objectDetectorHelper: ObjectDetectorHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    public override fun onResume() {
        super.onResume()
        hideSystemUI()
        startCamera()
    }

    private fun startCamera() {
        objectDetectorHelper = ObjectDetectorHelper(
            context = this,
            detectorListener = object : ObjectDetectorHelper.DetectorListener {
                override fun onError(error: String) {
                    runOnUiThread {
                        showToast(error)
                    }
                }

                override fun onResults(
                    results: MutableList<Detection>?,
                    inferenceTime: Long,
                    imageHeight: Int,
                    imageWidth: Int
                ) {
                    runOnUiThread {
                        results?.let {
                            if (it.isNotEmpty() && it[0].categories.isNotEmpty()) {
                                println(it)
                                binding.overlay.setResults(
                                    results,
                                    imageHeight,
                                    imageWidth
                                )
                                binding.tvInferenceTime.text = "$inferenceTime ms"
                            } else {
                                binding.overlay.clear()
                                binding.tvInferenceTime.text = ""
                            }
                        }

                        // Force a redraw
                        binding.overlay.invalidate()
                    }
                }
            }
        )

        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val resolutionSelector = ResolutionSelector.Builder()
                .setAspectRatioStrategy(AspectRatioStrategy.RATIO_16_9_FALLBACK_AUTO_STRATEGY)
                .build()
            val imageAnalyzer = ImageAnalysis.Builder().setResolutionSelector(resolutionSelector)
                .setTargetRotation(binding.viewFinder.display.rotation)
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888).build().also {
                    it.setAnalyzer(Executors.newSingleThreadExecutor()) { image ->
                        objectDetectorHelper.detectObject(image)
                    }
                }

            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
            }
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalyzer)
            } catch (exc: Exception) {
                showToast("Gagal memunculkan kamera.")
                Log.e(Constants.TAG_CAMERA_ACTIVITY, "startCamera: ${exc.message}")
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun hideSystemUI() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    companion object {
        const val EXTRA_CAMERAX_IMAGE = "CameraX Image"
        const val CAMERAX_RESULT = 200
    }
}