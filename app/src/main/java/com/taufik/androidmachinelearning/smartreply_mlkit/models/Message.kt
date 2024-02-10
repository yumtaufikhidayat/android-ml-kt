package com.taufik.androidmachinelearning.smartreply_mlkit.models

data class Message(
    val text: String,
    val isLocalUser: Boolean,
    val timestamp: Long
)
