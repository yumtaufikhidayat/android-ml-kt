package com.taufik.androidmachinelearning.barcodescanning.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.view.CameraController.COORDINATE_SYSTEM_VIEW_REFERENCED
import androidx.camera.view.LifecycleCameraController
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.taufik.androidmachinelearning.databinding.ActivityCameraBarcodeScanningBinding
import com.taufik.androidmachinelearning.onlineimageclassification.ext.Ext.showToast

class CameraBarcodeScanningActivity : AppCompatActivity() {

    private val binding by lazy { ActivityCameraBarcodeScanningBinding.inflate(layoutInflater) }
    private lateinit var barcodeScanner: BarcodeScanner
    private var firstCall = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    public override fun onResume() {
        super.onResume()
        hideSystemUI()
        startCamera()
    }

    private fun startCamera() {
        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()
        barcodeScanner = BarcodeScanning.getClient(options)

        val analyzer = MlKitAnalyzer(
            listOf(barcodeScanner),
            COORDINATE_SYSTEM_VIEW_REFERENCED,
            ContextCompat.getMainExecutor(this)
        ) { result: MlKitAnalyzer.Result ->
            showResult(result)
        }

        val cameraController = LifecycleCameraController(baseContext)
        cameraController.setImageAnalysisAnalyzer(
            ContextCompat.getMainExecutor(this),
            analyzer
        )
        cameraController.bindToLifecycle(this)
        binding.viewFinder.controller = cameraController
    }

    private fun showResult(result: MlKitAnalyzer.Result?) {
        if (firstCall) {
            val barcodeResults = result?.getValue(barcodeScanner)
            if ((barcodeResults != null) &&
                (barcodeResults.size != 0) &&
                (barcodeResults.first() != null)
            ) {
                firstCall = false
                val barcode = barcodeResults[0]
                val alertDialog = AlertDialog.Builder(this)
                    .setMessage(barcode.rawValue)
                    .setPositiveButton("Buka") { _, _ ->
                        firstCall = false
                        when (barcode.valueType) {
                            Barcode.TYPE_URL -> {
                                val openBrowserIntent = Intent(Intent.ACTION_VIEW)
                                openBrowserIntent.data = Uri.parse(barcode.url?.url)
                                startActivity(openBrowserIntent)
                            }
                            else -> {
                                showToast("Unsupported data type")
                                startCamera()
                            }
                        }
                    }
                    .setNegativeButton("Scan lagi") { _, _ ->
                        firstCall = true
                    }
                    .setCancelable(false)
                    .create()
                alertDialog.show()
            }
        }
    }

    private fun hideSystemUI() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    companion object {
        const val EXTRA_CAMERAX_IMAGE = "CameraX Image"
    }
}