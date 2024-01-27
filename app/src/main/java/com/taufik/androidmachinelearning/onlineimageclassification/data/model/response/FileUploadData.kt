package com.taufik.androidmachinelearning.onlineimageclassification.data.model.response


import com.google.gson.annotations.SerializedName

data class FileUploadData(
    @SerializedName("confidenceScore")
    val confidenceScore: Double = 0.0, // 99.67641830444336
    @SerializedName("createdAt")
    val createdAt: String = "", // 2023-11-29T09:53:39.710Z
    @SerializedName("id")
    val id: String = "", // 942yU_BabbkfIXdJ
    @SerializedName("isAboveThreshold")
    val isAboveThreshold: Boolean = false, // true
    @SerializedName("result")
    val result: String = "" // Vascular lesion
)