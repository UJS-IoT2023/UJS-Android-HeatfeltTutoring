package cn.arorms.android.ht.server.controller

import cn.arorms.android.ht.server.pojo.entity.Appointment
import cn.arorms.android.ht.server.service.AppointmentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/appointments")
class AppointmentController @Autowired constructor(
    private val appointmentService: AppointmentService
) {

    // Get all appointments
    @GetMapping
    fun getAllAppointments(): ResponseEntity<List<Appointment>> {
        val appointments = appointmentService.getAllAppointments()
        return ResponseEntity(appointments, HttpStatus.OK)
    }

    // Get appointment by ID
    @GetMapping("/{id}")
    fun getAppointmentById(@PathVariable id: Long): ResponseEntity<Appointment> {
        val appointment = appointmentService.getAppointmentById(id)
        return if (appointment.isPresent) {
            ResponseEntity(appointment.get(), HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    // Get appointments by user ID
    @GetMapping("/user/{userId}")
    fun getAppointmentsByUserId(@PathVariable userId: Long): ResponseEntity<List<Appointment>> {
        val appointments = appointmentService.getAppointmentsByUserId(userId)
        return ResponseEntity(appointments, HttpStatus.OK)
    }

    // Create new appointment
    @PostMapping
    fun createAppointment(@RequestBody appointment: Appointment): ResponseEntity<Appointment> {
        val createdAppointment = appointmentService.createAppointment(appointment)
        return ResponseEntity(createdAppointment, HttpStatus.CREATED)
    }

    // Update appointment
    @PutMapping("/{id}")
    fun updateAppointment(@PathVariable id: Long, @RequestBody appointmentDetails: Appointment): ResponseEntity<Appointment> {
        try {
            val updatedAppointment = appointmentService.updateAppointment(id, appointmentDetails)
            return ResponseEntity(updatedAppointment, HttpStatus.OK)
        } catch (e: RuntimeException) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    // Delete appointment
    @DeleteMapping("/{id}")
    fun deleteAppointment(@PathVariable id: Long): ResponseEntity<Void> {
        try {
            appointmentService.deleteAppointment(id)
            return ResponseEntity(HttpStatus.NO_CONTENT)
        } catch (e: RuntimeException) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }
}
