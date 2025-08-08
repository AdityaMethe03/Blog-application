package com.example.blog_application.post.dto

import java.time.LocalDateTime

data class PostResponseDto (
    val id: String,
    val title: String,
    val content: String,
    val likedBy: MutableSet<String> = mutableSetOf(),
    val likesCount: Int,
    val commentsCount: Int,
    val commentInfo: List<CommentsInfoDto> = listOf(),
    val authorId: String,
    val authorName: String,
    val createdAt: LocalDateTime,
    var updatedAt: LocalDateTime,
)