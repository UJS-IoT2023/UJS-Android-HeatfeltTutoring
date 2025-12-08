package cn.arorms.android.ht.client.ui.teachers

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import cn.arorms.android.ht.client.pojo.enums.Subject
import kotlinx.coroutines.FlowPreview
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
        setupSearch()

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

    private fun setupSearch() {
        // 关键字搜索
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val keyword = s?.toString() ?: ""
                viewModel.setKeyword(keyword)
                performSearch()
            }
        })

        // 科目选择
        val subjectButtons = listOf(
            binding.subjectAllButton to null,
            binding.subjectChineseButton to Subject.CHINESE,
            binding.subjectMathButton to Subject.MATH,
            binding.subjectEnglishButton to Subject.ENGLISH,
            binding.subjectChemistryButton to Subject.CHEMISTRY,
            binding.subjectPhysicsButton to Subject.PHYSICS,
            binding.subjectHistoryButton to Subject.HISTORY,
            binding.subjectComputerScienceButton to Subject.COMPUTER_SCIENCE
        )

        subjectButtons.forEach { (button, subject) ->
            button.setOnClickListener {
                updateSubjectButtons(subject)
                viewModel.setSelectedSubject(subject)
                performSearch()
            }
        }
    }

    private fun updateSubjectButtons(selectedSubject: Subject?) {
        val buttons = listOf(
            binding.subjectAllButton to null,
            binding.subjectChineseButton to Subject.CHINESE,
            binding.subjectMathButton to Subject.MATH,
            binding.subjectEnglishButton to Subject.ENGLISH,
            binding.subjectChemistryButton to Subject.CHEMISTRY,
            binding.subjectPhysicsButton to Subject.PHYSICS,
            binding.subjectHistoryButton to Subject.HISTORY,
            binding.subjectComputerScienceButton to Subject.COMPUTER_SCIENCE
        )

        buttons.forEach { (button, subject) ->
            if (subject == selectedSubject) {
                button.setBackgroundColor(resources.getColor(R.color.purple_muted_primary, null))
                button.setTextColor(resources.getColor(R.color.white, null))
            } else {
                button.setBackgroundColor(resources.getColor(android.R.color.transparent, null))
                button.setTextColor(resources.getColor(R.color.gray_900, null))
            }
        }
    }

    @OptIn(FlowPreview::class)
    private fun performSearch() {
        lifecycleScope.launch {
            viewModel.searchTeachers()
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
