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
    var title: String,
    var firstName: String,
    var lastName: String,
    var gender: String,
    var email: String,
    var password: String,
    var confirmPassword: String,
    var dateOfBirth: LocalDate,
    var roles: List<UserRoleEnum>,
    var occupation: String,

    var numberOfPosts: Int = 0,
    var numberOfComments: Int = 0,
    var status: UserStatusEnum = UserStatusEnum.ACTIVE,
    var passwordUpdated: Boolean = false,
    val creationDate: LocalDateTime = LocalDateTime.now(),
)