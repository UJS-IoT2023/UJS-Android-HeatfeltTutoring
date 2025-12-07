package cn.arorms.android.ht.client.ui.teachers

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
import cn.arorms.android.ht.client.databinding.FragmentTeachersBinding
import cn.arorms.android.ht.client.pojo.models.TeacherSummary
import cn.arorms.android.ht.client.ui.user.UserFragment
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class TeachersFragment : Fragment() {
    
    private var _binding: FragmentTeachersBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: TeachersViewModel by viewModels()
    private lateinit var teachersAdapter: TeachersAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTeachersBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        setupObservers()
        setupClickListeners()
        
        // 加载教师列表
        viewModel.loadTeachers()
    }
    
    private fun setupRecyclerView() {
        teachersAdapter = TeachersAdapter { teacher ->
            // Navigate to user profile when teacher is clicked
            val bundle = Bundle().apply {
                putLong("userId", teacher.id ?: 0)
            }
            findNavController().navigate(R.id.userFragment, bundle)
        }

        binding.teachersRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = teachersAdapter
        }
    }
    
    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.teachers.collect { teachers ->
                teachersAdapter.submitList(teachers)
                updateEmptyState(teachers.isEmpty())
            }
        }
        
        lifecycleScope.launch {
            viewModel.loading.collect { isLoading ->
                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
                binding.teachersRecyclerView.visibility = if (isLoading) View.GONE else View.VISIBLE
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
            viewModel.loadTeachers()
            binding.swipeRefreshLayout.isRefreshing = false
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
        binding.teachersRecyclerView.visibility = if (isEmpty) View.GONE else View.VISIBLE
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
