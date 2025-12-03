package cn.arorms.android.ht.client.ui.appointments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import cn.arorms.android.ht.client.databinding.FragmentAppointmentsBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AppointmentsFragment : Fragment() {
    
    private var _binding: FragmentAppointmentsBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: AppointmentsViewModel by viewModels()
    private lateinit var appointmentsAdapter: AppointmentsAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAppointmentsBinding.inflate(inflater, container, false)
        return binding.root
    }
    
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        setupRecyclerView()
//        setupObservers()
//        setupClickListeners()
//
//        // 加载预约列表
//        viewModel.loadAppointments()
//    }
    
    private fun setupRecyclerView() {
        appointmentsAdapter = AppointmentsAdapter()
        
        binding.appointmentsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = appointmentsAdapter
        }
    }
    
    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.appointments.collect { appointments ->
                appointmentsAdapter.submitList(appointments)
                updateEmptyState(appointments.isEmpty())
            }
        }
        
        lifecycleScope.launch {
            viewModel.loading.collect { isLoading ->
                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
                binding.appointmentsRecyclerView.visibility = if (isLoading) View.GONE else View.VISIBLE
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
    
//    private fun setupClickListeners() {
//        binding.swipeRefreshLayout.setOnRefreshListener {
//            viewModel.loadAppointments()
//            binding.swipeRefreshLayout.isRefreshing = false
//        }
//
//        binding.fabAddAppointment.setOnClickListener {
//            // 这里可以添加创建新预约的功能
//            showCreateAppointmentDialog()
//        }
//    }
    
    private fun showErrorDialog(message: String) {
        android.app.AlertDialog.Builder(requireContext())
            .setTitle("错误")
            .setMessage(message)
            .setPositiveButton("确定", null)
            .show()
    }
    
    private fun showCreateAppointmentDialog() {
        // 这里可以添加创建预约的对话框
        android.app.AlertDialog.Builder(requireContext())
            .setTitle("创建预约")
            .setMessage("创建预约功能正在开发中...")
            .setPositiveButton("确定", null)
            .show()
    }
    
    private fun updateEmptyState(isEmpty: Boolean) {
        binding.emptyState.visibility = if (isEmpty) View.VISIBLE else View.GONE
        binding.appointmentsRecyclerView.visibility = if (isEmpty) View.GONE else View.VISIBLE
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
