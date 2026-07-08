package com.app.arcadeaproject.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.arcadeaproject.R
import com.app.arcadeaproject.data.remote.model.MessageData

class MessageAdapter(
    private val currentUserId: Int,
    private var messages: List<MessageData>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_SENT = 1
        private const val VIEW_TYPE_RECEIVED = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].senderId == currentUserId) VIEW_TYPE_SENT else VIEW_TYPE_RECEIVED
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_SENT) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.social_item_message_sent, parent, false)
            SentViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.social_item_message_received, parent, false)
            ReceivedViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        if (holder is SentViewHolder) {
            holder.tvMessage.text = message.message
            holder.tvTime.text = formatTime(message.createdAt)
        } else if (holder is ReceivedViewHolder) {
            holder.tvMessage.text = message.message
            holder.tvTime.text = formatTime(message.createdAt)
        }
    }

    override fun getItemCount(): Int = messages.size

    fun updateMessages(newMessages: List<MessageData>) {
        messages = newMessages
        notifyDataSetChanged()
    }

    private fun formatTime(timestamp: String): String {
        // Simple formatting, adjust as needed based on backend format (e.g., 2023-10-27T10:00:00Z)
        return try {
            timestamp.substring(11, 16)
        } catch (e: Exception) {
            timestamp
        }
    }

    class SentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvMessage: TextView = view.findViewById(R.id.tv_message_sent)
        val tvTime: TextView = view.findViewById(R.id.tv_time_sent)
    }

    class ReceivedViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvMessage: TextView = view.findViewById(R.id.tv_message_received)
        val tvTime: TextView = view.findViewById(R.id.tv_time_received)
    }
}