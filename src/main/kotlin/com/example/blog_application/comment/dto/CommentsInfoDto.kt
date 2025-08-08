package com.example.blog_application.comment.dto

import java.time.LocalDateTime

class CommentsInfoDto (
    val id: String,
    val content: String,
    val likes: String,
    val postId: String,
    val authorId: String,
    val authorName: String,
    val createdAt: LocalDateTime,
)