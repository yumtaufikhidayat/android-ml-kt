package com.taufik.androidmachinelearning.textrecognition.ui.activity

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.taufik.androidmachinelearning.R
import com.taufik.androidmachinelearning.databinding.ActivityResultTextRecognitionBinding

class ResultTextRecognitionActivity : AppCompatActivity() {

    private val binding by lazy { ActivityResultTextRecognitionBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_text_recognition)

        val imageUri = Uri.parse(intent.getStringExtra(EXTRA_IMAGE_URI))
        imageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.resultImage.setImageURI(it)
        }
    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_RESULT = "extra_result"
    }
}