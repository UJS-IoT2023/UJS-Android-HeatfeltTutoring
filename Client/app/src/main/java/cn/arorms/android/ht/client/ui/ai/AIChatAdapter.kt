package cn.arorms.android.ht.client.ui.ai

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cn.arorms.android.ht.client.R
import cn.arorms.android.ht.client.databinding.ItemChatMessageBinding
import io.noties.markwon.Markwon
import io.noties.markwon.linkify.LinkifyPlugin

class AIChatAdapter : ListAdapter<ChatMessage, AIChatAdapter.ChatMessageViewHolder>(ChatMessageDiffCallback) {

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

        private val markwon = Markwon.builder(binding.root.context)
            .usePlugin(LinkifyPlugin.create())
            .build()

        fun bind(message: ChatMessage) {
            binding.apply {
                // Set text with or without markdown based on message type
                if (message.isUser) {
                    messageText.text = message.content
                } else {
                    markwon.setMarkdown(messageText, message.content)
                }

                messageTime.text = formatTimestamp(message.timestamp)

                if (message.isUser) {
                    // User message styling - right aligned
                    messageContainer.setBackgroundResource(R.drawable.user_message_background)
                    messageText.setTextColor(resolveThemeColor(binding.root.context, com.google.android.material.R.attr.colorOnPrimary))
                    (messageContainer.layoutParams as LinearLayout.LayoutParams).gravity = android.view.Gravity.END
                    (messageTime.layoutParams as LinearLayout.LayoutParams).gravity = android.view.Gravity.END
                } else {
                    // AI message styling - left aligned
                    messageContainer.setBackgroundResource(R.drawable.ai_message_background)
                    messageText.setTextColor(resolveThemeColor(binding.root.context, com.google.android.material.R.attr.colorOnSurface))
                    (messageContainer.layoutParams as LinearLayout.LayoutParams).gravity = android.view.Gravity.START
                    (messageTime.layoutParams as LinearLayout.LayoutParams).gravity = android.view.Gravity.START
                }
            }
        }

        private fun resolveThemeColor(context: Context, attr: Int): Int {
            val typedValue = TypedValue()
            val theme = context.theme
            theme.resolveAttribute(attr, typedValue, true)
            return typedValue.data
        }

        private fun formatTimestamp(timestamp: Long): String {
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
