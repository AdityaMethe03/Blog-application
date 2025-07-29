package com.example.blog_application.post

import com.example.blog_application.post.dto.PostRequestDto
import com.example.blog_application.post.dto.PostResponseDto
import org.springframework.http.HttpStatus
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

}