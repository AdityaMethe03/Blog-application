package com.example.blog_application.comment

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.Date

@Document(collection = "comments")
class Comment (
    @Id
    val id: String? = null,

    val content: String,
    val likes: String = "",

    //Link to the Post entity
    val postId: String,

    // Link to the User entity
    val authorId: String,
    val authorName: String,

    val createdAt: Date = Date(),
)