package com.app.arcadeaproject.ui.main.social

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.arcadeaproject.R
import com.app.arcadeaproject.data.remote.ApiClient
import com.app.arcadeaproject.data.remote.model.MessageRequest
import com.app.arcadeaproject.ui.adapter.MessageAdapter
import com.bumptech.glide.Glide
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ChatFragment : Fragment() {

    private lateinit var rvMessages: RecyclerView
    private lateinit var etMessage: EditText
    private lateinit var btnSend: ImageButton
    private lateinit var tvName: TextView
    private lateinit var ivAvatar: ImageView
    private lateinit var adapter: MessageAdapter

    private var senderId: Int = -1
    private var receiverId: Int = -1
    private var receiverName: String = ""
    private var receiverAvatar: String? = null

    private var isPolling = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            receiverId = it.getInt("receiver_id")
            receiverName = it.getString("receiver_name") ?: ""
            receiverAvatar = it.getString("receiver_avatar")
        }
        val sharedPref = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        senderId = sharedPref.getInt("user_id", -1)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.social_fragment_chat, container, false)

        rvMessages = view.findViewById(R.id.rv_messages)
        etMessage = view.findViewById(R.id.et_message)
        btnSend = view.findViewById(R.id.btn_send)
        tvName = view.findViewById(R.id.tv_chat_name)
        ivAvatar = view.findViewById(R.id.iv_chat_avatar)
        val btnBack = view.findViewById<ImageButton>(R.id.btn_back_chat)

        tvName.text = receiverName
        Glide.with(this)
            .load(receiverAvatar)
            .placeholder(R.drawable.circle_avatar_bg)
            .circleCrop()
            .into(ivAvatar)

        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        setupRecyclerView()

        btnSend.setOnClickListener {
            val text = etMessage.text.toString().trim()
            if (text.isNotEmpty()) {
                sendMessage(text)
            }
        }

        startPollingMessages()

        return view
    }

    private fun setupRecyclerView() {
        adapter = MessageAdapter(senderId, emptyList())
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.stackFromEnd = true
        rvMessages.layoutManager = layoutManager
        rvMessages.adapter = adapter
    }

    private fun sendMessage(message: String) {
        lifecycleScope.launch {
            try {
                val request = MessageRequest(senderId, receiverId, message)
                val response = ApiClient.instance.sendMessage(request)
                if (response.isSuccessful && response.body()?.success == true) {
                    etMessage.text.clear()
                    loadMessages() // Immediate refresh
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error sending message", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadMessages() {
        lifecycleScope.launch {
            try {
                val response = ApiClient.instance.getMessages(senderId, receiverId)
                if (response.isSuccessful && response.body()?.success == true) {
                    val messages = response.body()?.messages ?: emptyList()
                    adapter.updateMessages(messages)
                    if (messages.isNotEmpty()) {
                        rvMessages.scrollToPosition(messages.size - 1)
                    }
                }
            } catch (e: Exception) {
                // Silent fail for polling
            }
        }
    }

    private fun startPollingMessages() {
        isPolling = true
        lifecycleScope.launch {
            while (isPolling) {
                loadMessages()
                delay(3000) // Poll every 3 seconds
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isPolling = false
    }
}