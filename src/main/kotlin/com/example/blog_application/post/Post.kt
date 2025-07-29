package com.example.blog_application.post

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "posts")
data class Post(
    @Id
    val id: String? = null,

    val title: String,
    val content: String,
    val likes: Int = 0,
    val comments: Int = 0,

    // Link to the User entity
    val authorId: String,
    val authorName: String,

    val createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now(),
)