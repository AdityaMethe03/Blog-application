package com.example.blog_application.post

import com.example.blog_application.post.dto.PostRequestDto
import com.example.blog_application.post.dto.PostResponseDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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

    @PostMapping("/post/{postId}/like")
    fun toggleLike(@PathVariable postId: String): PostResponseDto {
        val userId = postService.getLoggedInUserId()

        return postService.toggleLike(postId, userId)
    }

    @PutMapping("/post/{id}")
    fun updatePost(
        @PathVariable id: String,
        @RequestBody postRequestDto: PostRequestDto
    ): ResponseEntity<PostResponseDto> {
        val updatedPostDto = postService.updatePost(id, postRequestDto)
        return if (updatedPostDto != null) {
            ResponseEntity.ok(updatedPostDto)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/post/{id}")
    fun deletePost(@PathVariable id: String): ResponseEntity<Void> {
        postService.deletePost(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/post/allposts")
    fun getAllPosts(): List<PostResponseDto> {
        return postService.findAllPosts()
    }

    @GetMapping("/post/{postId}")
    fun getPostById(@PathVariable postId: String): PostResponseDto {
        return postService.findPostById(postId)
    }
}