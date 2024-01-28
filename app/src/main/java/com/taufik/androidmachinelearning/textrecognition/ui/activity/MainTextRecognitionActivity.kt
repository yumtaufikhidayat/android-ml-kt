package com.taufik.androidmachinelearning.textrecognition.ui.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.isVisible
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.taufik.androidmachinelearning.R
import com.taufik.androidmachinelearning.databinding.ActivityMainTextRecognitionBinding
import com.taufik.androidmachinelearning.onlineimageclassification.ext.Ext.showToast
import com.taufik.androidmachinelearning.utils.Constants.CAMERAX_RESULT
import com.taufik.androidmachinelearning.utils.Constants.EXTRA_CAMERAX_IMAGE
import com.taufik.androidmachinelearning.utils.Constants.REQUIRED_PERMISSION
import com.taufik.androidmachinelearning.utils.getImageUri

class MainTextRecognitionActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainTextRecognitionBinding.inflate(layoutInflater) }
    private var currentImageUri: Uri? = null

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                showToast("Permission request granted")
            } else {
                showToast("Permission request denied")
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERAX_RESULT) {
            currentImageUri = it.data?.getStringExtra(EXTRA_CAMERAX_IMAGE)?.toUri()
            showImage()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.cameraButton.setOnClickListener { startCamera() }
        binding.cameraXButton.setOnClickListener { startCameraX() }
        binding.analyzeButton.setOnClickListener {
            currentImageUri?.let {
                analyzeImage(it)
            } ?: run {
                showToast(getString(R.string.empty_image_warning))
            }
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraTextRecognitionActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun analyzeImage(uri: Uri) {
        showLoading(true)
        val textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        val inputImage = InputImage.fromFilePath(this, uri)
        textRecognizer.process(inputImage)
            .addOnSuccessListener { visionText ->
                val detectedText = visionText.text
                if (detectedText.isBlank()) {
                    showLoading(false)
                    showToast(getString(R.string.no_text_recognized))
                } else {
                    showLoading(false)
                    val intent = Intent(this, ResultTextRecognitionActivity::class.java).apply {
                        putExtra(ResultTextRecognitionActivity.EXTRA_IMAGE_URI, uri.toString())
                        putExtra(ResultTextRecognitionActivity.EXTRA_RESULT, detectedText)
                    }
                    startActivity(intent)
                }
            }.addOnFailureListener {
                showLoading(false)
                showToast(it.message.toString())
            }
    }

    private fun showLoading(isShow: Boolean) = binding.progressIndicator.isVisible == isShow
}