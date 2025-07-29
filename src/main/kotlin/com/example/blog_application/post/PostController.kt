package com.example.blog_application.post

import com.example.blog_application.post.dto.PostRequestDto
import com.example.blog_application.post.dto.PostResponseDto
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/app/v1")
class PostController(
    private val postService: PostService
) {

    @PostMapping("/post/createpost")
    @ResponseStatus(HttpStatus.CREATED)
    fun createPost(@RequestBody postRequestDto: PostRequestDto): PostResponseDto {
        return postService.createPost(postRequestDto)
    }

    @PostMapping("/{postId}/like")
    fun toggleLike(@PathVariable postId: String): PostResponseDto {
        // Get the ID of the currently logged-in user
        val userEmail = SecurityContextHolder.getContext().authentication.name
        // You would need to add a method to get user ID from email
        val userId = postService.getUserIdFromEmail(userEmail)

        return postService.toggleLike(postId, userId)
    }
}