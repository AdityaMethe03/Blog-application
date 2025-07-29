package com.example.blog_application.post

import com.example.blog_application.post.dto.PostRequestDto
import com.example.blog_application.post.dto.PostResponseDto
import com.example.blog_application.user.UserRepository
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update

@Service
class PostService(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository,
    private val mongoTemplate: MongoTemplate
) {

    fun createPost(postRequestDto: PostRequestDto): PostResponseDto {
        // Get the email of the currently logged-in user
        val userEmail = SecurityContextHolder.getContext().authentication.name
        val author = userRepository.findByEmail(userEmail)
            .orElseThrow { UsernameNotFoundException("Author not found") }

        // Convert DTO to Post entity
        val post = Post(
            title = postRequestDto.title,
            content = postRequestDto.content,
            authorId = author.id!!,
            authorName = "${author.firstName} ${author.lastName}"
        )

        val savedPost = postRepository.save(post)

        return PostResponseDto(
            id = savedPost.id!!,
            title = savedPost.title,
            content = savedPost.content,
            likesCount = savedPost.likedBy.size,
            commentsCount = savedPost.comments.size,
            authorId = savedPost.authorId,
            authorName = savedPost.authorName,
            createdAt = savedPost.createdAt,
            updatedAt = savedPost.updatedAt
        )
    }

    fun getUserIdFromEmail(email: String): String {
        val user = userRepository.findByEmail(email)
            .orElseThrow { UsernameNotFoundException("User not found with email: $email") }
        return user.id!!
    }

    fun toggleLike(postId: String, userId: String): PostResponseDto {
        val post = postRepository.findById(postId)
            .orElseThrow { Exception("Post not found") }

        val query = Query(Criteria.where("id").`is`(postId))
        val update = Update()

        // If user has already liked, unlike. Otherwise, like.
        if (post.likedBy.contains(userId)) {
            update.pull("likedBy", userId)
        } else {
            update.addToSet("likedBy", userId)
        }

        val updatedPost = mongoTemplate.findAndModify(query, update, Post::class.java)
            ?: throw Exception("Could not update post")

        return PostResponseDto(
            id = updatedPost.id!!,
            title = updatedPost.title,
            content = updatedPost.content,
            likesCount = updatedPost.likedBy.size,
            commentsCount = updatedPost.comments.size,
            authorId = updatedPost.authorId,
            authorName = updatedPost.authorName,
            createdAt = updatedPost.createdAt,
            updatedAt = updatedPost.updatedAt
        )
    }

}