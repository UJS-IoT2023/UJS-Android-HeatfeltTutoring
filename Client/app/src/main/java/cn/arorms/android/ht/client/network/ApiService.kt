package cn.arorms.android.ht.client.network

import cn.arorms.android.ht.client.pojo.dto.AgentRequest
import cn.arorms.android.ht.client.pojo.dto.AuthResponse
import cn.arorms.android.ht.client.pojo.dto.EmailVerification
import cn.arorms.android.ht.client.pojo.dto.EmailVerificationRequest
import cn.arorms.android.ht.client.pojo.dto.LoginRequest
import cn.arorms.android.ht.client.pojo.dto.RegisterRequest
import cn.arorms.android.ht.client.pojo.dto.TeacherQueryRequest
import cn.arorms.android.ht.client.pojo.models.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.Response as OkHttpResponse
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

interface ApiService {
    
    // ========== 认证相关接口 ==========
    
    // 用户注册
    @POST("api/auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest): AuthResponse
    
    // 用户登录
    @POST("api/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): AuthResponse
    
    // 验证Token
    @POST("api/auth/verify")
    suspend fun verifyToken(@Header("Authorization") authHeader: String): Map<String, Any?>

    // 发送邮箱验证码
    @POST("api/auth/send-verification-code")
    suspend fun sendVerificationCode(@Body request: EmailVerificationRequest): ResponseBody

    // 验证邮箱
    @POST("api/auth/verify-email")
    suspend fun verifyEmail(@Body verification: EmailVerification): ResponseBody

    // ========== Plans ==========
    
    // Get all plans by user id
    @GET("api/plans/user/{userId}")
    suspend fun getUserPlans(@Path("userId") userId: Long): List<Plan>

//    // 获取所有计划
//    @GET("api/plans")
//    suspend fun getAllPlans(): List<Plan>
    
    // 根据ID获取计划
    @GET("api/plans/{id}")
    suspend fun getPlanById(@Path("id") id: Long): Plan
    
    // 根据完成状态获取计划
    @GET("api/plans/status/{isCompleted}")
    suspend fun getPlansByStatus(@Path("isCompleted") isCompleted: Boolean): List<Plan>
    
    // 创建新计划
    @POST("api/plans")
    suspend fun createPlan(@Body plan: Plan): Plan
    
    // 更新计划
    @PUT("api/plans/{id}")
    suspend fun updatePlan(@Path("id") id: Long, @Body plan: Plan): Plan
    
    // 切换计划完成状态
    @PUT("api/plans/toggle/{id}")
    suspend fun togglePlanCompletion(@Path("id") id: Long): Plan
    
    // 删除计划
    @DELETE("api/plans/{id}")
    suspend fun deletePlan(@Path("id") id: Long): Response<Unit>
    
    // ========== 预约管理接口 ==========
    
    // 获取所有预约
    
    // 根据ID获取预约
    @GET("api/appointments/{id}")
    suspend fun getAppointmentById(@Path("id") id: Long): Appointment
    
    // 获取用户的预约
    @GET("api/appointments/user/{userId}")
    suspend fun getAppointmentsByUserId(@Path("userId") userId: Long): List<Appointment>
    
    // 获取教师的预约
    @GET("api/appointments/teacher/{teacherId}")
    suspend fun getAppointmentsByTeacherId(@Path("teacherId") teacherId: Long): List<Appointment>
    
    // 创建预约
    @POST("api/appointments")
    suspend fun createAppointment(@Body appointment: Appointment): Appointment
    
    // 更新预约
    @PUT("api/appointments/{id}")
    suspend fun updateAppointment(@Path("id") id: Long, @Body appointment: Appointment): Appointment
    
    // 删除预约
    @DELETE("api/appointments/{id}")
    suspend fun deleteAppointment(@Path("id") id: Long)
    
    // ========== User ==========
    
    // Get all teacher users
    @POST("api/users/teachers")
    suspend fun getAllTeachers(@Body query: TeacherQueryRequest? = null): List<User>

    // Get user detail by id
    @GET("api/users/{id}")
    suspend fun getUserById(@Path("id") id: Long): User

    // Update user
    @PUT("api/users/{id}")
    suspend fun updateUser(@Path("id") id: Long, @Body user: User): User

    // ========== Comments ==========

    // Get all comments
    @GET("api/comments")
    suspend fun getAllComments(): List<Comment>

    // Get comment by id
    @GET("api/comments/{id}")
    suspend fun getCommentById(@Path("id") id: Long): Comment

    // Get comments by user id
    @GET("api/comments/user/{userId}")
    suspend fun getCommentsByUserId(@Path("userId") userId: Long): List<Comment>

    // Create comment
    @POST("api/comments")
    suspend fun createComment(@Body comment: Comment): Comment
    
    // ========== 其他接口 ==========

    // 订单管理接口
    @GET("api/orders")
    suspend fun getAllOrders(): List<Order>
    
    @GET("api/orders/{id}")
    suspend fun getOrderById(@Path("id") id: Long): Order
    
    @POST("api/orders")
    suspend fun createOrder(@Body order: Order): Order
    
    @PUT("api/orders/{id}")
    suspend fun updateOrder(@Path("id") id: Long, @Body order: Order): Order
    
    @DELETE("api/orders/{id}")
    suspend fun deleteOrder(@Path("id") id: Long)
    
    // 奖励管理接口
    @GET("api/rewards")
    suspend fun getAllRewards(): List<Reward>
    
    @GET("api/rewards/{id}")
    suspend fun getRewardById(@Path("id") id: Long): Reward
    
    @POST("api/rewards")
    suspend fun createReward(@Body reward: Reward): Reward
    
    @PUT("api/rewards/{id}")
    suspend fun updateReward(@Path("id") id: Long, @Body reward: Reward): Reward
    
    @DELETE("api/rewards/{id}")
    suspend fun deleteReward(@Path("id") id: Long)
    
    // 钱包管理接口
    @GET("api/wallets")
    suspend fun getAllWallets(): List<Wallet>

    @GET("api/wallets/user/{userId}")
    suspend fun getWalletByUserId(@Path("userId") userId: Long): Wallet

    @PUT("api/wallets/{id}")
    suspend fun updateWallet(@Path("id") id: Long, @Body wallet: Wallet): Wallet

    @DELETE("api/wallets/{id}")
    suspend fun deleteWallet(@Path("id") id: Long)

    // ========== AI Chat ==========

    // AI聊天接口（流式响应）
    @POST("api/ai/chat")
    fun chatWithAI(@Body request: AgentRequest): Call<ResponseBody>

    // ========== Chat ==========

    // Create dialogue
    @POST("api/chat/dialogue")
    suspend fun createDialogue(@Body request: CreateDialogueRequest): Dialogue

    // Get user's dialogues
    @GET("api/chat/dialogues/{userId}")
    suspend fun getUserDialogues(@Path("userId") userId: Long): List<Dialogue>

    // Send message to dialogue via REST API
    @POST("api/chat/dialogue/{dialogueId}/send")
    suspend fun sendMessage(
        @Path("dialogueId") dialogueId: Long,
        @Body request: SendMessageRequest
    ): ChatMessage

    // Get messages in dialogue
    @GET("api/chat/dialogue/{dialogueId}/messages")
    suspend fun getDialogueMessages(@Path("dialogueId") dialogueId: Long): List<ChatMessage>

    // Get all messages for current user
    @GET("api/chat/messages")
    suspend fun getUserMessages(): List<ChatMessage>

    // Get unread messages count
    @GET("api/chat/unread/count")
    suspend fun getUnreadMessageCount(): Map<String, Long>

    // Get unread messages
    @GET("api/chat/unread")
    suspend fun getUnreadMessages(): List<ChatMessage>

    // Mark messages as read
    @POST("api/chat/mark-read")
    suspend fun markMessagesAsRead(@Body request: MarkAsReadRequest): Map<String, Int>

    // Mark all messages from a specific dialogue as read
    @POST("api/chat/dialogue/{dialogueId}/mark-read")
    suspend fun markDialogueMessagesAsRead(@Path("dialogueId") dialogueId: Long): Map<String, Int>
}

object RetrofitClient {
//    private const val BASE_URL = "http://172.20.10.2:8080/"
//    private const val BASE_URL = "http://192.168.0.158:8080/"
    private const val BASE_URL = "http://172.20.10.4:8080/"
    
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .header("Content-Type", "application/json")
                .method(original.method, original.body)
            
            // 添加认证token（如果有）
            val token = AuthManager.getToken()
            if (token.isNotEmpty()) {
                requestBuilder.header("Authorization", "Bearer $token")
            }
            
            val request = requestBuilder.build()
            chain.proceed(request)
        }
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()
    
    val gson: Gson by lazy {
        GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeTypeAdapter())
            .create()
    }
    
    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService::class.java)
    }
}
