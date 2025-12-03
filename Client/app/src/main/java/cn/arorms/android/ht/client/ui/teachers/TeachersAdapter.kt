package cn.arorms.android.ht.client.ui.teachers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cn.arorms.android.ht.client.R
import cn.arorms.android.ht.client.databinding.ItemTeacherBinding
import cn.arorms.android.ht.client.pojo.models.TeacherSummary

class TeachersAdapter : ListAdapter<TeacherSummary, TeachersAdapter.TeacherViewHolder>(TeacherDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeacherViewHolder {
        val binding = ItemTeacherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TeacherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TeacherViewHolder, position: Int) {
        val teacher = getItem(position)
        holder.bind(teacher)
    }

    inner class TeacherViewHolder(private val binding: ItemTeacherBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(teacher: TeacherSummary) {
            binding.apply {
                teacherName.text = teacher.realName
                teacherPhone.text = "电话: ${teacher.phoneNumber}"
                teacherGender.text = "性别: ${teacher.sex}"
                teacherAddress.text = "地址: ${teacher.address}"
                teacherEducation.text = "学历: ${teacher.educationalBackground}"
                teacherGrades.text = "授课年级: ${teacher.taughtGrades}"
                teacherTaughtSubjects.text = "教授科目: ${teacher.taughtSubjects}"
                
                if (teacher.avatarUrl.isNullOrEmpty()) {
                    teacherIcon.setImageResource(R.drawable.baseline_person_24)
                } else (
                    teacherIcon.setImageResource(R.drawable.ic_menu_gallery)
                )
            }
        }
    }

    companion object {
        private val TeacherDiffCallback = object : DiffUtil.ItemCallback<TeacherSummary>() {
            override fun areItemsTheSame(oldItem: TeacherSummary, newItem: TeacherSummary): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: TeacherSummary, newItem: TeacherSummary): Boolean {
                return oldItem == newItem
            }
        }
    }
}
