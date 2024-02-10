package com.taufik.androidmachinelearning.smartreply_mlkit.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.taufik.androidmachinelearning.R
import com.taufik.androidmachinelearning.databinding.ActivityMainSmartReplyBinding
import com.taufik.androidmachinelearning.smartreply_mlkit.ui.fragment.ChatFragment

class MainSmartReplyActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainSmartReplyBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val fragmentManager = supportFragmentManager
        val chatFragment = ChatFragment()
        val fragment = fragmentManager.findFragmentByTag(ChatFragment::class.java.simpleName)

        if (fragment !is ChatFragment) {
            Log.d("MyChatFragment", "Fragment Name :" + ChatFragment::class.java.simpleName)
            fragmentManager
                .beginTransaction()
                .add(R.id.container, chatFragment, ChatFragment::class.java.simpleName)
                .commit()
        }
    }
}