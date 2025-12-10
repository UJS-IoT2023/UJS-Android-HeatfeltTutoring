package cn.arorms.android.ht.client.ui.ai

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import cn.arorms.android.ht.client.databinding.FragmentAiChatBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AIChatFragment : Fragment() {

    private var _binding: FragmentAiChatBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AIChatViewModel by viewModels()
    private lateinit var chatAdapter: AIChatAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAiChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObservers()
        setupClickListeners()
    }

    private fun setupRecyclerView() {
        chatAdapter = AIChatAdapter()

        binding.chatRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext()).apply {
                stackFromEnd = true // Messages appear from bottom
            }
            adapter = chatAdapter
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.messages.collect { messages ->
                chatAdapter.submitList(messages)
                if (messages.isNotEmpty()) {
                    binding.chatRecyclerView.scrollToPosition(messages.size - 1)
                }
                updateEmptyState(messages.isEmpty())
            }
        }

        lifecycleScope.launch {
            viewModel.loading.collect { isLoading ->
                binding.sendButton.isEnabled = !isLoading
                binding.messageInput.isEnabled = !isLoading
                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
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
            val message = binding.messageInput.text.toString().trim()
            if (message.isNotEmpty()) {
                viewModel.sendMessage(message)
                binding.messageInput.text.clear()
            }
        }
    }

    private fun showErrorDialog(message: String) {
        android.app.AlertDialog.Builder(requireContext())
            .setTitle("错误")
            .setMessage(message)
            .setPositiveButton("确定", null)
            .show()
    }

    private fun updateEmptyState(isEmpty: Boolean) {
        binding.emptyState.visibility = if (isEmpty) View.VISIBLE else View.GONE
        binding.chatRecyclerView.visibility = if (isEmpty) View.GONE else View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
