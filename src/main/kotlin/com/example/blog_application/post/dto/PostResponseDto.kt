package com.example.blog_application.post.dto

import com.example.blog_application.comment.Comment
import java.time.LocalDateTime

class PostResponseDto (
    val id: String,
    val title: String,
    val content: String,
    val likesCount: Int,
    val commentsCount: Int,
    val authorId: String,
    val authorName: String,
    val createdAt: LocalDateTime,
    var updatedAt: LocalDateTime,
)