package cn.arorms.android.ht.server.service

import cn.arorms.android.ht.server.models.Appointment
import cn.arorms.android.ht.server.repository.AppointmentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class AppointmentService @Autowired constructor(
    private val appointmentRepository: AppointmentRepository
) {

    // Get all appointments
    fun getAllAppointments(): List<Appointment> {
        return appointmentRepository.findAll()
    }

    // Get appointment by ID
    fun getAppointmentById(id: Long): Optional<Appointment> {
        return appointmentRepository.findById(id)
    }

    // Get appointments by user ID
    fun getAppointmentsByUserId(userId: Long): List<Appointment> {
        return appointmentRepository.findByUserId(userId)
    }

    // Get appointments by teacher ID
    fun getAppointmentsByTeacherId(teacherId: Long): List<Appointment> {
        return appointmentRepository.findByTeacherId(teacherId)
    }

    // Create new appointment
    fun createAppointment(appointment: Appointment): Appointment {
        return appointmentRepository.save(appointment)
    }

    // Update appointment
    fun updateAppointment(id: Long, appointmentDetails: Appointment): Appointment {
        val appointment = appointmentRepository.findById(id)
            .orElseThrow { RuntimeException("Appointment not found with id: $id") }

        appointment.user = appointmentDetails.user
        appointment.teacher = appointmentDetails.teacher
        appointment.subject = appointmentDetails.subject
        appointment.appointmentDate = appointmentDetails.appointmentDate

        return appointmentRepository.save(appointment)
    }

    // Delete appointment
    fun deleteAppointment(id: Long) {
        val appointment = appointmentRepository.findById(id)
            .orElseThrow { RuntimeException("Appointment not found with id: $id") }
        appointmentRepository.delete(appointment)
    }

    // Check if appointment exists
    fun existsById(id: Long): Boolean {
        return appointmentRepository.existsById(id)
    }
}
