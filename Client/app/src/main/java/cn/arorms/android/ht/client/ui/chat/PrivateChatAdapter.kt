package cn.arorms.android.ht.client.ui.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cn.arorms.android.ht.client.R
import cn.arorms.android.ht.client.databinding.ItemChatMessageBinding
import cn.arorms.android.ht.client.network.AuthManager
import cn.arorms.android.ht.client.pojo.models.ChatMessage
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

class PrivateChatAdapter : ListAdapter<ChatMessage, PrivateChatAdapter.ChatMessageViewHolder>(ChatMessageDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatMessageViewHolder {
        val binding = ItemChatMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatMessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatMessageViewHolder, position: Int) {
        val message = getItem(position)
        holder.bind(message)
    }

    inner class ChatMessageViewHolder(private val binding: ItemChatMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(message: ChatMessage) {
            android.util.Log.d("ChatDebug", "Adapter: Binding message: ${message.content}")
            binding.apply {
                messageText.text = message.content
                messageTime.text = formatTimestamp(message.createdAt)

                val currentUserId = AuthManager.getUserId()
                val isCurrentUser = message.senderId == currentUserId

                if (isCurrentUser) {
                    // Current user message - right aligned
                    messageContainer.setBackgroundResource(R.drawable.user_message_background)
                    messageText.setTextColor(root.context.getColor(R.color.white))
                    (messageContainer.layoutParams as android.widget.LinearLayout.LayoutParams).gravity = android.view.Gravity.END
                    (messageTime.layoutParams as android.widget.LinearLayout.LayoutParams).gravity = android.view.Gravity.END
                } else {
                    // Other user message - left aligned
                    messageContainer.setBackgroundResource(R.drawable.ai_message_background)
                    messageText.setTextColor(root.context.getColor(R.color.black))
                    (messageContainer.layoutParams as android.widget.LinearLayout.LayoutParams).gravity = android.view.Gravity.START
                    (messageTime.layoutParams as android.widget.LinearLayout.LayoutParams).gravity = android.view.Gravity.START
                }
            }
        }

        private fun formatTimestamp(createdAt: LocalDateTime?): String {
            if (createdAt == null) return "Unknown time"

            val timestamp = createdAt.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000
            val now = System.currentTimeMillis()
            val diff = now - timestamp

            return when {
                diff < 60 * 1000 -> "刚刚"
                diff < 60 * 60 * 1000 -> "${diff / (60 * 1000)}分钟前"
                diff < 24 * 60 * 60 * 1000 -> "${diff / (60 * 60 * 1000)}小时前"
                else -> "${diff / (24 * 60 * 60 * 1000)}天前"
            }
        }
    }

    companion object {
        private val ChatMessageDiffCallback = object : DiffUtil.ItemCallback<ChatMessage>() {
            override fun areItemsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
                return oldItem == newItem
            }
        }
    }
}
