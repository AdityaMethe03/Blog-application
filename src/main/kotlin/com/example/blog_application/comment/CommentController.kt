package com.example.blog_application.comment

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/app/v1")
class CommentController(
    private val commentService: CommentService
) {

}