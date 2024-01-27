package com.taufik.androidmachinelearning.onlineimageclassification.data.model.response


import com.google.gson.annotations.SerializedName

data class FileUploadResponse(
    @SerializedName("data")
    val data: FileUploadData = FileUploadData(),
    @SerializedName("message")
    val message: String = "" // Model is predicted successfully.
)