package com.taufik.androidmachinelearning.smartreply_mlkit.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.taufik.androidmachinelearning.R
import com.taufik.androidmachinelearning.databinding.FragmentChatBinding
import com.taufik.androidmachinelearning.onlineimageclassification.ext.Ext.showToast
import com.taufik.androidmachinelearning.smartreply_mlkit.models.Message
import com.taufik.androidmachinelearning.smartreply_mlkit.ui.adapter.ChatHistoryAdapter
import com.taufik.androidmachinelearning.smartreply_mlkit.ui.adapter.ReplyOptionsAdapter
import com.taufik.androidmachinelearning.smartreply_mlkit.ui.viewmodel.ChatViewModel
import java.util.Calendar


class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private val chatViewModel by viewModels<ChatViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).setSupportActionBar(binding.topAppBar as Toolbar)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Set up recycler view untuk chat history
        val historyLayoutManager = LinearLayoutManager(context)
        binding.rvChatHistory.layoutManager = historyLayoutManager

        val chatAdapter = ChatHistoryAdapter()
        binding.rvChatHistory.adapter = chatAdapter

        // Set up recycler view untuk opsi smart reply
        val optionsLayoutManager = LinearLayoutManager(context)
        optionsLayoutManager.orientation = RecyclerView.HORIZONTAL
        binding.rvSmartReplyOptions.layoutManager = optionsLayoutManager

        val replyOptionsAdapter = ReplyOptionsAdapter(object : ReplyOptionsAdapter.OnItemClickCallback {
            override fun onOptionClicked(optionText: String) {
                binding.tietInputTextEditText.setText(optionText)
            }
        })
        binding.rvSmartReplyOptions.adapter = replyOptionsAdapter

        if (chatViewModel.chatHistory.value == null){
            val chatHistory = ArrayList<Message>()
            chatHistory.add(Message(
                "Hello friend. How are you today?",
                false,
                System.currentTimeMillis())
            )
            chatViewModel.setMessages(chatHistory)
        }

        chatViewModel.chatHistory.observe(viewLifecycleOwner) { messages ->
            chatAdapter.setChatHistory(messages)
            if (chatAdapter.itemCount > 0) {
                binding.rvChatHistory.smoothScrollToPosition(chatAdapter.itemCount - 1)
            }
        }

        chatViewModel.smartReplyOptions.observe(viewLifecycleOwner) { options ->
            replyOptionsAdapter.setReplyOptions(options)
        }

        chatViewModel.pretendingAsAnotherUser.observe(viewLifecycleOwner) { isPretendingAsAnotherUser ->
            if (isPretendingAsAnotherUser) {
                binding.tvCurrentUser.text = requireContext().getText(R.string.chatting_as_evans)
                binding.tvCurrentUser.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.red
                    )
                )
            } else {
                binding.tvCurrentUser.text = requireContext().getText(R.string.chatting_as_kai)
                binding.tvCurrentUser.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.blue
                    )
                )
            }
        }

        chatViewModel.errorMessage.observe(viewLifecycleOwner) {
            if (it != null) requireContext().showToast(it)
        }

        binding.rvChatHistory.setOnTouchListener { v, _ ->
            val imm = requireContext().getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as InputMethodManager
            imm.hideSoftInputFromWindow(v.windowToken, 0)
            false
        }

        binding.rvSmartReplyOptions.setOnClickListener { v ->
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(v.windowToken, 0)
        }

        binding.btnSwitchUser.setOnClickListener {
            chatAdapter.pretendingAsAnotherUser = !chatAdapter.pretendingAsAnotherUser
            chatViewModel.switchUser()
        }

        binding.btnSend.setOnClickListener {
            val input = binding.tietInputTextEditText.text.toString()
            if (input.isNotEmpty()) {
                val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(it.windowToken, 0)
            }
            chatViewModel.addMessage(input)
            binding.tietInputTextEditText.text?.clear()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.chat_menu_options, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.generateBasicChatHistory -> {
                generateBasicChatHistory()
                true
            }

            R.id.generateSensitiveChatHistory -> {
                generateSensitiveChatHistory()
                true
            }

            R.id.clearChatHistory -> {
                chatViewModel.setMessages(ArrayList())
                true
            }

            else -> false
        }
    }

    private fun generateBasicChatHistory(){

        val chatHistory = ArrayList<Message>()
        val calendar = Calendar.getInstance() // Get the current time

        calendar.add(Calendar.MINUTE, -10)
        chatHistory.add(Message("Hello", true, calendar.timeInMillis))

        calendar.add(Calendar.MINUTE, 10)
        chatHistory.add(Message("Hey", false, calendar.timeInMillis))

        chatViewModel.setMessages(chatHistory)
    }

    private fun generateSensitiveChatHistory() {

        val chatHistory = ArrayList<Message>()
        val calendar = Calendar.getInstance()

        calendar.add(Calendar.MINUTE, -10)
        chatHistory.add(Message("Hi", false, calendar.timeInMillis))

        calendar.add(Calendar.MINUTE, 1)
        chatHistory.add(Message("How are you?", true, calendar.timeInMillis))

        calendar.add(Calendar.MINUTE, 10)
        chatHistory.add(Message("My cat died", false, calendar.timeInMillis))

        chatViewModel.setMessages(chatHistory)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}