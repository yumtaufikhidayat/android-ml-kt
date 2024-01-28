package com.taufik.androidmachinelearning.translation.ui.activity

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.taufik.androidmachinelearning.databinding.ActivityResultTranslationBinding

class ResultTranslationActivity : AppCompatActivity() {

    private val binding by lazy { ActivityResultTranslationBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        getData()
    }

    private fun getData() {
        val imageUri = Uri.parse(intent.getStringExtra(EXTRA_IMAGE_URI))
        val detectedText = intent.getStringExtra(EXTRA_RESULT)

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