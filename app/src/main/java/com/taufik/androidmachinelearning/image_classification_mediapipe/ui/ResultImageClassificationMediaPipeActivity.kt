package com.taufik.androidmachinelearning.image_classification_mediapipe.ui

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.taufik.androidmachinelearning.databinding.ActivityResultImageClassificationMediaPipeBinding

class ResultImageClassificationMediaPipeActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityResultImageClassificationMediaPipeBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        getData()
    }

    private fun getData() {
        val imageUri = Uri.parse(intent.getStringExtra(EXTRA_IMAGE_URI))
        imageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.resultImage.setImageURI(it)
        }
    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
    }
}