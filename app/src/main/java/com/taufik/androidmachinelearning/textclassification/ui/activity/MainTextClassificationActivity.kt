package com.taufik.androidmachinelearning.textclassification.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.mediapipe.tasks.components.containers.Classifications
import com.taufik.androidmachinelearning.databinding.ActivityMainTextClassificationBinding
import com.taufik.androidmachinelearning.onlineimageclassification.ext.Ext.showToast
import com.taufik.androidmachinelearning.textclassification.helper.TextClassifierHelper
import java.text.NumberFormat

class MainTextClassificationActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainTextClassificationBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val textClassifierHelper = TextClassifierHelper(
            context = this,
            classifierListener = object : TextClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
                    showToast(error)
                }

                override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
                    runOnUiThread {
                        results?.let { it ->
                            if (it.isNotEmpty() && it[0].categories().isNotEmpty()) {
                                println(it)
                                val sortedCategories =
                                    it[0].categories().sortedByDescending { it?.score() }

                                val displayResult =
                                    sortedCategories.joinToString("\n") {
                                        "${it.categoryName()} " + NumberFormat.getPercentInstance()
                                            .format(it.score()).trim()
                                    }
                                binding.tvResult.text = displayResult
                            } else {
                                binding.tvResult.text = ""
                            }
                        }
                    }
                }
            }
        )

        binding.btnClassify.setOnClickListener {
            val inputText = binding.edInput.text.toString()
            textClassifierHelper.classify(inputText)
        }
    }
}