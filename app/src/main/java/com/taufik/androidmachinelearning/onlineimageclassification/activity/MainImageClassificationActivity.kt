package com.taufik.androidmachinelearning.onlineimageclassification.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.taufik.androidmachinelearning.databinding.ActivityMainImageClassificationBinding

class MainImageClassificationActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainImageClassificationBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}