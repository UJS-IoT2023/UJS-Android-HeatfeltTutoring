package cn.arorms.android.ht.server.controller

import cn.arorms.android.ht.server.models.Feedback
import cn.arorms.android.ht.server.service.FeedbackService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/feedbacks")
class FeedbackController @Autowired constructor(
    private val feedbackService: FeedbackService
) {

    // Get all feedbacks
    @GetMapping
    fun getAllFeedbacks(): ResponseEntity<List<Feedback>> {
        val feedbacks = feedbackService.getAllFeedbacks()
        return ResponseEntity(feedbacks, HttpStatus.OK)
    }

    // Get feedback by ID
    @GetMapping("/{id}")
    fun getFeedbackById(@PathVariable id: Long): ResponseEntity<Feedback> {
        val feedback = feedbackService.getFeedbackById(id)
        return if (feedback.isPresent) {
            ResponseEntity(feedback.get(), HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    // Get feedbacks by user ID
    @GetMapping("/user/{userId}")
    fun getFeedbacksByUserId(@PathVariable userId: Long): ResponseEntity<List<Feedback>> {
        val feedbacks = feedbackService.getFeedbacksByUserId(userId)
        return ResponseEntity(feedbacks, HttpStatus.OK)
    }

    // Create new feedback
    @PostMapping
    fun createFeedback(@RequestBody feedback: Feedback): ResponseEntity<Feedback> {
        val createdFeedback = feedbackService.createFeedback(feedback)
        return ResponseEntity(createdFeedback, HttpStatus.CREATED)
    }

    // Update feedback
//    @PutMapping("/{id}")
//    fun updateFeedback(@PathVariable id: Long, @RequestBody feedbackDetails: Feedback): ResponseEntity<Feedback> {
//        try {
//            val updatedFeedback = feedbackService.updateFeedback(id, feedbackDetails)
//            return ResponseEntity(updatedFeedback, HttpStatus.OK)
//        } catch (e: RuntimeException) {
//            return ResponseEntity(HttpStatus.NOT_FOUND)
//        }
//    }

    // Delete feedback
//    @DeleteMapping("/{id}")
//    fun deleteFeedback(@PathVariable id: Long): ResponseEntity<Void> {
//        try {
//            feedbackService.deleteFeedback(id)
//            return ResponseEntity(HttpStatus.NO_CONTENT)
//        } catch (e: RuntimeException) {
//            return ResponseEntity(HttpStatus.NOT_FOUND)
//        }
//    }
}
