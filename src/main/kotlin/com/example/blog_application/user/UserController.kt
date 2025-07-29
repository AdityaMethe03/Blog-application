// In UserController.kt
package com.example.blog_application.user

import com.example.blog_application.security.JwtService
import com.example.blog_application.user.dto.AuthRequestDto
import com.example.blog_application.user.dto.AuthResponseDto
import com.example.blog_application.user.dto.UserRequestDto
import com.example.blog_application.user.dto.UserResponseDto
import com.example.blog_application.user.dto.UserUpdateDto
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
    private val userDetailsService: UserDetailsService
) {

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    fun registerUser(@RequestBody userRequestDto: UserRequestDto): UserResponseDto {
        return userService.registerUser(userRequestDto)
    }

    @PostMapping("/login")
    fun login(@RequestBody authRequest: AuthRequestDto): ResponseEntity<AuthResponseDto> {
        // 1. Authenticate the user
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(authRequest.email, authRequest.password)
        )

        // 2. If authentication is successful, generate a token
        val userDetails = userDetailsService.loadUserByUsername(authRequest.email)
        val token = jwtService.generateToken(userDetails)

        // 3. Return the token
        return ResponseEntity.ok(AuthResponseDto(token))
    }

    @PostMapping("/user/createuser")
    @PreAuthorize("hasAuthority('ADMIN')")
    fun createUser(@RequestBody userRequestDto: UserRequestDto): UserResponseDto {
        return userService.registerUser(userRequestDto)
    }

    @GetMapping("/user/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    fun getUserById(@PathVariable id: String): ResponseEntity<Any> {
        val userDto = userService.findUserById(id)
        return if (userDto != null) {
            ResponseEntity.ok(userDto)
        } else {
            ResponseEntity.notFound().build()
        }
    }
    @GetMapping("/user/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    fun getAll(): List<Any> {
        return userService.findAllUsers()
    }

    @PutMapping("/user/{id}")
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
}