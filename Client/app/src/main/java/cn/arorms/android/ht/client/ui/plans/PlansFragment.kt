package cn.arorms.android.ht.client.ui.plans

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import cn.arorms.android.ht.client.MainActivity
import cn.arorms.android.ht.client.databinding.FragmentPlansBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PlansFragment : Fragment() {
    
    private var _binding: FragmentPlansBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: PlansViewModel by viewModels()
    private lateinit var plansAdapter: PlansAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlansBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        setupObservers()
        setupClickListeners()
        
        // 加载用户计划
        viewModel.loadUserPlans()
    }
    
    private fun setupRecyclerView() {
        plansAdapter = PlansAdapter(
            onPlanChecked = { plan, isCompleted ->
                viewModel.updatePlanCompletion(plan.id ?: 0, isCompleted)
            },
            onPlanDelete = { plan ->
                showDeleteConfirmationDialog(plan)
            }
        )
        
        binding.plansRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = plansAdapter
        }
    }
    
    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.plans.collect { plans ->
                plansAdapter.submitList(plans)
                updateEmptyState(plans.isEmpty())
            }
        }
        
        lifecycleScope.launch {
            viewModel.loading.collect { isLoading ->
                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
                binding.plansRecyclerView.visibility = if (isLoading) View.GONE else View.VISIBLE
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
        binding.fabAddPlan.setOnClickListener {
            showAddPlanDialog()
        }
        
        // 移除下拉刷新功能，简化实现
        // binding.swipeRefreshLayout.setOnRefreshListener {
        //     viewModel.loadUserPlans()
        //     binding.swipeRefreshLayout.isRefreshing = false
        // }
    }
    
    private fun showAddPlanDialog() {
        val editText = EditText(requireContext())
        editText.hint = "请输入计划内容"
        
        android.app.AlertDialog.Builder(requireContext())
            .setTitle("添加新计划")
            .setView(editText)
            .setPositiveButton("添加") { _, _ ->
                val content = editText.text.toString().trim()
                if (content.isEmpty()) {
                    showErrorDialog("计划内容不能为空")
                } else {
                    // 简化处理，使用默认截止日期
                    val deadline = java.time.LocalDateTime.now().plusDays(7).toString()
                    viewModel.createPlan(content, deadline)
                }
            }
            .setNegativeButton("取消", null)
            .show()
    }
    
    private fun showDeleteConfirmationDialog(plan: cn.arorms.android.ht.client.models.Plan) {
        android.app.AlertDialog.Builder(requireContext())
            .setTitle("删除计划")
            .setMessage("确定要删除计划 \"${plan.content}\" 吗？")
            .setPositiveButton("删除") { _, _ ->
                viewModel.deletePlan(plan.id ?: 0)
            }
            .setNegativeButton("取消", null)
            .show()
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
        binding.plansRecyclerView.visibility = if (isEmpty) View.GONE else View.VISIBLE
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
