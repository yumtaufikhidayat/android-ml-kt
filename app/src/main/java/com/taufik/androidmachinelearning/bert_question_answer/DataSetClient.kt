package com.taufik.androidmachinelearning.bert_question_answer

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.taufik.androidmachinelearning.bert_question_answer.models.Topics
import java.io.IOException
import java.io.InputStream

class DataSetClient(private val context: Context) {

    private companion object {
        private const val TAG = "BertQAApp"
        private const val JSON_DIR = "topics.json"
    }

    fun loadJsonData(): Topics? {
        var dataSet: Topics? = null
        try {
            val inputStream: InputStream = context.assets.open(JSON_DIR)
            val bufferReader = inputStream.bufferedReader()
            val stringJson: String = bufferReader.use { it.readText() }
            val datasetType = object : TypeToken<Topics>() {}.type
            dataSet = Gson().fromJson(stringJson, datasetType)
        } catch (e: IOException) {
            Log.e(TAG, e.message.toString())
        }
        return dataSet
    }
}