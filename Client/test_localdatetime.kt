import cn.arorms.android.ht.client.network.LocalDateTimeTypeAdapter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.time.LocalDateTime

fun main() {
    val gson: Gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeTypeAdapter())
        .create()
    
    // Test 1: Serialization
    val originalDateTime = LocalDateTime.of(2023, 12, 3, 14, 30, 45)
    val json = gson.toJson(originalDateTime)
    println("Serialized JSON: $json")
    
    // Test 2: Deserialization
    val deserializedDateTime = gson.fromJson(json, LocalDateTime::class.java)
    println("Deserialized LocalDateTime: $deserializedDateTime")
    
    // Test 3: Null handling
    val nullJson = gson.toJson(null as LocalDateTime?)
    println("Null serialized: $nullJson")
    val nullDeserialized = gson.fromJson(nullJson, LocalDateTime::class.java)
    println("Null deserialized: $nullDeserialized")
    
    // Test 4: Test with TeacherSummary-like object
    data class TestTeacherSummary(
        val id: Long? = null,
        val username: String,
        val createdAt: LocalDateTime
    )
    
    val testTeacher = TestTeacherSummary(
        id = 1,
        username = "testuser",
        createdAt = LocalDateTime.of(2023, 12, 3, 15, 45, 30)
    )
    
    val teacherJson = gson.toJson(testTeacher)
    println("\nTeacherSummary JSON: $teacherJson")
    
    val deserializedTeacher = gson.fromJson(teacherJson, TestTeacherSummary::class.java)
    println("Deserialized TeacherSummary: $deserializedTeacher")
    
    // Verify
    if (originalDateTime == deserializedDateTime) {
        println("\n✓ Test 1 PASSED: Serialization/Deserialization works correctly")
    } else {
        println("\n✗ Test 1 FAILED: Dates don't match")
    }
    
    if (testTeacher == deserializedTeacher) {
        println("✓ Test 2 PASSED: Complex object serialization works correctly")
    } else {
        println("✗ Test 2 FAILED: Objects don't match")
    }
}
