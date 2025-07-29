package com.example.blog_application.user.dto

import com.example.blog_application.user.enums.UserRoleEnum

data class UserResponseDto(
    val id: String,
    val email: String,
    val firstName: String?,
    val lastName: String?,
    val roles: List<UserRoleEnum>
)