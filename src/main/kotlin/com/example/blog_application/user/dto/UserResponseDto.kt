package com.example.blog_application.user.dto

import com.example.blog_application.user.enums.UserRoleEnum
import com.example.blog_application.user.enums.UserStatusEnum
import java.time.LocalDate
import java.time.LocalDateTime

data class UserResponseDto(
    val id: String,
    val title: String,
    val firstName: String,
    val lastName: String,
    val gender: String,
    val email: String,
    val dateOfBirth: LocalDate,
    val roles: List<UserRoleEnum>,
    val occupation: String,
    val numberOfPosts: Int,
    val numberOfComments: Int,
    val status: UserStatusEnum,
    val passwordUpdated: Boolean,
    val creationDate: LocalDateTime,
)