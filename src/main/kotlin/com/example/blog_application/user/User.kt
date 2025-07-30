package com.example.blog_application.user

import com.example.blog_application.user.enums.UserRoleEnum
import com.example.blog_application.user.enums.UserStatusEnum
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate
import java.time.LocalDateTime

@Document(collection = "user")
data class User(
    @Id
    val id: String? = null,
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

    val numberOfPosts: Int = 0,
    val numberOfComments: Int = 0,
    val status: UserStatusEnum = UserStatusEnum.ACTIVE,
    val passwordUpdated: Boolean = false,
    val creationDate: LocalDateTime = LocalDateTime.now(),
)