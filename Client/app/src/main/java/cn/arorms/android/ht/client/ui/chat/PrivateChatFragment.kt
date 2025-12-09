package cn.arorms.android.ht.client.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import cn.arorms.android.ht.client.databinding.FragmentPrivateChatBinding
import kotlinx.coroutines.launch

class PrivateChatFragment : Fragment() {

    private var _binding: FragmentPrivateChatBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PrivateChatViewModel by viewModels()
    private lateinit var chatAdapter: PrivateChatAdapter

    private var dialogueId: Long = 0
    private var otherUserId: Long = 0
    private var otherUserName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            dialogueId = it.getLong("dialogueId")
            otherUserId = it.getLong("otherUserId")
            otherUserName = it.getString("otherUserName") ?: ""
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPrivateChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupRecyclerView()
        setupObservers()
        setupClickListeners()

        // Initialize view model with dialogue data
        if (dialogueId > 0) {
            viewModel.initializeWithDialogue(dialogueId, otherUserId, otherUserName)
        } else {
            viewModel.initialize(otherUserId, otherUserName)
        }
    }

    private fun setupToolbar() {
        binding.toolbar.title = "与 $otherUserName 聊天"
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupRecyclerView() {
        chatAdapter = PrivateChatAdapter()

        binding.messagesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext()).apply {
                stackFromEnd = true
            }
            adapter = chatAdapter
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.messages.collect { messages ->
                chatAdapter.submitList(messages) {
                    // Scroll to bottom when new messages arrive
                    binding.messagesRecyclerView.scrollToPosition(messages.size - 1)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.loading.collect { isLoading ->
                // Could show loading indicator if needed
            }
        }

        lifecycleScope.launch {
            viewModel.error.collect { error ->
                error?.let {
                    showErrorDialog(it)
                    viewModel.clearError()
                }
            }
        }
    }

    private fun setupClickListeners() {
        binding.sendButton.setOnClickListener {
            sendMessage()
        }

        binding.messageInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEND) {
                sendMessage()
                true
            } else {
                false
            }
        }
    }

    private fun sendMessage() {
        val content = binding.messageInput.text?.toString()?.trim() ?: ""
        if (content.isNotEmpty()) {
            viewModel.sendMessage(content)
            binding.messageInput.setText("")
        }
    }

    private fun showErrorDialog(message: String) {
        android.app.AlertDialog.Builder(requireContext())
            .setTitle("错误")
            .setMessage(message)
            .setPositiveButton("确定", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
