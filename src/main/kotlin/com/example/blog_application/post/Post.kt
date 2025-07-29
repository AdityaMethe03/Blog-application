package com.example.blog_application.post

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.Date

@Document(collection = "posts")
data class Post(
    @Id
    val id: String? = null,

    val title: String,
    val content: String,
    val likes: String = "",

    // Link to the User entity
    val authorId: String,
    val authorName: String,

    val createdAt: Date = Date(),
    var updatedAt: Date = Date(),
)