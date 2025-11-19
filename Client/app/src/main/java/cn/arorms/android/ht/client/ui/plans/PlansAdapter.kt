package cn.arorms.android.ht.client.ui.plans

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cn.arorms.android.ht.client.databinding.ItemPlanBinding
import cn.arorms.android.ht.client.models.Plan

class PlansAdapter(
    private val onPlanChecked: (Plan, Boolean) -> Unit,
    private val onPlanDelete: (Plan) -> Unit
) : ListAdapter<Plan, PlansAdapter.PlanViewHolder>(PlanDiffCallback) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanViewHolder {
        val binding = ItemPlanBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PlanViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: PlanViewHolder, position: Int) {
        val plan = getItem(position)
        holder.bind(plan)
    }
    
    inner class PlanViewHolder(
        private val binding: ItemPlanBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(plan: Plan) {
            binding.apply {
                planContent.text = plan.content
                planDeadline.text = formatDeadline(plan.deadline)
                planCheckbox.isChecked = plan.isCompleted
                
                // 根据完成状态设置样式
                if (plan.isCompleted) {
                    planContent.setTextAppearance(android.R.style.TextAppearance_Material_Body1)
                    planContent.paintFlags = android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
                } else {
                    planContent.setTextAppearance(android.R.style.TextAppearance_Material_Body1)
                    planContent.paintFlags = 0
                }
                
                // 复选框点击事件
                planCheckbox.setOnCheckedChangeListener { _, isChecked ->
                    onPlanChecked(plan, isChecked)
                }
                
                // 删除按钮点击事件
                deleteButton.setOnClickListener {
                    onPlanDelete(plan)
                }
            }
        }
        
        private fun formatDeadline(deadline: String): String {
            // 简化处理，直接显示原始字符串
            return "截止时间: ${deadline.replace("T", " ")}"
        }
    }
    
    companion object {
        private val PlanDiffCallback = object : DiffUtil.ItemCallback<Plan>() {
            override fun areItemsTheSame(oldItem: Plan, newItem: Plan): Boolean {
                return oldItem.id == newItem.id
            }
            
            override fun areContentsTheSame(oldItem: Plan, newItem: Plan): Boolean {
                return oldItem == newItem
            }
        }
    }
}
