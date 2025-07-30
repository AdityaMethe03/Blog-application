package com.example.blog_application.user

import com.example.blog_application.user.dto.UserRequestDto
import com.example.blog_application.user.dto.UserResponseDto
import com.example.blog_application.user.dto.UserUpdateDto
import com.example.blog_application.user.enums.UserRoleEnum
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import kotlin.String

@Service
class UserService(private val userRepository: UserRepository, private val passwordEncoder: PasswordEncoder) {

    fun toUserResponseDto(user: User): UserResponseDto {
        return UserResponseDto(
            id= user.id!!,
            title= user.email,
            firstName= user.firstName,
            lastName= user.lastName,
            gender= user.gender,
            email= user.email,
            dateOfBirth= user.dateOfBirth,
            roles= user.roles,
            occupation= user.occupation,
            numberOfPosts= user.numberOfPosts,
            numberOfComments= user.numberOfComments,
            status= user.status,
            passwordUpdated= user.passwordUpdated,
            creationDate= user.creationDate,
        )
    }

    fun getLoggedInUserId(): String {
        val authentication = SecurityContextHolder.getContext().authentication
        val userEmail = authentication.name
        val userId = userRepository.findByEmail(userEmail).get().id ?: throw IllegalArgumentException("User ID cannot be null")

        return userId
    }

    fun userAlreadyExits(email: String): Boolean {
        val userOptional = userRepository.findByEmail(email)
        return !userOptional.isEmpty
    }

    fun findUserById(id: String): UserResponseDto? {
        val user = userRepository.findById(id).orElse(null) ?: return null

        return toUserResponseDto(user)
    }

    fun findAllUsers(): List<UserResponseDto> {
        return userRepository.findAll().map { user ->
            toUserResponseDto(user)
        }
    }

    fun registerUser(userRequestDto: UserRequestDto): UserResponseDto {
        // 1. Convert DTO to User entity
        val user = User(
            title = userRequestDto.title,
            firstName = userRequestDto.firstName,
            lastName = userRequestDto.lastName,
            gender = userRequestDto.gender,
            email = userRequestDto.email,
            password = passwordEncoder.encode(userRequestDto.password), // Hash the password
            confirmPassword = passwordEncoder.encode(userRequestDto.confirmPassword),
            dateOfBirth = userRequestDto.dateOfBirth,
            occupation = userRequestDto.occupation,
            roles = userRequestDto.roles
        )

        // 2. Save the User entity to the database
        val savedUser = userRepository.save(user)

        // 3. Convert the saved User entity to a response DTO
        return toUserResponseDto(savedUser)
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

        return toUserResponseDto(savedUser)
    }

    fun deleteUser(id: String) {
        // First, check if the user exists
        if (!userRepository.existsById(id)) {
            throw UsernameNotFoundException("User with id $id not found")
        }
        userRepository.deleteById(id)
    }
}