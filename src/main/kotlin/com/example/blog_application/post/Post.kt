package com.example.blog_application.post

import com.example.blog_application.comment.Comment
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "posts")
data class Post(
    @Id
    val id: String? = null,
    var title: String,
    var content: String,
    val likedBy: MutableSet<String> = mutableSetOf(),
    val comments: MutableList<Comment> = mutableListOf(),
    val authorId: String,
    val authorName: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now(),
)