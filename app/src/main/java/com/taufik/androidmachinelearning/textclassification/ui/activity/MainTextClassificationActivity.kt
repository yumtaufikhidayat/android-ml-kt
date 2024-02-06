package com.taufik.androidmachinelearning.textclassification.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.taufik.androidmachinelearning.databinding.ActivityMainTextClassificationBinding

class MainTextClassificationActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainTextClassificationBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}