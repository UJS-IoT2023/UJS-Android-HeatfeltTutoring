package cn.arorms.android.ht.server.controller

import cn.arorms.android.ht.server.pojo.dto.CommentDto
import cn.arorms.android.ht.server.pojo.entity.Comment
import cn.arorms.android.ht.server.service.CommentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/comments")
class CommentController @Autowired constructor(
    private val commentService: CommentService
) {

//    // Get all comments
//    @GetMapping
//    fun getAllComments(): ResponseEntity<List<Comment>> {
//        val comments = commentService.getAllComments()
//        return ResponseEntity(comments, HttpStatus.OK)
//    }
//
//    // Get comment by ID
//    @GetMapping("/{id}")
//    fun getCommentById(@PathVariable id: Long): ResponseEntity<Comment> {
//        val comment = commentService.getCommentById(id)
//        return if (comment.isPresent) {
//            ResponseEntity(comment.get(), HttpStatus.OK)
//        } else {
//            ResponseEntity(HttpStatus.NOT_FOUND)
//        }
//    }

    // Get comments by user ID
    @GetMapping("/user/{userId}")
    fun getCommentsByUserId(@PathVariable userId: Long): ResponseEntity<List<CommentDto>> {
        val comments = commentService.getCommentsByUserId(userId)
        val commentDtoList = comments.map { CommentDto(it) }
        return ResponseEntity(commentDtoList, HttpStatus.OK)
    }

    // Create new comment
    @PostMapping
    fun createComment(@RequestBody commentDto: CommentDto): ResponseEntity<CommentDto> {
        var createdComment = commentService.projection(commentDto)
        createdComment = commentService.createComment(createdComment)
        return ResponseEntity(commentDto, HttpStatus.CREATED)
    }

    // Update comment
//    @PutMapping("/{id}")
//    fun updateComment(@PathVariable id: Long, @RequestBody commentDetails: Comment): ResponseEntity<Comment> {
//        try {
//            val updatedComment = commentService.updateComment(id, commentDetails)
//            return ResponseEntity(updatedComment, HttpStatus.OK)
//        } catch (e: RuntimeException) {
//            return ResponseEntity(HttpStatus.NOT_FOUND)
//        }
//    }

    // Delete comment
//    @DeleteMapping("/{id}")
//    fun deleteComment(@PathVariable id: Long): ResponseEntity<Void> {
//        try {
//            commentService.deleteComment(id)
//            return ResponseEntity(HttpStatus.NO_CONTENT)
//        } catch (e: RuntimeException) {
//            return ResponseEntity(HttpStatus.NOT_FOUND)
//        }

}
