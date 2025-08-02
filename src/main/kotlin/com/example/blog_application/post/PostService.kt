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
import org.springframework.security.access.AccessDeniedException
import java.time.LocalDateTime

@Service
class PostService(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository,
    private val mongoTemplate: MongoTemplate
) {

    fun toPostResponseDto(post: Post): PostResponseDto {
        return PostResponseDto(
            id = post.id!!,
            title = post.title,
            content = post.content,
            likedBy = post.likedBy,
            likesCount = post.likedBy.size,
            commentsCount = post.comments.size,
            authorId = post.authorId,
            authorName = post.authorName,
            createdAt = post.createdAt,
            updatedAt = post.updatedAt
        )
    }

    fun getLoggedInUserId(): String {
        val authentication = SecurityContextHolder.getContext().authentication
        val userEmail = authentication.name
        val userId = userRepository.findByEmail(userEmail).get().id ?: throw IllegalArgumentException("User ID cannot be null")

        return userId
    }

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

        return toPostResponseDto(savedPost)
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

        mongoTemplate.findAndModify(query, update, Post::class.java)
            ?: throw Exception("Could not update post")

        val updatedPost = postRepository.findById(postId)
            .orElseThrow { Exception("Post not found after update, this indicates a serious data inconsistency.") }

        return toPostResponseDto(updatedPost)
    }

    fun updatePost(id: String, postRequestDto: PostRequestDto): PostResponseDto? {
        val existingPost = postRepository.findById(id)
            .orElseThrow { Exception("Post with id $id not found") }

        val currentUserId = getLoggedInUserId()

        if (existingPost.authorId != currentUserId) {
            throw AccessDeniedException("You are not authorized to update this post")
        }

        val updatedPost = existingPost.copy(
            title = postRequestDto.title,
            content = postRequestDto.content,
            updatedAt = LocalDateTime.now()
        )

        val savedPost = postRepository.save(updatedPost)

        return toPostResponseDto(savedPost)
    }

    fun deletePost(id: String) {
        val existingPost = postRepository.findById(id)
            .orElseThrow { Exception("Post with id $id not found") }

        val currentUserId = getLoggedInUserId()

        if (existingPost.authorId != currentUserId) {
            throw AccessDeniedException("You are not authorized to update this post")
        }

        postRepository.deleteById(id)
    }

    fun findAllPosts(): List<PostResponseDto> {
        return postRepository.findAll().map { post ->
            toPostResponseDto(post)
        }
    }

    fun findPostById(postId: String): PostResponseDto {
        val post = postRepository.findById(postId)
            .orElseThrow { Exception("Post not found.") }

        return toPostResponseDto(post)
    }

}