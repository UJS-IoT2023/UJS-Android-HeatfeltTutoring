package cn.arorms.android.ht.client.network

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

/**
 * Gson TypeAdapter for LocalDateTime serialization/deserialization.
 * Supports ISO-8601 format (e.g., "2023-12-03T13:58:00")
 */
class LocalDateTimeTypeAdapter : TypeAdapter<LocalDateTime>() {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    
    @Throws(IOException::class)
    override fun write(out: JsonWriter, value: LocalDateTime?) {
        if (value == null) {
            out.nullValue()
        } else {
            out.value(formatter.format(value))
        }
    }
    
    @Throws(IOException::class)
    override fun read(`in`: JsonReader): LocalDateTime? {
        return try {
            if (`in`.peek() == com.google.gson.stream.JsonToken.NULL) {
                `in`.nextNull()
                null
            } else {
                val dateString = `in`.nextString()
                if (dateString.isBlank()) {
                    null
                } else {
                    LocalDateTime.parse(dateString, formatter)
                }
            }
        } catch (e: DateTimeParseException) {
            throw IOException("Failed to parse LocalDateTime: ${e.message}", e)
        }
    }
}
