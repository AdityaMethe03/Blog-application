package com.example.blog_application.user

import com.example.blog_application.user.dto.UserRequestDto
import com.example.blog_application.user.dto.UserResponseDto
import com.example.blog_application.user.dto.UserUpdateDto
import com.example.blog_application.user.enums.UserRoleEnum
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository, private val passwordEncoder: PasswordEncoder) {

    fun getLoggedInUserId(): String {
        val authentication = SecurityContextHolder.getContext().authentication
        val userEmail = authentication.name
        val userId = userRepository.findByEmail(userEmail).get().id ?: throw IllegalArgumentException("User ID cannot be null")

        return userId
    }

    fun findUserById(id: String): UserResponseDto? {
        val user = userRepository.findById(id).orElse(null) ?: return null

        return UserResponseDto(
            id = user.id!!,
            email = user.email,
            firstName = user.firstName,
            lastName = user.lastName,
            roles = user.roles
        )
    }

    fun findAllUsers(): List<UserResponseDto> {
        return userRepository.findAll().map { user ->
            UserResponseDto(
                id = user.id!!,
                email = user.email,
                firstName = user.firstName,
                lastName = user.lastName,
                roles = user.roles
            )
        }
    }

    fun registerUser(userRequestDto: UserRequestDto): UserResponseDto {
        // 1. Convert DTO to User entity
        val user = User(
            email = userRequestDto.email,
            password = passwordEncoder.encode(userRequestDto.password), // Hash the password
            firstName = userRequestDto.firstName,
            lastName = userRequestDto.lastName,
            roles = userRequestDto.roles ?: listOf(UserRoleEnum.USER)
        )

        // 2. Save the User entity to the database
        val savedUser = userRepository.save(user)

        // 3. Convert the saved User entity to a response DTO
        return UserResponseDto(
            id = savedUser.id!!, // The ID is guaranteed to be non-null after saving
            email = savedUser.email,
            firstName = savedUser.firstName,
            lastName = savedUser.lastName,
            roles = savedUser.roles
        )
    }

    fun updateUser(id: String, userUpdateDto: UserUpdateDto): UserResponseDto? {
        val existingUser = userRepository.findById(id).orElse(null) ?: return null

        // Create a new user object with the updated fields
        val updatedUser = existingUser.copy(
            firstName = userUpdateDto.firstName ?: existingUser.firstName,
            lastName = userUpdateDto.lastName ?: existingUser.lastName,
            title = userUpdateDto.title ?: existingUser.title,
            gender = userUpdateDto.gender ?: existingUser.gender,
            occupation = userUpdateDto.occupation ?: existingUser.occupation
        )

        val savedUser = userRepository.save(updatedUser)

        // Convert to response DTO
        return UserResponseDto(
            id = savedUser.id!!,
            email = savedUser.email,
            firstName = savedUser.firstName,
            lastName = savedUser.lastName,
            roles = savedUser.roles
        )
    }

    fun deleteUser(id: String) {
        // First, check if the user exists
        if (!userRepository.existsById(id)) {
            throw UsernameNotFoundException("User with id $id not found")
        }
        userRepository.deleteById(id)
    }
}