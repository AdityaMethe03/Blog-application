package com.example.blog_application.user.dto

data class UserUpdateDto(
    val firstName: String?,
    val lastName: String?,
    val title: String?,
    val gender: String?,
    val occupation: String?
)