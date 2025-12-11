package cn.arorms.android.ht.client.ui.user

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import cn.arorms.android.ht.client.R
import cn.arorms.android.ht.client.databinding.FragmentUserBinding
import cn.arorms.android.ht.client.network.AuthManager
import cn.arorms.android.ht.client.network.RetrofitClient
import cn.arorms.android.ht.client.pojo.models.Appointment
import cn.arorms.android.ht.client.pojo.models.CreateDialogueRequest
import cn.arorms.android.ht.client.pojo.models.Dialogue
import cn.arorms.android.ht.client.pojo.models.TeacherSummary
import cn.arorms.android.ht.client.pojo.models.User
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

class UserFragment : Fragment() {

    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserViewModel by viewModels()
    private lateinit var commentAdapter: CommentAdapter

    private var userId: Long = 0
    private var isOwnProfile: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = arguments?.getLong("userId") ?: 0
        val currentUserId = AuthManager.getUserId()
        isOwnProfile = userId == currentUserId
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObservers()
        setupClickListeners()
        setupKeyboardHandling()

        // 加载用户资料和评论
        viewModel.loadUserProfile(userId)
        viewModel.loadComments(userId)

        // 根据是否为自己的资料显示不同UI
        updateUIForProfileType()
    }

    private fun setupRecyclerView() {
        commentAdapter = CommentAdapter()
        binding.commentsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = commentAdapter
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.user.collect { user ->
                user?.let {
                    updateUserProfile(it)
                    updateUIForProfileType()  // Update UI after user data is loaded
                }
            }
        }

        lifecycleScope.launch {
            viewModel.comments.collect { comments ->
                commentAdapter.submitList(comments)
            }
        }

        lifecycleScope.launch {
            viewModel.loading.collect { isLoading ->
                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }

        lifecycleScope.launch {
            viewModel.error.collect { error ->
                error?.let {
                    showErrorDialog(it)
                    viewModel.clearError()
                }
            }
        }
    }

    private fun setupClickListeners() {
        binding.editButton.setOnClickListener {
            toggleEditMode(true)
        }

        binding.saveButton.setOnClickListener {
            saveUserProfile()
        }

        binding.cancelButton.setOnClickListener {
            toggleEditMode(false)
        }

        binding.addCommentButton.setOnClickListener {
            binding.commentInputSection.visibility = View.VISIBLE
            binding.addCommentButton.visibility = View.GONE
        }

        binding.bookAppointmentButton.setOnClickListener {
            showAppointmentDialog()
        }

        binding.chatButton.setOnClickListener {
            startChat()
        }

        binding.sendCommentButton.setOnClickListener {
            sendComment()
        }
    }

    private fun updateUIForProfileType() {
        if (isOwnProfile) {
            binding.editButton.visibility = View.VISIBLE
            binding.bookAppointmentButton.visibility = View.GONE
            binding.chatButton.visibility = View.GONE
            binding.addCommentButton.visibility = View.GONE
            binding.commentInputSection.visibility = View.GONE
        } else {
            binding.editButton.visibility = View.GONE
            // Show appointment button only for students viewing teachers
            val currentUserRole = AuthManager.getUser()?.role ?: "STUDENT"
            val isViewingTeacher = viewModel.user.value?.role == "TEACHER"
            binding.bookAppointmentButton.visibility = if (isViewingTeacher) View.VISIBLE else View.GONE
            binding.chatButton.visibility = View.VISIBLE
            binding.addCommentButton.visibility = View.VISIBLE
            binding.commentInputSection.visibility = View.GONE
        }
    }

    private fun updateUserProfile(user: cn.arorms.android.ht.client.pojo.models.User) {
        binding.apply {
            usernameTextView.text = user.username
            emailTextView.text = user.email
            roleTextView.text = user.role ?: "STUDENT"

            // 显示教师详细信息 (仅当用户角色为TEACHER时)
            if (user.role == "TEACHER" && user.teacherProfile != null) {
                teacherProfileSection.visibility = View.VISIBLE
                user.teacherProfile?.let { profile ->
                    educationalBackgroundTextView.text = "学历背景: ${profile.educationalBackground ?: "未填写"}"
                    taughtGradesTextView.text = "教授年级: ${profile.taughtGrades ?: "未填写"}"
                    taughtSubjectsTextView.text = "教授科目: ${profile.taughtSubjects ?: "未填写"}"
                    taughtCoursesTextView.text = "教授课程: ${profile.taughtCourses ?: "未填写"}"
                }
            } else {
                teacherProfileSection.visibility = View.GONE
            }

            // 显示详细信息
            realNameTextView.text = "真实姓名: ${user.realName ?: "未填写"}"
            phoneNumberTextView.text = "电话号码: ${user.phoneNumber ?: "未填写"}"
            genderTextView.text = "性别: ${user.gender ?: "未填写"}"
            wechatIdTextView.text = "微信ID: ${user.wechatId ?: "未填写"}"
            qqIdTextView.text = "QQ ID: ${user.qqId ?: "未填写"}"
            addressTextView.text = "地址: ${user.address ?: "未填写"}"
            createdAtTextView.text = "注册时间: ${user.createdAt.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))}"

            // 填充编辑表单 (仅在非编辑模式时)
            if (editForm.visibility != View.VISIBLE) {
                realNameEdit.setText(user.realName ?: "")
                phoneEdit.setText(user.phoneNumber ?: "")
                genderEdit.setText(user.gender ?: "")
                wechatIdEdit.setText(user.wechatId ?: "")
                qqIdEdit.setText(user.qqId ?: "")
                addressEdit.setText(user.address ?: "")
            }
        }
    }

    private fun toggleEditMode(editMode: Boolean) {
        val user = viewModel.user.value
        val isTeacher = user?.role == "TEACHER" && user.teacherProfile != null

        binding.apply {
            editForm.visibility = if (editMode) View.VISIBLE else View.GONE
            profileSection.visibility = if (editMode) View.GONE else View.VISIBLE
            detailSection.visibility = if (editMode) View.GONE else View.VISIBLE
            teacherProfileSection.visibility = if (editMode) View.GONE else if (isTeacher) View.VISIBLE else View.GONE
            editButton.visibility = if (editMode) View.GONE else View.VISIBLE
            commentSection.visibility = if (editMode) View.GONE else View.VISIBLE
        }
    }

    private fun saveUserProfile() {
        val user = viewModel.user.value ?: return

        val updatedUser = user.copy(
            realName = binding.realNameEdit.text.toString().takeIf { it.isNotBlank() },
            phoneNumber = binding.phoneEdit.text.toString().takeIf { it.isNotBlank() },
            gender = binding.genderEdit.text.toString().takeIf { it.isNotBlank() },
            wechatId = binding.wechatIdEdit.text.toString().takeIf { it.isNotBlank() },
            qqId = binding.qqIdEdit.text.toString().takeIf { it.isNotBlank() },
            address = binding.addressEdit.text.toString().takeIf { it.isNotBlank() }
        )

        viewModel.updateUserProfile(updatedUser)
        toggleEditMode(false)
    }

    private fun sendComment() {
        val content = binding.commentEditText.text.toString().trim()
        if (content.isEmpty()) {
            showErrorDialog("评论内容不能为空")
            return
        }

        viewModel.createComment(userId, content)
        binding.commentEditText.text.clear()
    }

    private fun showAppointmentDialog() {
        val teacher = viewModel.user.value ?: return
        val currentUser = AuthManager.getUser() ?: return

        // Create dialog layout
        val layout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 16, 32, 16)
        }

        val subjectEdit = EditText(requireContext()).apply {
            hint = "预约科目"
            setPadding(16, 16, 16, 16)
        }

        val dateTimeEdit = EditText(requireContext()).apply {
            hint = "点击选择预约时间"
            setPadding(16, 16, 16, 16)
            isFocusable = false
            isClickable = true
        }

        var selectedDateTime: LocalDateTime? = null

        dateTimeEdit.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(requireContext(), { _, year, month, day ->
                TimePickerDialog(requireContext(), { _, hour, minute ->
                    selectedDateTime = LocalDateTime.of(year, month + 1, day, hour, minute)
                    dateTimeEdit.setText(selectedDateTime?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        layout.addView(subjectEdit)
        layout.addView(dateTimeEdit)

        android.app.AlertDialog.Builder(requireContext())
            .setTitle("预约教师: ${teacher.realName ?: teacher.username}")
            .setView(layout)
            .setPositiveButton("预约") { _, _ ->
                val subject = subjectEdit.text.toString().trim()
                if (subject.isEmpty()) {
                    showErrorDialog("预约科目不能为空")
                    return@setPositiveButton
                }
                if (selectedDateTime == null) {
                    showErrorDialog("请选择预约时间")
                    return@setPositiveButton
                }

                createAppointment(currentUser, teacher, subject, selectedDateTime!!)
            }
            .setNegativeButton("取消", null)
            .show()
    }

    private fun createAppointment(user: User, teacherUser: cn.arorms.android.ht.client.pojo.models.User, subject: String, dateTime: LocalDateTime) {
        lifecycleScope.launch {
            try {

                val appointment = Appointment(
                    userId = user.id!!,
                    teacherUserId = teacherUser.id!!,
                    subject = subject,
                    appointmentDate = dateTime
                )

                val apiService = RetrofitClient.instance
                val result = apiService.createAppointment(appointment)

                android.app.AlertDialog.Builder(requireContext())
                    .setTitle("预约成功")
                    .setMessage("您的预约已提交，请等待教师确认。")
                    .setPositiveButton("确定", null)
                    .show()

            } catch (e: Exception) {
                showErrorDialog("预约失败: ${e.message}")
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupKeyboardHandling() {
        // Handle "Done" action on comment EditText
        binding.commentEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard()
                true
            } else {
                false
            }
        }

        // Hide keyboard when tapping outside the EditText
        binding.root.setOnTouchListener { _, _ ->
            hideKeyboard()
            false
        }
    }

    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

    private fun startChat() {
        val user = viewModel.user.value ?: return
        val currentUserId = AuthManager.getUserId()

        lifecycleScope.launch {
            try {
                // First get user's dialogues to check if one exists with this user
                val apiService = RetrofitClient.instance
                val dialogues = apiService.getUserDialogues(currentUserId)

                // Look for existing dialogue between current user and target user
                val existingDialogue = dialogues.find { dialogue ->
                    dialogue.participants?.any { it.id == currentUserId } == true &&
                    dialogue.participants?.any { it.id == userId } == true &&
                    dialogue.participants?.size == 2
                }

                val dialogueId = if (existingDialogue != null) {
                    existingDialogue.id!!
                } else {
                    // Create new dialogue
                    val createRequest = CreateDialogueRequest(participantIds = listOf(currentUserId, userId))
                    val newDialogue = apiService.createDialogue(createRequest)
                    newDialogue.id!!
                }

                // Navigate to chat with dialogue ID
                val bundle = Bundle().apply {
                    putLong("dialogueId", dialogueId)
                    putLong("otherUserId", userId)
                    putString("otherUserName", user.realName ?: user.username)
                }
                findNavController().navigate(R.id.privateChatFragment, bundle)

            } catch (e: Exception) {
                showErrorDialog("创建对话失败: ${e.message}")
            }
        }
    }

    private fun showErrorDialog(message: String) {
        android.app.AlertDialog.Builder(requireContext())
            .setTitle("错误")
            .setMessage(message)
            .setPositiveButton("确定", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(userId: Long): UserFragment {
            val fragment = UserFragment()
            val args = Bundle()
            args.putLong("userId", userId)
            fragment.arguments = args
            return fragment
        }
    }
}
