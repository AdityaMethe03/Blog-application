package com.example.blog_application.comment

import com.example.blog_application.comment.dto.CommentRequestDto
import com.example.blog_application.comment.dto.CommentResponseDto
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/app/v1")
class CommentController(
    private val commentService: CommentService
) {

    @PostMapping("/comment/createcomment")
    @ResponseStatus(HttpStatus.CREATED)
    fun createComment(@RequestBody commentRequestDto: CommentRequestDto): CommentResponseDto {
        return commentService.createComment(commentRequestDto)
    }

}