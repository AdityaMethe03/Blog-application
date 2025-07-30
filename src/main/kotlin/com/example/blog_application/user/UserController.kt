// In UserController.kt
package com.example.blog_application.user

import com.example.blog_application.security.JwtService
import com.example.blog_application.user.dto.AuthRequestDto
import com.example.blog_application.user.dto.AuthResponseDto
import com.example.blog_application.user.dto.UserRequestDto
import com.example.blog_application.user.dto.UserResponseDto
import com.example.blog_application.user.dto.UserUpdateDto
import com.example.blog_application.user.enums.UserStatusEnum
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/app/v1")
class UserController(
    private val userService: UserService,
    private val authenticationManager: AuthenticationManager,
    private val jwtService: JwtService,
    private val userDetailsService: UserDetailsService,
    private val userRepository: UserRepository,
) {

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    fun registerUser(@RequestBody userRequestDto: UserRequestDto): UserResponseDto {

        if (userService.userAlreadyExits(userRequestDto.email)) {
            throw Exception("User with email ${userRequestDto.email} already exists")
        }

        return userService.registerUser(userRequestDto)
    }

    @PostMapping("/login")
    fun login(@RequestBody authRequest: AuthRequestDto): ResponseEntity<AuthResponseDto> {
        // 1. Authenticate the user credentials
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                authRequest.email,
                authRequest.password
            )
        )

        // 2. Load the UserDetails (this typically comes from your UserDetailsService)
        val userDetails = userDetailsService.loadUserByUsername(authRequest.email)

        // 3. Fetch the actual User entity from the database
        // This is crucial to get the user's ID and other specific details
        val user = userRepository.findByEmail(authRequest.email)
            .orElseThrow { Exception("User not found after successful authentication. This is an internal error.") }

        // Optional: Add a check for user status if needed
        if (user.status != UserStatusEnum.ACTIVE) {
            throw Exception("User account is not active.")
        }

        // 4. Generate JWT token
        val jwtToken = jwtService.generateToken(userDetails)

        // 5. Build and return AuthResponse including the user's ID
        return ResponseEntity.ok(
            AuthResponseDto(
                id = user.id!!, // Assuming user.id is never null here after fetching
                token = jwtToken,
                email = user.email,
                roles = user.roles
            )
        )
    }

    @PostMapping("/user/createuser")
    @PreAuthorize("hasAuthority('ADMIN')")
    fun createUser(@RequestBody userRequestDto: UserRequestDto): UserResponseDto {

        if (userService.userAlreadyExits(userRequestDto.email)) {
            throw Exception("User with email ${userRequestDto.email} already exists")
        }

        return userService.registerUser(userRequestDto)
    }

    @GetMapping("/user/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    fun getUserById(@PathVariable id: String): ResponseEntity<UserResponseDto> {
        val userDto = userService.findUserById(id)
        return if (userDto != null) {
            ResponseEntity.ok(userDto)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/user/profile/me")
    fun getMe(): ResponseEntity<Any> {

        val userId = userService.getLoggedInUserId()

        val userDto = userService.findUserById(userId)
        return if (userDto != null) {
            ResponseEntity.ok(userDto)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/user/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    fun getAll(): List<UserResponseDto> {
        return userService.findAllUsers()
    }

    @PutMapping("/user/updateuser/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    fun updateUser(
        @PathVariable id: String,
        @RequestBody userUpdateDto: UserUpdateDto
    ): ResponseEntity<UserResponseDto> {
        val updatedUserDto = userService.updateUser(id, userUpdateDto)
        return if (updatedUserDto != null) {
            ResponseEntity.ok(updatedUserDto)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PutMapping("/user/updateprofile/me")
    fun updateMe(
        @RequestBody userUpdateDto: UserUpdateDto
    ): ResponseEntity<UserResponseDto> {

        val userId = userService.getLoggedInUserId()

        val updatedUserDto = userService.updateUser(userId, userUpdateDto)
        return if (updatedUserDto != null) {
            ResponseEntity.ok(updatedUserDto)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/user/deleteuser/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    fun deleteUser(@PathVariable id: String): ResponseEntity<Void> {
        userService.deleteUser(id)
        return ResponseEntity.noContent().build() // Returns a 204 No Content response
    }
}