package com.taufik.androidmachinelearning.imageclassification.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.taufik.androidmachinelearning.databinding.ActivityResultImageClassificationTfliteBinding

class ResultImageClassificationTFLiteActivity : AppCompatActivity() {

    private val binding by lazy { ActivityResultImageClassificationTfliteBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}