package cn.arorms.android.ht.server.service

import cn.arorms.android.ht.server.pojo.entity.Feedback
import cn.arorms.android.ht.server.repository.FeedbackRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class FeedbackService @Autowired constructor(
    private val feedbackRepository: FeedbackRepository
) {

    // Get all feedbacks
    fun getAllFeedbacks(): List<Feedback> {
        return feedbackRepository.findAll()
    }

    // Get feedback by ID
    fun getFeedbackById(id: Long): Optional<Feedback> {
        return feedbackRepository.findById(id)
    }

    // Get feedbacks by user ID
    fun getFeedbacksByUserId(userId: Long): List<Feedback> {
        return feedbackRepository.findByUserId(userId)
    }

    // Create new feedback
    fun createFeedback(feedback: Feedback): Feedback {
        return feedbackRepository.save(feedback)
    }

    // Update feedback
//    fun updateFeedback(id: Long, feedbackDetails: Feedback): Feedback {
//        val feedback = feedbackRepository.findById(id)
//            .orElseThrow { RuntimeException("Feedback not found with id: $id") }
//
//        feedback.user = feedbackDetails.user
//        feedback.teacher = feedbackDetails.teacher
//        feedback.content = feedbackDetails.content
//        feedback.createdAt = feedbackDetails.createdAt
//        feedback.subject = feedbackDetails.subject
//
//        return feedbackRepository.save(feedback)
//    }

    // Delete feedback
    fun deleteFeedback(id: Long) {
        val feedback = feedbackRepository.findById(id)
            .orElseThrow { RuntimeException("Feedback not found with id: $id") }
        feedbackRepository.delete(feedback)
    }

    // Check if feedback exists
    fun existsById(id: Long): Boolean {
        return feedbackRepository.existsById(id)
    }
}
