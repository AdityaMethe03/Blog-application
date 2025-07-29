package com.example.blog_application.user

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface UserRepository : MongoRepository<User, String> {

    /**
     * Finds all posts created by a specific user.
     * Spring Data creates the query automatically from the method name.
     */
    fun findByEmail(email: String): Optional<User>
}