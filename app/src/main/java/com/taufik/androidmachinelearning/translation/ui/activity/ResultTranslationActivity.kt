package com.taufik.androidmachinelearning.translation.ui.activity

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.taufik.androidmachinelearning.R
import com.taufik.androidmachinelearning.databinding.ActivityResultTranslationBinding
import com.taufik.androidmachinelearning.onlineimageclassification.ext.Ext.showToast

class ResultTranslationActivity : AppCompatActivity() {

    private val binding by lazy { ActivityResultTranslationBinding.inflate(layoutInflater) }
    private var imageUri: Uri? = null
    private var detectedText = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        getData()
        onTranslateButtonOnClickListener()
    }

    private fun getData() {
        imageUri = Uri.parse(intent.getStringExtra(EXTRA_IMAGE_URI))
        detectedText = intent.getStringExtra(EXTRA_RESULT).orEmpty()

        binding.apply {
            imageUri?.let {
                Log.d("Image URI", "showImage: $it")
                resultImage.setImageURI(it)
            }
            resultText.text = detectedText
        }
    }

    private fun onTranslateButtonOnClickListener() {
        binding.translateButton.setOnClickListener {
            showLoading(true)
            translateText(detectedText)
        }
    }

    private fun translateText(detectedText: String?) {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(TranslateLanguage.INDONESIAN)
            .build()

        val indonesianEnglishTranslator = Translation.getClient(options)
        val conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()

        indonesianEnglishTranslator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                indonesianEnglishTranslator.translate(detectedText.toString())
                    .addOnSuccessListener { translatedText ->
                        binding.translatedText.text = translatedText
                        indonesianEnglishTranslator.close()
                        showLoading(false)
                    }
                    .addOnFailureListener { exception ->
                        showToast(exception.message.toString())
                        print(exception.stackTrace)
                        indonesianEnglishTranslator.close()
                        showLoading(false)
                    }
            }
            .addOnFailureListener { _ ->
                showToast(getString(R.string.downloading_model_fail))
                showLoading(false)
            }
        lifecycle.addObserver(indonesianEnglishTranslator)
    }

    private fun showLoading(isShow: Boolean) = binding.progressIndicator.isVisible == isShow

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_RESULT = "extra_result"
    }
}