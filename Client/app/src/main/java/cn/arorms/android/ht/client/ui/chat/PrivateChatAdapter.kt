package cn.arorms.android.ht.client.ui.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cn.arorms.android.ht.client.R
import cn.arorms.android.ht.client.databinding.ItemPrivateChatMessageBinding
import cn.arorms.android.ht.client.network.AuthManager
import cn.arorms.android.ht.client.pojo.models.ChatMessage
import java.time.format.DateTimeFormatter

class PrivateChatAdapter : ListAdapter<ChatMessage, PrivateChatAdapter.ChatMessageViewHolder>(ChatMessageDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatMessageViewHolder {
        val binding = ItemPrivateChatMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatMessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatMessageViewHolder, position: Int) {
        val message = getItem(position)
        holder.bind(message)
    }

    inner class ChatMessageViewHolder(private val binding: ItemPrivateChatMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(message: ChatMessage) {
            binding.apply {
                messageText.text = message.content
                messageTime.text = message.createdAt.format(DateTimeFormatter.ofPattern("HH:mm"))

                val currentUserId = AuthManager.getUserId()
                val isCurrentUser = message.senderId == currentUserId

                if (isCurrentUser) {
                    // Current user message - right aligned
                    messageContainer.setBackgroundResource(R.drawable.user_message_background)
                    messageText.setTextColor(root.context.getColor(R.color.white))
                    messageContainer.layoutParams = (messageContainer.layoutParams as androidx.constraintlayout.widget.ConstraintLayout.LayoutParams).apply {
                        horizontalBias = 1.0f
                    }
                } else {
                    // Other user message - left aligned
                    messageContainer.setBackgroundResource(R.drawable.ai_message_background)
                    messageText.setTextColor(root.context.getColor(R.color.black))
                    messageContainer.layoutParams = (messageContainer.layoutParams as androidx.constraintlayout.widget.ConstraintLayout.LayoutParams).apply {
                        horizontalBias = 0.0f
                    }
                }
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
