package com.example.blog_application.user.dto

import com.example.blog_application.user.enums.UserRoleEnum

data class AuthResponseDto(
    val id: String,
    val email: String,
    val roles: List<UserRoleEnum>,
    val token: String,
)