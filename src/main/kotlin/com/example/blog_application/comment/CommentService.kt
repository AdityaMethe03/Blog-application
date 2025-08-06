package com.example.blog_application.comment

import com.example.blog_application.comment.dto.CommentRequestDto
import com.example.blog_application.comment.dto.CommentResponseDto
import com.example.blog_application.user.UserRepository
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CommentService(
    private val commentRepository: CommentRepository,
    private val userRepository: UserRepository,
    private val mongoTemplate: MongoTemplate
) {

    fun toCommentResponseDto(comment: Comment): CommentResponseDto {
        return CommentResponseDto(
            id = comment.id!!,
            content = comment.content,
            likes = comment.likes,
            postId = comment.postId,
            authorId = comment.authorId,
            authorName = comment.authorName,
            createdAt = comment.createdAt,
        )
    }

    fun getLoggedInUserId(): String {
        val authentication = SecurityContextHolder.getContext().authentication
        val userEmail = authentication.name
        val userId = userRepository.findByEmail(userEmail).get().id ?: throw IllegalArgumentException("User ID cannot be null")

        return userId
    }

    fun createComment(commentRequestDto: CommentRequestDto): CommentResponseDto {

        val userEmail = SecurityContextHolder.getContext().authentication.name
        val author = userRepository.findByEmail(userEmail)
            .orElseThrow { UsernameNotFoundException("Author not found") }

        val comment = Comment(
            content = commentRequestDto.content,
            postId = commentRequestDto.postId,
            authorId = author.id!!,
            authorName = "${author.firstName} ${author.lastName}"
        )

        val savedComment = commentRepository.save(comment)

        return toCommentResponseDto(savedComment)
    }
}