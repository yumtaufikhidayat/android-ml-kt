package com.taufik.androidmachinelearning.image_classification_prediction.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.taufik.androidmachinelearning.databinding.ActivityMainImageClassificationPredictionBinding
import com.taufik.androidmachinelearning.image_classification_prediction.helper.PredictionHelper
import com.taufik.androidmachinelearning.onlineimageclassification.ext.Ext.showToast

class MainImageClassificationPredictionActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainImageClassificationPredictionBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        predictRice()
    }

    private fun predictRice() {
        val predictionHelper = PredictionHelper(
            context = this,
            onResult = { result ->
                binding.tvResult.text = result
            },
            onError = { errorMessage ->
                showToast(errorMessage)
            }
        )
        binding.btnPredict.setOnClickListener {
            val input = binding.edSales.text.toString()
            predictionHelper.predict(input)
        }
    }
}