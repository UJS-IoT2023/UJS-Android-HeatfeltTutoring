package cn.arorms.android.ht.client.ui.appointments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cn.arorms.android.ht.client.R
import cn.arorms.android.ht.client.databinding.ItemAppointmentBinding
import cn.arorms.android.ht.client.pojo.models.Appointment
import java.time.format.DateTimeFormatter

class AppointmentsAdapter : ListAdapter<Appointment, AppointmentsAdapter.AppointmentViewHolder>(AppointmentDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val binding = ItemAppointmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AppointmentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        val appointment = getItem(position)
        holder.bind(appointment)
    }

    inner class AppointmentViewHolder(private val binding: ItemAppointmentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(appointment: Appointment) {
            binding.apply {
                // 格式化日期时间
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                val formattedDate = appointment.appointmentDate.format(formatter)
                
//                appointmentDate.text = "预约时间: $formattedDate"
//                appointmentSubject.text = "科目: ${appointment.subject}"
//                appointmentUser.text = "学生: ${appointment.user.username}"
//                appointmentTeacher.text = "教师: ${appointment.teacher.name}"
                
                // 设置图标
                appointmentIcon.setImageResource(R.drawable.outline_calendar_clock_24)
                
                // 设置点击事件
                root.setOnClickListener {
                    // 这里可以添加点击事件处理
                }
            }
        }
    }

    companion object {
        private val AppointmentDiffCallback = object : DiffUtil.ItemCallback<Appointment>() {
            override fun areItemsTheSame(oldItem: Appointment, newItem: Appointment): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Appointment, newItem: Appointment): Boolean {
                return oldItem == newItem
            }
        }
    }
}
