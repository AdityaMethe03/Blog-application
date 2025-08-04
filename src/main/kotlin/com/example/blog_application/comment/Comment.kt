package com.example.blog_application.comment

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "comments")
class Comment (
    @Id
    val id: String? = null,
    val content: String,
    val likes: String = "",
    val postId: String,
    val authorId: String,
    val authorName: String,
    val createdAt: LocalDateTime = LocalDateTime.now()
)