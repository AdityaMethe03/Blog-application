package com.example.blog_application.post.dto

import java.time.LocalDateTime

class PostResponseDto (
    val id: String,
    val title: String,
    val content: String,
    val likes: Int,
    val comments: Int,
    val authorId: String,
    val authorName: String,
    val createdAt: LocalDateTime,
    var updatedAt: LocalDateTime,
)