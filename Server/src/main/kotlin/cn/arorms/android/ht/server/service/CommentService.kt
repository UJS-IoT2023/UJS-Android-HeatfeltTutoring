package cn.arorms.android.ht.server.service

import cn.arorms.android.ht.server.pojo.dto.CommentDto
import cn.arorms.android.ht.server.pojo.entity.Comment
import cn.arorms.android.ht.server.repository.CommentRepository
import jakarta.mail.internet.NewsAddress
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.expression.spel.ast.Projection
import org.springframework.stereotype.Service
import java.util.Optional
import kotlin.collections.List

@Service
class CommentService @Autowired constructor(
    private val commentRepository: CommentRepository,
    private val userService: UserService
) {

    fun projection(commentDto: CommentDto): Comment {
        val fromUser = userService.getReferenceById(commentDto.fromUserId)
        val toUser = userService.getReferenceById(commentDto.toUserId)
        return Comment(
            fromUser = fromUser,
            toUser = toUser,
            content = commentDto.content
        )
    }
    
//    // Get all comments
//    fun getAllComments(): List<Comment> {
//        return commentRepository.findAll()
//    }
//
//    // Get comment by ID
//    fun getCommentById(id: Long): Optional<Comment> {
//        return commentRepository.findById(id)
//    }

    // Get comments by user ID
    fun getCommentsByUserId(userId: Long): List<Comment> {
        return commentRepository.findByToUserId(userId)
    }

    // Create new comment
    fun createComment(comment: Comment): Comment {
        return commentRepository.save(comment)
    }

    // Update comment
//    fun updateComment(id: Long, commentDetails: Comment): Comment {
//        val comment = commentRepository.findById(id)
//            .orElseThrow { RuntimeException("Comment not found with id: $id") }
//
//        comment.fromUser = commentDetails.fromUser
//        comment.toUser = commentDetails.toUser
//        comment.content = commentDetails.content
//        comment.createdAt = commentDetails.createdAt
//
//        return commentRepository.save(comment)
//    }

    // Delete comment
    fun deleteComment(id: Long) {
        val comment = commentRepository.findById(id)
            .orElseThrow { RuntimeException("Comment not found with id: $id") }
        commentRepository.delete(comment)
    }

    // Check if comment exists
    fun existsById(id: Long): Boolean {
        return commentRepository.existsById(id)
    }
}
