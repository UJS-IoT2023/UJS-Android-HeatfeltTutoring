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
import cn.arorms.android.ht.client.R
import cn.arorms.android.ht.client.databinding.FragmentChatSessionsBinding
import kotlinx.coroutines.launch

class ChatSessionsFragment : Fragment() {

    private var _binding: FragmentChatSessionsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ChatSessionsViewModel by viewModels()
    private lateinit var chatSessionsAdapter: ChatSessionsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatSessionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObservers()
        setupClickListeners()

        // 加载聊天会话
        viewModel.loadChatSessions()
    }

    private fun setupRecyclerView() {
        chatSessionsAdapter = ChatSessionsAdapter { session ->
            // Navigate to private chat when session is clicked
            val bundle = Bundle().apply {
                putLong("otherUserId", session.otherUserId)
                putString("otherUserName", session.otherUserName)
            }
            findNavController().navigate(R.id.privateChatFragment, bundle)
        }

        binding.chatSessionsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = chatSessionsAdapter
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.chatSessions.collect { sessions ->
                chatSessionsAdapter.submitList(sessions)
                updateEmptyState(sessions.isEmpty())
            }
        }

        lifecycleScope.launch {
            viewModel.loading.collect { isLoading ->
                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
                binding.chatSessionsRecyclerView.visibility = if (isLoading) View.GONE else View.VISIBLE
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
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.loadChatSessions()
            binding.swipeRefreshLayout.isRefreshing = false
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
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
        binding.chatSessionsRecyclerView.visibility = if (isEmpty) View.GONE else View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
