package com.taufik.androidmachinelearning.onlineimageclassification.ext

import android.content.Context
import android.widget.Toast

object Ext {
    fun Context.showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}