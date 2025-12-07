package cn.arorms.android.ht.client.ui.wallet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import cn.arorms.android.ht.client.databinding.FragmentWalletBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class WalletFragment : Fragment() {

    private var _binding: FragmentWalletBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WalletViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWalletBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        setupClickListeners()

        // 加载钱包信息
        viewModel.loadWallet()
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.wallet.collect { wallet ->
                wallet?.let { updateWalletInfo(it) }
            }
        }

        lifecycleScope.launch {
            viewModel.loading.collect { isLoading ->
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
        binding.rechargeButton.setOnClickListener {
            // TODO: 实现充值功能
            showErrorDialog("充值功能开发中")
        }

        binding.withdrawButton.setOnClickListener {
            // TODO: 实现提现功能
            showErrorDialog("提现功能开发中")
        }

        binding.transactionHistoryButton.setOnClickListener {
            // TODO: 实现交易记录功能
            showErrorDialog("交易记录功能开发中")
        }
    }

    private fun updateWalletInfo(wallet: cn.arorms.android.ht.client.pojo.models.Wallet) {
        binding.apply {
            balanceTextView.text = String.format("¥%.2f", wallet.balance)
            pointsTextView.text = String.format("%.0f 积分", wallet.points)
        }
    }

    private fun showErrorDialog(message: String) {
        android.app.AlertDialog.Builder(requireContext())
            .setTitle("提示")
            .setMessage(message)
            .setPositiveButton("确定", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
