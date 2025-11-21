package cn.arorms.android.ht.client.repository

import cn.arorms.android.ht.client.models.Appointment
import cn.arorms.android.ht.client.network.ApiService
import cn.arorms.android.ht.client.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppointmentRepository {
    private val apiService: ApiService = RetrofitClient.instance
    
    suspend fun getAllAppointments(): Result<List<Appointment>> {
        return try {
            withContext(Dispatchers.IO) {
                val appointments = apiService.getAllAppointments()
                Result.success(appointments)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getAppointmentById(id: Long): Result<Appointment> {
        return try {
            withContext(Dispatchers.IO) {
                val appointment = apiService.getAppointmentById(id)
                Result.success(appointment)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getAppointmentsByUserId(userId: Long): Result<List<Appointment>> {
        return try {
            withContext(Dispatchers.IO) {
                val appointments = apiService.getAppointmentsByUserId(userId)
                Result.success(appointments)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getAppointmentsByTeacherId(teacherId: Long): Result<List<Appointment>> {
        return try {
            withContext(Dispatchers.IO) {
                val appointments = apiService.getAppointmentsByTeacherId(teacherId)
                Result.success(appointments)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun createAppointment(appointment: Appointment): Result<Appointment> {
        return try {
            withContext(Dispatchers.IO) {
                val createdAppointment = apiService.createAppointment(appointment)
                Result.success(createdAppointment)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateAppointment(id: Long, appointment: Appointment): Result<Appointment> {
        return try {
            withContext(Dispatchers.IO) {
                val updatedAppointment = apiService.updateAppointment(id, appointment)
                Result.success(updatedAppointment)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun deleteAppointment(id: Long): Result<Unit> {
        return try {
            withContext(Dispatchers.IO) {
                apiService.deleteAppointment(id)
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
