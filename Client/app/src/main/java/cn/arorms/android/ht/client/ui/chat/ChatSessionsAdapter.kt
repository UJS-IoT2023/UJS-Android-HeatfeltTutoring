package cn.arorms.android.ht.client.ui.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cn.arorms.android.ht.client.R
import cn.arorms.android.ht.client.databinding.ItemChatSessionBinding
import java.time.format.DateTimeFormatter

class ChatSessionsAdapter(private val onSessionClick: (ChatSession) -> Unit) : ListAdapter<ChatSession, ChatSessionsAdapter.ChatSessionViewHolder>(ChatSessionDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatSessionViewHolder {
        val binding = ItemChatSessionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatSessionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatSessionViewHolder, position: Int) {
        val session = getItem(position)
        holder.bind(session, onSessionClick)
    }

    inner class ChatSessionViewHolder(private val binding: ItemChatSessionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(session: ChatSession, onSessionClick: (ChatSession) -> Unit) {
            binding.apply {
                userName.text = session.otherUserName
                lastMessage.text = session.lastMessage ?: "暂无消息"
                timeStamp.text = session.lastMessageTime?.format(DateTimeFormatter.ofPattern("HH:mm")) ?: ""

                if (session.unreadCount > 0) {
                    unreadBadge.text = session.unreadCount.toString()
                    unreadBadge.visibility = android.view.View.VISIBLE
                } else {
                    unreadBadge.visibility = android.view.View.GONE
                }

                // Set click listener on the root view
                root.setOnClickListener {
                    onSessionClick(session)
                }
            }
        }
    }

    companion object {
        private val ChatSessionDiffCallback = object : DiffUtil.ItemCallback<ChatSession>() {
            override fun areItemsTheSame(oldItem: ChatSession, newItem: ChatSession): Boolean {
                return oldItem.dialogueId == newItem.dialogueId
            }

            override fun areContentsTheSame(oldItem: ChatSession, newItem: ChatSession): Boolean {
                return oldItem == newItem
            }
        }
    }
}
