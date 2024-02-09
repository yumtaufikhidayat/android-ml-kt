package com.taufik.androidmachinelearning.smartreply_mlkit.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.taufik.androidmachinelearning.databinding.ActivityMainSmartReplyBinding

class MainSmartReplyActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainSmartReplyBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}