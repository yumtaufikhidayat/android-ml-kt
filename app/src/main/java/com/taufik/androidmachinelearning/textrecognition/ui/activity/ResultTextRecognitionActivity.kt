package com.taufik.androidmachinelearning.textrecognition.ui.activity

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.taufik.androidmachinelearning.databinding.ActivityResultTextRecognitionBinding
import com.taufik.androidmachinelearning.onlineimageclassification.ext.Ext.showToast

class ResultTextRecognitionActivity : AppCompatActivity() {

    private val binding by lazy { ActivityResultTextRecognitionBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        getData()
    }

    private fun getData() {
        val imageUri = Uri.parse(intent.getStringExtra(EXTRA_IMAGE_URI))
        val detectedText = intent.getStringExtra(EXTRA_RESULT)
        showToast("imageUri: $imageUri\ntext: $detectedText", Toast.LENGTH_LONG)

        binding.apply {
            imageUri?.let {
                Log.d("Image URI", "showImage: $it")
                resultImage.setImageURI(it)
            }
            resultText.text = detectedText.toString()
        }
    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_RESULT = "extra_result"
    }
}