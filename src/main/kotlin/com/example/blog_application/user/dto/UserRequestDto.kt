package com.example.blog_application.user.dto

import com.example.blog_application.user.enums.UserRoleEnum

class UserRequestDto (
    val email: String,
    val password: String,
    val firstName: String?,
    val lastName: String?,
    val roles: List<UserRoleEnum>? = null
)