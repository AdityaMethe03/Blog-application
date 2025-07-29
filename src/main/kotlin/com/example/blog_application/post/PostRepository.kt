package com.example.blog_application.post

import com.example.blog_application.user.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface PostRepository : MongoRepository<Post, String> {

}