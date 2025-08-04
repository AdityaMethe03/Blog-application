package com.example.blog_application.comment.dto

data class CommentRequestDto (
    val content: String = "",
    val postId: String,
)