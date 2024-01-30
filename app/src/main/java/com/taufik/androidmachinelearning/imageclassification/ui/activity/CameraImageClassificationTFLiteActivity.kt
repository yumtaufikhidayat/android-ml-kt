package com.taufik.androidmachinelearning.imageclassification.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.taufik.androidmachinelearning.databinding.ActivityCameraImageClassificationTfliteBinding

class CameraImageClassificationTFLiteActivity : AppCompatActivity() {

    private val binding by lazy { ActivityCameraImageClassificationTfliteBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}