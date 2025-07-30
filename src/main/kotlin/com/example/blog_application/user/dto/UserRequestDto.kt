package com.example.blog_application.user.dto

import com.example.blog_application.user.enums.UserRoleEnum
import java.time.LocalDate

class UserRequestDto (
    val title: String,
    val firstName: String,
    val lastName: String,
    val gender: String,
    val email: String,
    val password: String,
    val confirmPassword: String,
    val dateOfBirth: LocalDate,
    val roles: List<UserRoleEnum>,
    val occupation: String,
)