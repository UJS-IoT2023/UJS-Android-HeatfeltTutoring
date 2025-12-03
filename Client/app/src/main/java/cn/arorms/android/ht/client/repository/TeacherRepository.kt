package cn.arorms.android.ht.client.repository

import cn.arorms.android.ht.client.network.ApiService
import cn.arorms.android.ht.client.network.RetrofitClient
import cn.arorms.android.ht.client.pojo.models.TeacherSummary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TeacherRepository {
    private val apiService: ApiService = RetrofitClient.instance
    
    suspend fun getAllTeachers(): Result<List<TeacherSummary>> {
        return try {
            withContext(Dispatchers.IO) {
                val teachers = apiService.getAllTeachers()
                Result.success(teachers)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
//    suspend fun getTeacherById(id: Long): Result<TeacherSummary> {
//        return try {
//            withContext(Dispatchers.IO) {
//                val teacher = apiService.getTeacherById(id)
//                Result.success(teacher)
//            }
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }
//
//    suspend fun createTeacher(teacher: TeacherSummary): Result<TeacherSummary> {
//        return try {
//            withContext(Dispatchers.IO) {
//                val createdTeacher = apiService.createTeacher(teacher)
//                Result.success(createdTeacher)
//            }
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }
//
//    suspend fun updateTeacher(id: Long, teacher: TeacherSummary): Result<TeacherSummary> {
//        return try {
//            withContext(Dispatchers.IO) {
//                val updatedTeacher = apiService.updateTeacher(id, teacher)
//                Result.success(updatedTeacher)
//            }
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }
//
//    suspend fun deleteTeacher(id: Long): Result<Unit> {
//        return try {
//            withContext(Dispatchers.IO) {
//                apiService.deleteTeacher(id)
//                Result.success(Unit)
//            }
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }
}
