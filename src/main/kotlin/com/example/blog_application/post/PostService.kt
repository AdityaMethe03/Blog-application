package com.example.blog_application.post

import com.example.blog_application.post.dto.PostRequestDto
import com.example.blog_application.post.dto.PostResponseDto
import com.example.blog_application.user.UserRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class PostService(private val postRepository: PostRepository, private val userRepository: UserRepository) {

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
            likes = savedPost.likes,
            comments = savedPost.comments,
            authorId = savedPost.authorId,
            authorName = savedPost.authorName,
            createdAt = savedPost.createdAt,
            updatedAt = savedPost.updatedAt
        )
    }

}