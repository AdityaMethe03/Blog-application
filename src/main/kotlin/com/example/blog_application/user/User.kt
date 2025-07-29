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

    // --- Core Information (Required) ---
    val email: String,
    val password: String, // This should always be the HASHED password
    val roles: List<UserRoleEnum>,

    // --- User Profile (Optional) ---
    val title: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val gender: String? = null,
    val dateOfBirth: LocalDate? = null,
    val occupation: String? = null,

    // --- Security & Status (with defaults) ---
    val status: UserStatusEnum = UserStatusEnum.ACTIVE,
    val passwordUpdated: Boolean = false,

    // --- Counters & Timestamps (with defaults) ---
    val numberOfPosts: Int = 0,
    val numberOfComments: Int = 0,
    val creationDate: LocalDateTime = LocalDateTime.now(),
)