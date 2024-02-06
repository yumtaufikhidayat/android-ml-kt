package com.taufik.androidmachinelearning.audioclassification.ui.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.mediapipe.tasks.components.containers.Classifications
import com.taufik.androidmachinelearning.audioclassification.helper.AudioClassifierHelper
import com.taufik.androidmachinelearning.databinding.ActivityMainAudioClassificationBinding
import com.taufik.androidmachinelearning.onlineimageclassification.ext.Ext.showToast
import java.text.NumberFormat

class MainAudioClassificationActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainAudioClassificationBinding.inflate(layoutInflater)
    }
    private lateinit var audioClassifierHelper: AudioClassifierHelper
    private var isRecording = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initializeAudioClassifierHelper()
        setClickListener()
        updateButtonStates()
        requestPermissionsIfNeeded()
    }

    private fun initializeAudioClassifierHelper() {
        audioClassifierHelper = AudioClassifierHelper(
            context = this,
            classifierListener = object : AudioClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
                    showToast(error)
                }

                override fun onResults(results: List<Classifications>, inferenceTime: Long) {
                    runOnUiThread {
                        results.let { it ->
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
    }

    private fun setClickListener() {
        binding.btnStart.setOnClickListener {
            audioClassifierHelper.startAudioClassification()
            isRecording = true
            updateButtonStates()
        }
        binding.btnStop.setOnClickListener {
            audioClassifierHelper.stopAudioClassification()
            isRecording = false
            updateButtonStates()
        }
    }

    private fun updateButtonStates() {
        binding.btnStart.isEnabled = !isRecording
        binding.btnStop.isEnabled = isRecording
    }


    override fun onResume() {
        super.onResume()
        if (isRecording) {
            audioClassifierHelper.startAudioClassification()
        }
    }

    override fun onPause() {
        super.onPause()
        if (::audioClassifierHelper.isInitialized) {
            audioClassifierHelper.stopAudioClassification()
        }
    }


    private fun requestPermissionsIfNeeded() {
        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }
    }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            val message = if (isGranted) "Permission granted" else "Permission denied"
            showToast(message)
        }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.RECORD_AUDIO
    }
}