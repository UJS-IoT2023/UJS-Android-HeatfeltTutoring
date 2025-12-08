package cn.arorms.android.ht.client.ui.teachers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cn.arorms.android.ht.client.R
import cn.arorms.android.ht.client.databinding.ItemTeacherBinding
import cn.arorms.android.ht.client.pojo.models.User

class TeachersAdapter(private val onTeacherClick: (User) -> Unit) : ListAdapter<User, TeachersAdapter.TeacherViewHolder>(TeacherDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeacherViewHolder {
        val binding = ItemTeacherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TeacherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TeacherViewHolder, position: Int) {
        val teacher = getItem(position)
        holder.bind(teacher, onTeacherClick)
    }

    inner class TeacherViewHolder(private val binding: ItemTeacherBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(teacher: User, onTeacherClick: (User) -> Unit) {
            binding.apply {
                teacherName.text = teacher.realName ?: teacher.username
                teacherPhone.text = "电话: ${teacher.phoneNumber ?: "未填写"}"
                teacherGender.text = "性别: ${teacher.gender ?: "未填写"}"
                teacherAddress.text = "地址: ${teacher.address ?: "未填写"}"
                teacherEducation.text = "学历: ${teacher.teacherProfile?.educationalBackground ?: "未填写"}"
                teacherGrades.text = "授课年级: ${teacher.teacherProfile?.taughtGrades ?: "未填写"}"
                teacherTaughtSubjects.text = "教授科目: ${teacher.teacherProfile?.taughtSubjects ?: "未填写"}"

                if (teacher.avatarUrl.isNullOrEmpty()) {
                    teacherIcon.setImageResource(R.drawable.baseline_person_24)
                } else {
                    teacherIcon.setImageResource(R.drawable.ic_menu_gallery)
                }

                // Set click listener on the root view
                root.setOnClickListener {
                    onTeacherClick(teacher)
                }
            }
        }
    }

    companion object {
        private val TeacherDiffCallback = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem
            }
        }
    }
}
